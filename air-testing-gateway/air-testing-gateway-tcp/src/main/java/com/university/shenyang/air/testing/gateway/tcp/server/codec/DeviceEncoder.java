package com.university.shenyang.air.testing.gateway.tcp.server.codec;

import com.university.shenyang.air.testing.gateway.common.kit.Convert;
import com.university.shenyang.air.testing.gateway.common.kit.Packet;
import com.university.shenyang.air.testing.gateway.common.kit.lang.ArraysUtils;
import com.university.shenyang.air.testing.gateway.util.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * Created by chenjc on 2017/05/03.
 * Packet 到Encoder之前要完成code码补全17位
 */
public class DeviceEncoder extends MessageToByteEncoder<Packet> {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(DeviceEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) throws Exception {
        int length = packet.getContent().length;
        byte[] bytes = new byte[length + 25];
        ArraysUtils.arrayappend(bytes, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(bytes, 2, Convert.intTobytes(packet.getCommandId(), 1));
        ArraysUtils.arrayappend(bytes, 3, Convert.intTobytes(packet.getAnswerId(), 1));
        if (packet.getUniqueMark().getBytes("ASCII").length <= 17){
            ArraysUtils.arrayappend(bytes, 4, ArraysUtils.fillArrayTail(packet.getUniqueMark().getBytes("ASCII"), 17, (byte) 0x00));
//            ArraysUtils.arrayappend(bytes, 4, packet.getUniqueMark().getBytes("ASCII"));
        }else{
            LOGGER.info("设备code码错误", packet.getUniqueMark());
            return ;
        }
        ArraysUtils.arrayappend(bytes, 21, Convert.intTobytes(packet.getEncrypt(), 1));
        ArraysUtils.arrayappend(bytes, 22, Convert.intTobytes(length, 2));
        ArraysUtils.arrayappend(bytes, 24, packet.getContent());
        byte crc = Convert.checkPackage(bytes, 2, bytes.length-2);
        ArraysUtils.arrayappend(bytes, bytes.length - 1, new byte[]{crc});
        LOGGER.info(ctx.channel().toString() + "发送给终端的最终数据:" + Convert.bytesToHexString(bytes));
        out.writeBytes(bytes);
    }
}
