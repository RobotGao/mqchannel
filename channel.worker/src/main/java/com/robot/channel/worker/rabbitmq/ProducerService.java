package com.robot.channel.worker.rabbitmq;

public interface ProducerService {
	public void sendMessage(String topic, String data);
}
