package com.loading.server;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class BinaryWebsockFrameToByteBufDecoder extends MessageToMessageDecoder<WebSocketFrame> {

	@Override
	protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
		if(msg instanceof BinaryWebSocketFrame) {
			out.add(msg.content());
		} else {
			// 丢弃非二进制帧
			msg.release();
		}
	}

}
