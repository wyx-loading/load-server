package com.loading.server.group;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class MyChannelGroup {
	
	public static final ChannelGroup TOTAL = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

}
