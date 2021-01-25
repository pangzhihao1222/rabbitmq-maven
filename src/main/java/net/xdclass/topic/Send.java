package net.xdclass.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * 发布订阅 direct路由模式
 */
public class Send {

    private final static String EXCHANGE_NAME = "exchange_topic";

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("8.131.119.145");
        factory.setUsername("admin");
        factory.setPassword("password");
        factory.setVirtualHost("/dev");
        factory.setPort(5672);
        try(
            //创建连接
            Connection connection = factory.newConnection();
            //创建信道
            Channel channel = connection.createChannel()) {
            //绑定交换机，topic交换机
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            String error = "我是订单服务的error日志";
            String info = "我是订单服务的info日志";
            String debug = "我是订单服务的debug日志";
            channel.basicPublish(EXCHANGE_NAME,"order.log.error",null,error.getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(EXCHANGE_NAME,"order.log.info",null,info.getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(EXCHANGE_NAME,"product.log.debug",null,debug.getBytes(StandardCharsets.UTF_8));
            System.out.println("direct消息发送成功");
        }
    }
}
