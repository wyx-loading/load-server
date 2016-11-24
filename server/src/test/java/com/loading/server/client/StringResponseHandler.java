package com.loading.server.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class StringResponseHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
		if(frame instanceof TextWebSocketFrame) {
			TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
			System.out.println("received:\t" + textFrame.text());
		} else {
			System.out.println("other");
		}
		frame.release();
	}

}
