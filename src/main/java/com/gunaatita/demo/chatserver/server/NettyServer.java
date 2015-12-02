package com.gunaatita.demo.chatserver.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.gunaatita.demo.chatserver.config.AppConfig;
import com.gunaatita.demo.chatserver.config.NettyConfig;

public class NettyServer {
	
	public void init() throws Exception {
		ApplicationContext springContext = getSpringContext();
		
		int port = 8181;
		int poolSize = 100;
		NettyConfig nettyConfig = springContext.getBean(NettyConfig.class);
		if(nettyConfig!=null){
			port = nettyConfig.getPort();
			poolSize = nettyConfig.getPoolSize();
		}
        EventLoopGroup bossGroup = new NioEventLoopGroup(); 
        EventLoopGroup workerGroup = new NioEventLoopGroup(poolSize);
        try {
            ServerBootstrap b = new ServerBootstrap(); 
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class) 
             .childHandler(new NettyInitializer(springContext))
             .option(ChannelOption.SO_BACKLOG, poolSize)
             .option(ChannelOption.SO_REUSEADDR, true)
             .childOption(ChannelOption.SO_KEEPALIVE, true); 

            ChannelFuture f = b.bind(port).sync(); 
            System.out.println("server started");
            
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
	
	private ApplicationContext getSpringContext(){
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		return context;
	}

	public static void main(String[] args) {
		try {
			new NettyServer().init();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
