package com.lyp.example002;

import com.lyp.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 工作队列 轮询分发
 * 工作模式不需要指定交换机，直接指定队列，一个队列对应多个消费者；
 *
 * 主要有两种模式：
 *
 * 轮询模式的分发：一个消费者一条，按均分配
 * 公平分发：根据消费者的消费能力进行公平分发，处理快的处理的多，处理慢的处理的少；按劳分配
 */
public class Produce{

  public static final String QUEUE = "helloWorld";

  public static void main(String[] args) throws IOException, TimeoutException, InterruptedException{

    Connection connection = ConnectionUtils.getConnection();

    //创建会话通道
    Channel channel = connection.createChannel();
    // 1，queue 队列名，
    // 2，durable 是否持久化，如果持久化，mq重启后队列还在，
    // 3，exclusive 是否独占连接，队列只允许在该连接中访问，如果 connection连接关闭队列则自动删除，如果 将此参数设置true，可用于临时队列的创建
    // 4，autoDelete 自动删除，队列不再使用时，是否自动删除此队列，如果将此参数和exclusive参数设置为true，就可以实现临时队列（不用就自动删除）
    // 5，arguments 参数，可以设置一些额外的参数，比如可以设置存活时间

    channel.queueDeclare(QUEUE, true, false, false, null);

    for(int i = 0; i < 20; i++){
      // 发磅消息
      String message = "heaven" + i;
      /**
       * 1，exchange: 交换机，如果不指定将使用mq默认交换机
       * 2，routingKey： 路由key，交换机根据路由key来将消息转发到指定的队列，如果不使用默认交换机，routingKey设置为队列名称
       * 3，props： 消息属性
       * 4，body： 消息内容
       */
      channel.basicPublish("", QUEUE, null, message.getBytes("utf-8"));
      System.out.println("produce send: " + message);
      Thread.sleep(i * 20);
    }
  }
}