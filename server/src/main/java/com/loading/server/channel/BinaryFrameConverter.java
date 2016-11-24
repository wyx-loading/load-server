package com.loading.server.channel;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

public class BinaryFrameConverter extends MessageToMessageDecoder<BinaryWebSocketFrame> {

	@Override
	protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame binFrame, List<Object> out) throws Exception {
		out.add(binFrame.content().retain());
	}

}
