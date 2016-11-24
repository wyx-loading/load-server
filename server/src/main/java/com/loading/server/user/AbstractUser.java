package com.loading.server.user;

import com.loading.server_rrimpl.common.ResponseProtocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

abstract public class AbstractUser {
	
	private Channel channel;
	
	void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	public ChannelFuture sendResponse(ResponseProtocol.ResponseOrBuilder resp) {
		return channel.writeAndFlush(resp);
	}
	
	abstract public long getAccountId();

}
