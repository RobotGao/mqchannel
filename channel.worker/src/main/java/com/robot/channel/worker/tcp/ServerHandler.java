package com.robot.channel.worker.tcp;

import com.robot.channel.worker.utils.AESUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.robot.channel.worker.rabbitmq.ProducerService;
import com.robot.channel.worker.threading.WorkQueue;
import com.robot.channel.worker.utils.Constants;
import com.robot.channel.worker.utils.DataConvertor;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

@Component
public class ServerHandler implements ChannelInboundHandler {
	private final static Logger logger = Logger.getLogger(ServerHandler.class);
	@Autowired
	private ProducerService sender;
	@Autowired
	private WorkQueue workQueue;

	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			Runnable r = new Runnable() {
				public void run() {
					try {
						ByteBuf byteresult = (ByteBuf) msg;
						byte[] result = new byte[byteresult.readableBytes()];
						byteresult.readBytes(result);
						byteresult.release();
						logger.info("channelRead:" + DataConvertor.byteToHexString(result));
                        result = AESUtil.encrypt(result,Constants.SECURITY_KEY);
						sender.sendMessage(Constants.MESSAGE_TOPIC_PUBLISH, DataConvertor.byteToHexString(result));
						Constants.channelMap.put(Constants.APP_TCP_CHANNEL, ctx.channel());
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
			};
			workQueue.execute(r);
		} catch (Exception e) {
			logger.error("ERROR:" + e.getMessage());
		}
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
		logger.info(e.getMessage());
		if (this == ctx.pipeline().last()) {
			logger.warn(
					"EXCEPTION, please implement " + getClass().getName() + ".exceptionCaught() for proper handling.");
		}
		ctx.fireExceptionCaught(e);
	}

	public void handlerAdded(ChannelHandlerContext arg0) throws Exception {
		logger.info("handlerAdded ");
	}

	public void handlerRemoved(ChannelHandlerContext arg0) throws Exception {
		logger.info("handlerRemoved ");
	}

	public void channelActive(ChannelHandlerContext arg0) throws Exception {
		logger.info("channelActive ");
	}

	public void channelInactive(ChannelHandlerContext arg0) throws Exception {

	}

	public void channelReadComplete(ChannelHandlerContext arg0) throws Exception {

	}

	public void channelRegistered(ChannelHandlerContext arg0) throws Exception {

	}

	public void channelUnregistered(ChannelHandlerContext arg0) throws Exception {

	}

	public void channelWritabilityChanged(ChannelHandlerContext arg0) throws Exception {

	}

	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

		if (evt instanceof IdleStateEvent) {

			IdleStateEvent event = (IdleStateEvent) evt;

			if (event.state().equals(IdleState.READER_IDLE)) {
				// 未进行读操作
				logger.info("READER_IDLE");
				// 超时关闭channel
				// ctx.close();

			} else if (event.state().equals(IdleState.WRITER_IDLE)) {
				logger.info("WRITER_IDLE");
			} else if (event.state().equals(IdleState.ALL_IDLE)) {
				// 未进行读写
				logger.info("ALL_IDLE");
				// 发送心跳消息

			}

		}
	}

}
