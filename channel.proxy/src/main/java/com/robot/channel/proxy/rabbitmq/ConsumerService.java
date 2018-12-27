package com.robot.channel.proxy.rabbitmq;

import org.springframework.stereotype.Component;

@Component
public interface ConsumerService {
	 public void listen(String data) ;
}
