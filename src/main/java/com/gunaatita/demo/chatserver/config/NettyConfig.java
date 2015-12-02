package com.gunaatita.demo.chatserver.config;

public class NettyConfig {

	private int port;
	private int poolSize;
	
	public int getPoolSize() {
		return poolSize;
	}
	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
}
