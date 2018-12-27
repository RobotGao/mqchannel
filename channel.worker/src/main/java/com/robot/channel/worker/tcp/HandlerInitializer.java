package com.robot.channel.worker.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Qualifier
public class HandlerInitializer extends ChannelInitializer<SocketChannel> {
	private final static Logger logger = Logger.getLogger(HandlerInitializer.class);
	@Autowired
	private ServerHandler serverHandler;
	
	protected void initChannel(SocketChannel sc) throws Exception {
		ChannelPipeline pipeline = sc.pipeline();
		pipeline.addLast(new IdleStateHandler(3, 3, 0,TimeUnit.SECONDS));
		pipeline.addLast("ServerHandler",serverHandler);
		logger.info("initChannel");
	}
}
