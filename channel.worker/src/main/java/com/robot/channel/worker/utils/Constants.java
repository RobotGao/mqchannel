package com.robot.channel.worker.utils;

import io.netty.channel.Channel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Constants {
	public static String MESSAGE_TOPIC_PUBLISH="";
	public static String APPID="";
	public static String APP_TCP_CHANNEL="CHANNEL_";
	public static Map<String,Channel> channelMap = new ConcurrentHashMap<>();
	public static String SECURITY_KEY="";
}
