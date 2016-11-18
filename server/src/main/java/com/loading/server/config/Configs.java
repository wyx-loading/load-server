package com.loading.server.config;

import com.loading.xmlparser.ConfigWrapper;

public class Configs {

	private static class SingletonHolder {
		static Configs instance = new Configs();
	}
	
	public static Configs instance() {
		return SingletonHolder.instance;
	}
	
	private final ConfigWrapper<ServerConfig> serverConfig = new ConfigWrapper<>(ServerConfig.class);
	
	public void load() {
		serverConfig.load("serverConfig.xml");
	}

	public void reload(String... args) {
		// TODO
	}
	
	public ServerConfig serverConfig() {
		return serverConfig.get();
	}

}
