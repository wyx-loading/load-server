package com.loading.server.group;

import java.util.Set;

import com.loading.server.user.AbstractExtendUser;
import com.loading.server.user.EngineSideUser;

import io.netty.channel.group.ChannelGroupFuture;

public interface UserGroup extends Set<EngineSideUser>, Comparable<UserGroup> {
	
	String name();
	
	AbstractExtendUser find(long accountId);
	
	ChannelGroupFuture writeAndFlush(Object message);
	
	@SuppressWarnings("rawtypes")
	ChannelGroupFuture writeAndFlush(Object message, UserMatcher matcher);
	
	ChannelGroupFuture disconnect();
	
	@SuppressWarnings("rawtypes")
	ChannelGroupFuture disconnect(UserMatcher matcher);
	
}
