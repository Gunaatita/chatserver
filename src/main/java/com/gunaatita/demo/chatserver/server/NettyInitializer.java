package com.gunaatita.demo.chatserver.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import org.springframework.context.ApplicationContext;

public class NettyInitializer extends ChannelInitializer<SocketChannel> {

	private ApplicationContext applicationContext;
	private NettyChannelHandler channelHandler;

	public NettyInitializer(ApplicationContext ctx) {
		this.applicationContext = ctx;
		channelHandler = new NettyChannelHandler(applicationContext);
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new HttpServerCodec());
		pipeline.addLast(new HttpObjectAggregator(100 * 1024 * 1024));
		pipeline.addLast(channelHandler);
	}

}
