package com.lyp.example003;

import com.lyp.utils.ConnectionUtils;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer02{

  public static final String QUEUE = "helloWorld";

  public static void main(String[] args) throws IOException, TimeoutException{
    Connection connection = ConnectionUtils.getConnection();
    //创建会话通道
    final Channel channel = connection.createChannel();

    // 一次仅接受一条未经确认的消息
    channel.basicQos(1);

    // 1，queue 队列名，
    // 2，durable 是否持久化，如果持久化，mq重启后队列还在，
    // 3，exclusive 是否独占连接，队列只允许在该连接中访问，如果 connection连接关闭队列则自动删除，如果 将此参数设置true，可用于临时队列的创建
    // 4，autoDelete 自动删除，队列不再使用时，是否自动删除此队列，如果将此参数和exclusive参数设置为true，就可以实现临时队列（不用就自动删除）
    // 5，arguments 参数，可以设置一些额外的参数，比如可以设置存活时间

    channel.queueDeclare(QUEUE, true, false, false, null);

    // 实现消息方法
    DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
          throws IOException{
        String exchange = envelope.getExchange();
        long deliveryTag = envelope.getDeliveryTag();
        String msg = new String(body, "utf-8");
        System.out.println("Consumer02 receive: " + msg);
        // 手动回执消息
        channel.basicAck(deliveryTag, false);
        try{
          Thread.sleep(500);
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
    boolean ack = false; // true是自动应答机制，即工作模式中的 轮询模式
    channel.basicConsume(QUEUE, ack, defaultConsumer);

    //
    // channel.basicConsume(QUEUE,ack,defaultConsumer);
    // channel.basicAck();

  }
}