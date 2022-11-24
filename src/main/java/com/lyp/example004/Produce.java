package com.lyp.example004;

import com.lyp.utils.ConnectionUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * fanout 模式 就是把交换机（Exchange）里的消息发送给所有绑定该交换机的队列，忽略routingKey。
 * <p>
 * 在广播模式中，生产者声明交换机的名称和类型即可
 */
public class Produce{

  public static final String QUEUE = "helloWorld";

  public static void main(String[] args) throws IOException, TimeoutException, InterruptedException{

    Connection connection = ConnectionUtils.getConnection();

    //创建会话通道
    Channel channel = connection.createChannel();

    // 发送消息
    String message = "heaven";

    // 声明交换机名称与类型
    String exchangeName = "fanout-exchange";
    channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);

    // 指定路由key
    String routingKey = "";

    /**
     * 1，exchange: 交换机，如果不指定将使用mq默认交换机
     * 2，routingKey： 路由key，交换机根据路由key来将消息转发到指定的队列，如果不使用默认交换机，routingKey设置为队列名称
     * 3，props： 消息属性
     * 4，body： 消息内容
     */
    channel.basicPublish(exchangeName, routingKey, null, message.getBytes("utf-8"));
    System.out.println("produce send: " + message);
  }
}