package com.lyp.example005;

import com.lyp.utils.ConnectionUtils;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer{

  public static void main(String[] args) throws IOException, TimeoutException{
    Connection connection = ConnectionUtils.getConnection();
    //创建会话通道
    final Channel channel = connection.createChannel();

    String tempQueue = "queue001";

    // 实现消息方法
    DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
          throws IOException{
        String exchange = envelope.getExchange();
        long deliveryTag = envelope.getDeliveryTag();
        String msg = new String(body, "utf-8");
        System.out.println("Consumer receive: " + msg);

        try{
          Thread.sleep(1000);
        }catch(InterruptedException e){
          e.printStackTrace();
        }
      }
    };

    // 监听队列
    /**
     * 1，队列名
     * 2，自动回复
     * 3，消费方法
     */
    boolean ack = true; // true是自动应答机制，即工作模式中的 轮询模式
    channel.basicConsume(tempQueue, ack, defaultConsumer);
  }
}