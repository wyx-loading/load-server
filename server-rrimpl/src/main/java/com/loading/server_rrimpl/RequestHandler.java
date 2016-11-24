package com.loading.server_rrimpl;

import com.loading.server_rrimpl.common.RequestProtocol;
import com.loading.server_rrimpl.common.ResponseProtocol;
import com.loading.server_rrimpl.common.RequestProtocol.Request;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RequestHandler extends SimpleChannelInboundHandler<RequestProtocol.Request> {
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("new channel " + ctx.channel().id() + " connected");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("new channel " + ctx.channel().id() + " disconnected");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Request msg) throws Exception {
		System.out.println("received [" + ctx.channel().id() + "]\t" + msg.getValue());
		ctx.channel().writeAndFlush(ResponseProtocol.Response.newBuilder()
				.setCmd(msg.getCmd()).setTag(msg.getTag()).setParam(msg.getValue().toUpperCase()).build());
	}

}
