package io.github.quickmsg.core.mqtt;

import io.github.quickmsg.core.metric.MetricManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

/**
 * @author luxurong
 */
public class MetricChannelHandler extends ChannelDuplexHandler {


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ByteBuf) {
            ByteBuf buffer = (ByteBuf) msg;
            if (buffer.readableBytes() > 0) {
                MetricManager.recordDataSend(buffer.readableBytes());
            }
        }
        super.write(ctx, msg, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            ByteBuf buffer = (ByteBuf) msg;
            if (buffer.readableBytes() > 0) {
                MetricManager.recordDataReceived(buffer.readableBytes());
            }
        }
        super.channelRead(ctx, msg);
    }



}
