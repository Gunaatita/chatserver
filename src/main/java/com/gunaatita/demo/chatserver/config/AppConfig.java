package com.gunaatita.demo.chatserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;


@Configuration
@ComponentScan("com.gunaatita")
@PropertySource({ "classpath:app.properties" })
public class AppConfig {

	@Autowired
    private Environment        env;
	
	@Bean
	public NettyConfig nettyConfig(){
		NettyConfig config = new NettyConfig();
		config.setPoolSize(env.getProperty("server.poolsize", Integer.class));
		config.setPort(env.getProperty("server.port", Integer.class));
		return config;
	}
	
}
