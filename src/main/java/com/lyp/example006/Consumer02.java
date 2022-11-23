package com.lyp.example006;

import com.lyp.utils.ConnectionUtils;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer02{

  public static void main(String[] args) throws IOException, TimeoutException{
    Connection connection = ConnectionUtils.getConnection();
    //创建会话通道
    final Channel channel = connection.createChannel();
    String queue = "abcdef";
    channel.queueDeclare(queue,false,false,false,null);

    // 交换机绑定队列时携带路由
    String exchangeName = "topic-exchange";
    channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
    String routeKey = "*.log";
    channel.queueBind(queue,exchangeName,routeKey);

    // 接收到消息后的回调函数
    DeliverCallback deliverCallback = new DeliverCallback(){
      public void handle(String consumerTag, Delivery delivery) throws IOException{
        String msg = new String(delivery.getBody(), "utf-8");
        System.out.println("receive: " + msg);
      }
    };


    boolean ack = true; // true是自动应答机制，即工作模式中的 轮询模式
    channel.basicConsume(queue, ack, deliverCallback, new CancelCallback(){
      public void handle(String consumerTag){}
    });

  }


}