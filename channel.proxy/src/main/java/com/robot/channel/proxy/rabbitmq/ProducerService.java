package com.robot.channel.proxy.rabbitmq;

public interface ProducerService {
	public void sendMessage(String topic, String data);
}
