package com.loading.server.ipflood;

import java.net.InetSocketAddress;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ipfilter.AbstractRemoteAddressFilter;

@ChannelHandler.Sharable
public class IpFloodFilter extends AbstractRemoteAddressFilter<InetSocketAddress> {
	
	@Override
	protected boolean accept(ChannelHandlerContext ctx, InetSocketAddress remoteAddress) throws Exception {
		final String ipAddress = remoteAddress.getAddress().getHostAddress();
		if(!IpFloodChecker.getInstance().addIp(ipAddress)) {
			return false;
		} else {
			ctx.channel().closeFuture().addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					IpFloodChecker.getInstance().decreaseIpCount(ipAddress);
				}
			});
			return true;
		}
	}

}
