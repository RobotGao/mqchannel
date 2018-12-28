package com.robot.channel.proxy.client;

import com.robot.channel.proxy.utils.AESUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.robot.channel.proxy.rabbitmq.ProducerService;
import com.robot.channel.proxy.utils.Constants;
import com.robot.channel.proxy.utils.DataConvertor;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

@Component
public class ClientHandler implements ChannelInboundHandler {
	private final static Logger logger = Logger.getLogger(ClientHandler.class);
	@Autowired
	private ProducerService sender;

	private Client client;

	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			ByteBuf byteresult = (ByteBuf) msg;
			byte[] result = new byte[byteresult.readableBytes()];
			byteresult.readBytes(result);
			byteresult.release();
            result = AESUtil.encrypt(result,Constants.SECURITY_KEY);
			logger.info("channelRead:"+ DataConvertor.byteToHexString(result));
			sender.sendMessage(Constants.MESSAGE_TOPIC_PUBLISH,DataConvertor.byteToHexString(result));
			Constants.channelMap.put(Constants.APP_TCP_CHANNEL,ctx.channel());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public void setClient(Client client) {
		this.client = client;
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
		this.client.setFlag(false);
		Constants.channelMap.get(Constants.APP_TCP_CHANNEL).closeFuture();
		Constants.channelMap.remove(Constants.APP_TCP_CHANNEL);
//		System.exit(0);
//		this.client.reconnect();
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
				//ctx.close();

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
