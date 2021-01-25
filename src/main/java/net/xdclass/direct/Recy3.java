package net.xdclass.direct;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Recy3 {

    private final static String EXCHANGE_NAME = "exchange_direct";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("8.131.119.145");
        factory.setUsername("admin");
        factory.setPassword("password");
        factory.setVirtualHost("/dev");
        factory.setPort(5672);

        //消费者一般不增加自动关闭
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //绑定交换机，fanout扇形，即广播
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //获取队列
        String queueName = channel.queueDeclare().getQueue();
        //绑定交换机和队列，direct交换机需要指定交换机和routingkey
        channel.queueBind(queueName,EXCHANGE_NAME,"errorRoutingKey");
        //回调方法，下面两种都行
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println("body="+new String(body,"utf-8"));

                //手工确认消息，不是多条确认,消费一个确认一个
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        //消费，关闭消息自动确认，重要
        channel.basicConsume(queueName,false,consumer);

    }
}
