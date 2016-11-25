package com.loading.server.user;

import com.loading.server_rrimpl.common.ResponseProtocol.Response;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

abstract public class BaseUser {

	Channel channel;
	
	abstract public long getAccountId();
	
	public ChannelFuture sendResponse(Response resp) {
		return channel.writeAndFlush(resp);
	}
	
}
