package com.loading.server;

import java.net.InetSocketAddress;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class IpFloodHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
		String ip = socketAddress.getAddress().getHostAddress();
		if(!IpFloodChecker.getInstance().addIp(ip)) {
			ctx.close();
		} else {
			super.channelActive(ctx);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		InetSocketAddress inAddress = (InetSocketAddress) ctx.channel().remoteAddress();
		String ip = inAddress.getAddress().getHostAddress();
		IpFloodChecker.getInstance().decreaseIpCount(ip);
		super.channelInactive(ctx);
	}

}
