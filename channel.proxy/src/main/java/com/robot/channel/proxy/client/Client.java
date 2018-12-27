package com.robot.channel.proxy.client;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.robot.channel.proxy.utils.Constants;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.codec.*;

@Component
public class Client {
    private final static Logger logger = Logger.getLogger(Client.class);
    private static Bootstrap b;
    private static EventLoopGroup group;
    private static Channel channel;
    private String host;
    private int port;
    @Autowired
    private ClientHandler handler;
    private boolean flag = false;
    private ChannelFuture f;
    public boolean connect(String host, int port) {
        try {
            logger.info("connecting...");
            Client client=this;
            this.host = host;
            this.port = port;
            group = new NioEventLoopGroup();
            b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new IdleStateHandler(3, 3, 3,TimeUnit.SECONDS));
                            handler.setClient(client);
                            pipeline.addLast(handler);
                            channel = pipeline.channel();
                            Constants.channelMap.put(Constants.APP_TCP_CHANNEL,channel);
                        }
                    });
            f = b.connect(new InetSocketAddress(host ,port));
            f.channel().closeFuture();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    public void reconnect(){
        connect(host, port);
    }
    @Scheduled(fixedRate = 500)
    public void execute() throws Exception {
        if (b != null && (channel == null || !channel.isOpen() || !channel.isActive())) {
            logger.info("channel inactive, reconnect");
            connect(host, port);
        }
    }

    public Channel getChannel() {
        return channel;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
