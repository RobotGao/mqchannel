package com.robot.channel.proxy.config;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.annotation.Order;

import com.robot.channel.proxy.utils.Constants;

@Configuration
@PropertySources({ @PropertySource("classpath:application.properties") })
@Order(1)
public class SpringConfig {
    private final static Logger logger = Logger.getLogger(SpringConfig.class);
    @Value("${client.host}")
    private String host;
    @Value("${client.port}")
    private int port;
   


    @PostConstruct
    public void initialize() {
        logger.info("Loading the config for channel proxy");
        Constants.CLIENT_HOST = host;
        Constants.CLIENT_PORT = port;
    }



}
