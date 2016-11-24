package com.loading.server;

import com.loading.server.config.Configs;

public class ServerStarter {
	
	public void run() throws Exception {
		Configs.instance().load();
		
		SocketStarter socket = new SocketStarter();
		socket.run();
	}
	
	public static void main(String[] args) throws Exception {
		new ServerStarter().run();
	}

}
