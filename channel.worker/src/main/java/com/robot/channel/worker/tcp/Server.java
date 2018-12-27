package com.robot.channel.worker.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Component
@PropertySources({ @PropertySource("classpath:application.properties") })
public class Server {
	private final static Logger logger = Logger.getLogger(Server.class);
	@Autowired
	@Qualifier("serverBootstrap")
	private ServerBootstrap serverBootstrap;

	@Autowired
	@Qualifier("tcpSocketAddress")
	private InetSocketAddress tcpSocketAddress;
	private Channel serverChannel;

	@PostConstruct
	public void init() {
		logger.info("server starting ...");
		try {
			serverChannel = serverBootstrap.bind(tcpSocketAddress).sync().channel();
			serverChannel.closeFuture();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	@PreDestroy
	public void stop() {
		serverChannel.close();
	}

}
