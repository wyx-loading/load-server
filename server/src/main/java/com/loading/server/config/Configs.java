package com.loading.server.config;

import com.loading.xmlparser.ConfigWrapper;

public class Configs {

	private static class SingletonHolder {
		static Configs instance = new Configs();
	}
	
	public static Configs instance() {
		return SingletonHolder.instance;
	}
	
	private ServerConfig serverConfig;

	public void load() {
		String path = FixConfig.CONFIG_PATH + "serverConfig.xml";
		
		serverConfig = new ConfigWrapper<ServerConfig>(ServerConfig.class).load(path);
	}

	public void reload(String... args) {
		// TODO
	}
	
	public ServerConfig serverConfig() {
		return serverConfig;
	}

}
