package com.loading.server;

import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class EventExecutorGroupImpl {
	
	public static EventExecutorGroup getLogicThreadGroup() {
		return new DefaultEventExecutorGroup(10);
	}

}
