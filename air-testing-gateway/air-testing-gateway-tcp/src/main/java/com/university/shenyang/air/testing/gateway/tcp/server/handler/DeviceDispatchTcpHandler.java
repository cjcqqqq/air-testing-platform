package com.university.shenyang.air.testing.gateway.tcp.server.handler;

import com.university.shenyang.air.testing.gateway.common.kit.Convert;
import com.university.shenyang.air.testing.gateway.common.kit.Packet;
import com.university.shenyang.air.testing.gateway.common.kit.lang.ArraysUtils;
import com.university.shenyang.air.testing.gateway.cache.ChannelPacket;
import com.university.shenyang.air.testing.gateway.cache.DevicesManager;
import com.university.shenyang.air.testing.gateway.procotol.Command;
import com.university.shenyang.air.testing.gateway.procotol.ProtocolDispatcher;
import com.university.shenyang.air.testing.gateway.util.Constants;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 服务分发
 * Created by chenjc on 2017/05/03.
 */
@Component
@ChannelHandler.Sharable
public class DeviceDispatchTcpHandler extends ChannelInboundHandlerAdapter {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(DeviceDispatchTcpHandler.class);

    private static int COUNT_START = 0;

    private static int COUNT_END = 0;

    @Autowired
    private ProtocolDispatcher protocolDispatcher;

    /**
     * 先打开连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelRegistered();
        logger.info("channelRegistered {}", ctx.channel().remoteAddress().toString());
    }

    /**
     * 客户端连接后，再触发此方法
     *
     * @param ctx
     * @throws Exception
     */
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
        logger.info("channelActive {} {}", ctx.channel().remoteAddress().toString(), COUNT_START);
        COUNT_START++;
    }

    /**
     * 客户端主动断开连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 删除遗留半包
        ChannelPacket.getInstance().remove(ctx.channel().remoteAddress().toString());
        // 获取对应设备识别码
        String deviceCode = DevicesManager.getInstance().getDeviceCodeByRemoteAddress(ctx.channel().remoteAddress().toString());
        if(deviceCode != null && !"".equals(deviceCode)){
            // 删除设备识别码与设备远程地址与的映射缓存
            DevicesManager.getInstance().removeDeviceCodeMappingChannel(deviceCode);
            // 删除设备远程地址与设备识别码的映射缓存
            DevicesManager.getInstance().removeChannelMappingDeviceCode(ctx.channel().remoteAddress().toString());
        }

        ctx.fireChannelInactive();
        logger.info("channelInactive{} {}", ctx.channel().remoteAddress().toString(), COUNT_END);
        COUNT_END++;
    }

    /**
     * 客户端异常,断开连接后，再触发此方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        // 删除遗留半包
        ChannelPacket.getInstance().remove(ctx.channel().remoteAddress().toString());
        // 获取对应设备识别码
        String deviceCode = DevicesManager.getInstance().getDeviceCodeByRemoteAddress(ctx.channel().remoteAddress().toString());
        if(deviceCode != null && !"".equals(deviceCode)){
            // 删除设备识别码与设备远程地址与的映射缓存
            DevicesManager.getInstance().removeDeviceCodeMappingChannel(deviceCode);
            // 删除设备远程地址与设备识别码的映射缓存
            DevicesManager.getInstance().removeChannelMappingDeviceCode(ctx.channel().remoteAddress().toString());
        }
        ctx.fireExceptionCaught(cause);
        logger.info("[exceptionCaught] 终端异常断开连接,[{}],{}", ctx.channel().remoteAddress().toString(), cause.getMessage());
    }

    /**
     * 心跳设置
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if(event.state().equals(IdleState.READER_IDLE)){
                ctx.close();
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    /**
     * 服务端收到客户端发送过来的消息时，触发此方法，根据command派发指定handler
     *
     * @param channelHandlerContext
     * @param o
     * @throws Exception
     */
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        try {
            byte[] original = (byte[])o;
            Packet packet = new Packet();
            packet.setCommandId(Convert.byte2Int(ArraysUtils.subarrays(original, 2, 1), 1));
            packet.setAnswerId(Convert.byte2Int(ArraysUtils.subarrays(original, 3, 1), 1));
            packet.setUniqueMark(new String(ArraysUtils.subarrays(original, 4, 17), "ASCII").trim());
            //TODO 加载设备信息后，此处做校验，非法断开链路
            packet.setEncrypt(Convert.byte2Int(ArraysUtils.subarrays(original, 21, 1), 1));
            int len = Convert.byte2Int(ArraysUtils.subarrays(original, 22, 2), 2);
            packet.setContent(ArraysUtils.subarrays(original, 24, len));
//             TODO
//            DevicesManager.getInstance().addChannel(channelHandlerContext.channel().remoteAddress().toString(), channelHandlerContext);
           /* Command command = null;
            if(packet.getCommandIdForHex().equals("03")){
                command = this.protocolDispatcher.getHandler(Constants.PROTOCOL_GB + "_02");
            }else {
                command = this.protocolDispatcher.getHandler(Constants.PROTOCOL_GB + "_" + packet.getCommandIdForHex());
            }*/
            Command command = this.protocolDispatcher.getHandler(Constants.PROTOCOL_GB + "_" + packet.getCommandIdForHex());

            if (command != null) {
                command.processor(channelHandlerContext, packet);
//                Packet answer = command.processor(channelHandlerContext, packet);
//                if(answer != null){
//                    channelHandlerContext.writeAndFlush(answer);
//                }
            } else {
                logger.info("指令[ " + packet.getCommandIdForHex() + " ]未找到匹配的应答协议类 . ");
                channelHandlerContext.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
