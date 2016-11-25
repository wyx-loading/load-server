package com.loading.server.channel;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

@ChannelHandler.Sharable
public class BinaryFrameWrapper extends MessageToMessageEncoder<ByteBuf> {
	
	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		out.add(new BinaryWebSocketFrame(msg.retain()));
	}

}
