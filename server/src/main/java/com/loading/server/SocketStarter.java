package com.loading.server;

import com.loading.server.config.Configs;
import com.loading.server_rrimpl.EventExecutorGroupImpl;
import com.loading.server_rrimpl.RequestHandler;
import com.loading.server_rrimpl.common.RequestProtocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
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
						// IpFloodCheck
						ch.pipeline().addLast("ipFloodChecker", new IpFloodHandler());
						
						// Websocket Pipeline
						ch.pipeline().addLast("codec", new HttpServerCodec());
						ch.pipeline().addLast("aggregator", new HttpObjectAggregator(64 * 1024));
						ch.pipeline().addLast("wsCompression", new WebSocketServerCompressionHandler());
						ch.pipeline().addLast("wsProtocol", new WebSocketServerProtocolHandler("/"));
						ch.pipeline().addLast("wsBinFrame", new BinaryWebsockFrameToByteBufDecoder());
						ch.pipeline().addLast("protobufDecoder", new ProtobufDecoder(RequestProtocol.Request.getDefaultInstance()));
						ch.pipeline().addLast("protobufEncoder", new ProtobufEncoder());
						
						// Socket Pipeline
//						ch.pipeline().addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
//						ch.pipeline().addLast("protobufDecoder", new ProtobufDecoder(RequestProtocol.Request.getDefaultInstance()));
//						ch.pipeline().addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
//						ch.pipeline().addLast("protobufEncoder", new ProtobufEncoder());
						
						ch.pipeline().addLast(EventExecutorGroupImpl.getLogicThreadGroup(), "requestHandler", new RequestHandler());
					}
				})
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			// Bind and start to accept incoming connections.
			ChannelFuture f = b.bind(
					Configs.instance().serverConfig().getSocketHost(), 
					Configs.instance().serverConfig().getSockPort()).sync();
			
			// Wait until the server socket is closed.
			// In this example, this does not happen, but you can do that to gracefully
			// shut down your server.
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception {
		new SocketStarter().run();
	}

}
