package com.lyp.example005;

import com.lyp.utils.ConnectionUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Direct模式是fanout模式上的一种叠加，增加了路由RoutingKey的模式。
 *
 * 生产者创建消息 -> 投递到交换机 -> 交换机通过路由key匹配队列 -> 投递到队列 ->消费者消费
 *
 * 在rabitmq管理台上创建交换机 direct-exchange,创建队列 queue001 queue002 queue003
 * 绑定交换机，分别对应路由：sms email weixin
 *
 *
 */
public class Produce{

  public static final String QUEUE = "helloWorld";

  public static void main(String[] args) throws IOException, TimeoutException, InterruptedException{

    Connection connection = ConnectionUtils.getConnection();

    //创建会话通道
    Channel channel = connection.createChannel();

    // 发送消息
    String message = "heaven";

    // 不用再声明交换机名称与类型，因为已经在管理台中创建了
    String exchangeName = "direct-exchange";

    // 指定路由key
    String routingKey = "sms";

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