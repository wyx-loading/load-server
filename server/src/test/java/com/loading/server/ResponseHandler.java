package com.loading.server;

import com.loading.server_rrimpl.common.ResponseProtocol;
import com.loading.server_rrimpl.common.ResponseProtocol.Response;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ResponseHandler extends SimpleChannelInboundHandler<ResponseProtocol.Response> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Response msg) throws Exception {
		System.out.println("received:\t" + msg.getParam());
	}

}
