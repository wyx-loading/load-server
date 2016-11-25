package com.loading.server.user;

import com.loading.server_rrimpl.common.ResponseProtocol.Response;

import io.netty.channel.ChannelFuture;

abstract public class AbstractExtendUser {
	
	private BaseUser baseUser;
	
	protected AbstractExtendUser(BaseUser baseUser) {
		this.baseUser = baseUser;
	}
	
	public ChannelFuture sendResponse(Response resp) {
		return baseUser.sendResponse(resp);
	}

}
