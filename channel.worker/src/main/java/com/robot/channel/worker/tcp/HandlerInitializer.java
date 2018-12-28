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

//   private static final int READ_IDEL_TIME_OUT = 5;
//   private static final int WRITE_IDEL_TIME_OUT = 0;
//   private static final int ALL_IDEL_TIME_OUT = 0;

	
	protected void initChannel(SocketChannel sc) throws Exception {
		ChannelPipeline pipeline = sc.pipeline();
		//pipeline.addLast(new IdleStateHandler(READ_IDEL_TIME_OUT, WRITE_IDEL_TIME_OUT, ALL_IDEL_TIME_OUT,TimeUnit.SECONDS));
		pipeline.addLast("ServerHandler",serverHandler);
		logger.info("initChannel");
	}
}
