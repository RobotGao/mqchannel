package com.robot.channel.proxy.rabbitmq;



import com.robot.channel.proxy.utils.AESUtil;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

import com.robot.channel.proxy.client.Client;
import com.robot.channel.proxy.utils.Constants;
import com.robot.channel.proxy.utils.DataConvertor;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;


@Component
@PropertySources({ @PropertySource("classpath:application.properties") })
public class ConsumerServiceImpl implements ConsumerService {
    private final static Logger logger = Logger.getLogger(ConsumerServiceImpl.class);
    @Autowired
    private Client client;

    @RabbitListener(queues = "${spring.rabbitmq.consumer}")
    public void listen(String data) {
        
        try {
            byte[] input = DataConvertor.hexToBytes(data);
            input = AESUtil.decrypt(input, Constants.SECURITY_KEY);
            logger.info("message proxy");
            logger.info("messageArrived:" +data);
            logger.info("text:" +new String(input));
            try {
                if (!client.isFlag()) {
                    client.connect(Constants.CLIENT_HOST, Constants.CLIENT_PORT);
                    Thread.sleep(100);
                }
                messageArrived(input);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }


    public void messageArrived(byte[] data) {
        try {
            Channel channel = client.getChannel();
            logger.info("channel:"+channel);
            if(channel!=null){
                ByteBuf buf = channel.alloc().directBuffer();
                buf.writeBytes(data);
                channel.writeAndFlush(buf);
            }else{
                if (!client.isFlag()) {
                    client.connect(Constants.CLIENT_HOST, Constants.CLIENT_PORT);
                    Thread.sleep(100);
                    messageArrived(data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }





}