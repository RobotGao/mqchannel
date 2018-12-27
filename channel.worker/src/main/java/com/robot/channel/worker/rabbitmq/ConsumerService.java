package com.robot.channel.worker.rabbitmq;

import org.springframework.stereotype.Component;

@Component
public interface ConsumerService {
	 public void listen(String data) ;
}
