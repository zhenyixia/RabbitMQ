package com.lyp.example006;

import com.lyp.utils.ConnectionUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Topic 模式是direct模式上的一种叠加，增加了模糊路由RoutingKey的模式
 *
 * 简单的可以理解为就是模糊的路由key匹配模式
 *
 * “#” : 匹配一个或者多个 “*”：匹配一个
 */
public class Produce{

  public static final String QUEUE = "helloWorld";

  public static void main(String[] args) throws IOException, TimeoutException, InterruptedException{

    Connection connection = ConnectionUtils.getConnection();

    //创建会话通道
    Channel channel = connection.createChannel();

    // 声明交换机名称与类型
    String exchangeName = "topic-exchange";
    channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);

    String msg1 = "error.log";
    String routeKey1 = "error.log";
    channel.basicPublish(exchangeName, routeKey1, null, msg1.getBytes("utf-8"));

    String msg2 = "success.log";
    String routeKey2 = "success.log";
    channel.basicPublish(exchangeName, routeKey2, null, msg2.getBytes("utf-8"));

    String msg3 = "a.b.log";
    String routeKey3 = "a.b.log";
    channel.basicPublish(exchangeName, routeKey3, null, msg3.getBytes("utf-8"));

    // channel.close();
    // connection.close();
  }
}