package com.university.shenyang.air.testing.collector.tcp.server.codec;

import com.university.shenyang.air.testing.collector.common.kit.Convert;
import com.university.shenyang.air.testing.collector.common.kit.lang.ArraysUtils;
import com.university.shenyang.air.testing.collector.cache.ChannelPacket;
import com.university.shenyang.air.testing.collector.util.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.List;

/**
 * Created by chenjc on 2017/05/03.
 */
public class DeviceDecoder extends ByteToMessageDecoder {

    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(DeviceDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        LOGGER.info(ctx.channel().toString() + "收到终端原始数据:" + ByteBufUtil.hexDump(in));
        if (in.writerIndex() > in.readerIndex()) {
            String channelKey = ctx.channel().remoteAddress().toString();
            byte[] lastBytes = ChannelPacket.getInstance().get(channelKey);
            byte[] bytes = new byte[in.writerIndex() - in.readerIndex()];
            in.readBytes(bytes);
            if(lastBytes != null) {
                bytes = ArraysUtils.arraycopy(lastBytes, bytes);
            }
            ChannelPacket.getInstance().remove(channelKey);
            if(bytes.length < 25){
                ChannelPacket.getInstance().add(channelKey, bytes);
            }else{
                while(bytes.length > 2){
                    int head = Convert.byte2Int(ArraysUtils.subarrays(bytes, 0, 2), 2);
                    if(head == Constants.HEAD){
                        int len = Convert.byte2Int(ArraysUtils.subarrays(bytes, 22, 2), 2)+25;
                        if(bytes.length < len){
                            ChannelPacket.getInstance().add(channelKey, bytes);
                            break;
                        }
                        byte[] packet = ArraysUtils.subarrays(bytes, 0, len);
                        byte  crc = Convert.checkPackage(packet, 2, len-2);
                        if(crc != packet[len-1]){
                            LOGGER.info("crc check error ! 计算校验码和数据包校验码为：", crc, packet[len - 1]);
                            return;
                        }
                        LOGGER.info("packet : " + Convert.bytesToHexString(packet));
                        out.add(packet);
                        bytes = ArraysUtils.subarrays(bytes, len);
                    }else{
                        LOGGER.info("当前数据包消息头格式错误！", head);
                        bytes = ArraysUtils.subarrays(bytes, 1);
                    }
                    if(bytes.length < 25){
                        ChannelPacket.getInstance().add(channelKey, bytes);
                        break;
                    }
                }
            }
        }
    }
}
