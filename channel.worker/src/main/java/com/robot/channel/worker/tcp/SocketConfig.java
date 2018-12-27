package com.robot.channel.worker.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;

@Configuration
@PropertySources({ @PropertySource("classpath:application.properties") })
@Order(2)
public class SocketConfig {
	private final static Logger logger = Logger.getLogger(SocketConfig.class);
	private static ApplicationContext staticApplicationContext;
	@Autowired
	private ApplicationContext context;
	@Autowired
	private HandlerInitializer handlerInitializer;
	@Value("${tcp.port}")
	private int TCP_PORT;

	public static ApplicationContext getApplicationContext() {
		return staticApplicationContext;
	}

	@PostConstruct
	public void initialize() {
		logger.info("Loading the config for Socket");
		staticApplicationContext = context;
	}

	@Bean(name = "serverBootstrap")
	public ServerBootstrap serverBootstrap() {
		ServerBootstrap b = new ServerBootstrap();
		b.group(serverGroup(), workGroup())
				.channel(NioServerSocketChannel.class)
				.childHandler(handlerInitializer);
		b.option(ChannelOption.SO_BACKLOG, 128);
		return b;
	}


	@Bean(name = "serverGroup", destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup serverGroup() {
		return new NioEventLoopGroup();
	}

	@Bean(name = "workGroup", destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup workGroup() {
		return new NioEventLoopGroup(
				Runtime.getRuntime().availableProcessors() * 8);
	}

	@Bean(name = "tcpSocketAddress")
	public InetSocketAddress serverAddress() {
		return new InetSocketAddress(TCP_PORT);
	}

}
