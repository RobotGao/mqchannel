package com.robot.channel.worker.config;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;

@Configuration
@PropertySources({ @PropertySource("classpath:application.properties") })
@Order(1)
public class SpringConfig {
    private final static Logger logger = Logger.getLogger(SpringConfig.class);
    @PostConstruct
    public void initialize() {
        logger.info("Loading the config for channel worker");
    }



}
