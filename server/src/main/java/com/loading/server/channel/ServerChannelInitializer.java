package com.loading.server.channel;

import com.loading.server.EventExecutorGroupImpl;
import com.loading.server.RequestHandler;
import com.loading.server.config.Configs;
import com.loading.server.ipflood.IpFloodFilter;
import com.loading.server_rrimpl.common.RequestProtocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class ServerChannelInitializer extends ChannelInitializer<Channel> {
	
	private static final LoggingHandler LOGGING_HANDLER = 
			new LoggingHandler(LogLevel.WARN);
	
	private static final BinaryFrameConverter WS_BINFRAME_CONVERTER = 
			new BinaryFrameConverter();
	private static final BinaryFrameWrapper WS_BINFRAME_WRAPPER = 
			new BinaryFrameWrapper();
	
	private static final IpFloodFilter IP_FLOOD_FILTER = 
			new IpFloodFilter();
	
	private static final ProtobufEncoder PROTOBUF_ENCODER = 
			new ProtobufEncoder();

	private static final ProtobufDecoder REQUEST_DECODER = 
			new ProtobufDecoder(RequestProtocol.Request.getDefaultInstance());
	
	private static final RequestHandler REQUEST_HANDLER = 
			new RequestHandler();

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		
//		p.addLast("logging", LOGGING_HANDLER);

		// IpFloodCheck
		p.addLast("ipFloodFilter", IP_FLOOD_FILTER);
		
		// Websocket Pipeline
		p.addLast("codec", new HttpServerCodec());
		p.addLast("aggregator", new HttpObjectAggregator(64 * 1024));
		p.addLast("wsCompression", new WebSocketServerCompressionHandler());
		p.addLast("wsProtocol", new WebSocketServerProtocolHandler("/", null, true));
		
		p.addLast("readTimeout", new ReadTimeoutHandler(Configs.instance().serverConfig().getCommonReadTimeoutSeconds()));

		p.addLast("wsBinFrame", WS_BINFRAME_CONVERTER);
		p.addLast("binaryWrapper", WS_BINFRAME_WRAPPER);

		p.addLast("protobufDecoder", REQUEST_DECODER);
		p.addLast("protobufEncoder", PROTOBUF_ENCODER);
		
		p.addLast(EventExecutorGroupImpl.getLogicThreadGroup(), "requestHandler", REQUEST_HANDLER);
	}

}
