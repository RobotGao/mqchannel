package com.robot.channel.worker.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQProducer implements ProducerService {
	@Autowired
	private RabbitTemplate template;

	public void sendMessage(String topic, String data) {
		template.convertAndSend(topic, data);
		System.out.println(topic + ":" + data);
	}

}
