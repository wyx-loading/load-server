package com.loading.server.user;

import io.netty.channel.Channel;

public class EngineSideUser extends BaseUser {
	
	public EngineSideUser(Channel channel) {
		this.channel = channel;
	}

	@Override
	public long getAccountId() {
		return 0;
	}
	
}
