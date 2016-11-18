package com.loading.server_rrimpl;

import com.loading.server_rrimpl.common.RequestProtocol;
import com.loading.server_rrimpl.common.RequestProtocol.Request;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RequestHandler extends SimpleChannelInboundHandler<RequestProtocol.Request> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Request msg) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
