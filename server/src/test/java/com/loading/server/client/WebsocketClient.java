package com.loading.server.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import com.loading.server.channel.BinaryFrameWrapper;
import com.loading.server_rrimpl.common.RequestProtocol;
import com.loading.server_rrimpl.common.ResponseProtocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class WebsocketClient {
	
	static final String URL = "ws://127.0.0.1:9332/";
	
	public static void main(String[] args) throws Exception {
		URI uri = new URI(URL);
		
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			final WebSocketHandshakeHandler handshakeHandler = 
					new WebSocketHandshakeHandler(
							WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders()));
			
			Bootstrap b = new Bootstrap();
			b.group(group)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline p = ch.pipeline();
						p.addLast(
								new LoggingHandler(LogLevel.WARN),
								new HttpClientCodec(), 
								new HttpObjectAggregator(8192), 
								WebSocketClientCompressionHandler.INSTANCE, 
								handshakeHandler, 
								new ProtobufDecoder(ResponseProtocol.Response.getDefaultInstance()), 
								new BinaryFrameWrapper(),
								new ProtobufEncoder(),
								new ResponseHandler()
								);
					}
				});
			
			Channel ch = b.connect(uri.getHost(), uri.getPort()).sync().channel();
			handshakeHandler.handshakeFuture().sync();
			
			BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				String msg = console.readLine();
				if(msg == null) {
					break;
				} else if("bye".equals(msg.toLowerCase())) {
					ch.writeAndFlush(new CloseWebSocketFrame());
					ch.closeFuture().sync();
					break;
				} else if("ping".equals(msg.toLowerCase())) {
					WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[] { 8, 1, 8, 1 }));
					ch.writeAndFlush(frame);
				} else {
					ch.writeAndFlush(RequestProtocol.Request.newBuilder().setCmd(1).setTag(1).setValue(msg).build());
				}
			}
		} finally {
			group.shutdownGracefully();
		}
	}

}
