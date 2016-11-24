package com.loading.server.channel;

import com.loading.server.config.Configs;
import com.loading.server_rrimpl.EventExecutorGroupImpl;
import com.loading.server_rrimpl.RequestHandler;
import com.loading.server_rrimpl.common.RequestProtocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class SocketStarter {
	
	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast("logging", new LoggingHandler(LogLevel.WARN));

						// IpFloodCheck
//						ch.pipeline().addLast("ipFloodChecker", new IpFloodHandler());
						
						// Websocket Pipeline
						ch.pipeline().addLast("codec", new HttpServerCodec());
						ch.pipeline().addLast("aggregator", new HttpObjectAggregator(64 * 1024));
						ch.pipeline().addLast("wsCompression", new WebSocketServerCompressionHandler());
						ch.pipeline().addLast("wsProtocol", new WebSocketServerProtocolHandler("/", null, true));
						ch.pipeline().addLast("wsBinFrame", new BinaryFrameConverter());
						ch.pipeline().addLast("binaryWrapper", new BinaryFrameWrapper());
						ch.pipeline().addLast("protobufDecoder", new ProtobufDecoder(RequestProtocol.Request.getDefaultInstance()));
						ch.pipeline().addLast("protobufEncoder", new ProtobufEncoder());
						
						ch.pipeline().addLast(EventExecutorGroupImpl.getLogicThreadGroup(), "requestHandler", new RequestHandler());
					}
				})
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			// Bind and start to accept incoming connections.
			Channel ch = b.bind(
//					Configs.instance().serverConfig().getSocketHost(), 
					Configs.instance().serverConfig().getSocketPort()).sync().channel();
			
			System.out.println("Server started!");
			
			// Wait until the server socket is closed.
			// In this example, this does not happen, but you can do that to gracefully
			// shut down your server.
			ch.closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

}
