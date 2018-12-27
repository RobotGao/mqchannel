package com.robot.channel.worker.rabbitmq;

import com.robot.channel.worker.utils.AESUtil;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

import com.robot.channel.worker.utils.Constants;
import com.robot.channel.worker.utils.DataConvertor;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

@Component
@PropertySources({ @PropertySource("classpath:application.properties") })
public class ConsumerServiceImpl implements ConsumerService {
	private final static Logger logger = Logger.getLogger(ConsumerServiceImpl.class);

	@RabbitListener(queues = "${spring.rabbitmq.consumer}")
	public void listen(String data) {
		try {
			byte[] input = DataConvertor.hexToBytes(data);
            input = AESUtil.decrypt(input, Constants.SECURITY_KEY);
			logger.info("messageArrived:" + data);
			logger.info("text:" + new String(input));
			Channel channel = Constants.channelMap.get(Constants.APP_TCP_CHANNEL);
			ByteBuf buf = channel.alloc().directBuffer();
			buf.writeBytes(input);
			channel.writeAndFlush(buf);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}

	}

}