package com.lyp.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionUtils{

  public static Connection getConnection(){
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setHost("127.0.0.1");
    connectionFactory.setPort(5672);
    connectionFactory.setUsername("guest");
    connectionFactory.setPassword("guest");

    // 设置虚拟机，一个mq服务可以设置多个虚拟机，每个虚拟机相当于一个独立的mq
    connectionFactory.setVirtualHost("/");

    try{
      return connectionFactory.newConnection();
    }catch(IOException e){
      e.printStackTrace();
    }catch(TimeoutException e){
      e.printStackTrace();
    }

    return null;
  }
}