package com.robot.channel.proxy.config;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.robot.channel.proxy.utils.Constants;

@Configuration
@PropertySources({ @PropertySource("classpath:application.properties") })
public class RabbitMQConfig {
	private final static Logger logger = Logger.getLogger(RabbitMQConfig.class);
	@Value("${spring.rabbitmq.host}")
	private String host;
	@Value("${spring.rabbitmq.port}")
	private int port;
	@Value("${spring.rabbitmq.username}")
	private String username;
	@Value("${spring.rabbitmq.password}")
	private String password;
	@Value("${spring.rabbitmq.consumer}")
	private String consumer;
	@Value("${spring.rabbitmq.publish}")
	private String mqttPublishTopic;
    @Value("${app.security}")
	private String security;


	@PostConstruct
	public void initialize() {
		System.out.println("Loading the config for RabbitMQ");
		logger.info("publish:" + mqttPublishTopic);
		logger.info("consumer:" + consumer);
		Constants.MESSAGE_TOPIC_PUBLISH = mqttPublishTopic;
		Constants.SECURITY_KEY = security;
	}

	@Bean
	public RabbitAdmin admin() {
		RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());
		rabbitAdmin.afterPropertiesSet();
		return rabbitAdmin;
	}

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
		connectionFactory.setPort(port);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		return connectionFactory;
	}

	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		return template;
	}

	@Bean(name = "queueMessage")
	public Queue queueMessage() {
		Queue q = new Queue(consumer);
		q.setAdminsThatShouldDeclare(admin());
		return q;
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("amq.topic");
	}

	@Bean
	Binding bindingExchangeMessages(@Qualifier("queueMessage") Queue queueMessages, TopicExchange exchange) {
		Binding binding = BindingBuilder.bind(queueMessages).to(exchange).with(consumer);
		binding.setAdminsThatShouldDeclare(admin());
		return binding;
	}
}
