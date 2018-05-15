package consumer;

import com.rabbitmq.client.*;
import utils.PropertiesLoader;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * @author: liyj
 * @Date:Created in 2018/5/14
 */
public class ReceiveLogs {
    private static final String EXCHANGE_NAME="logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory=new ConnectionFactory();
        factory.load(PropertiesLoader.newInstance().getPros("rabbit.properties"));
        //创建连接
        Connection connection=factory.newConnection();
        //创建频道
        Channel channel=connection.createChannel();
        //声明exchange
        channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.FANOUT);
        //获取随机queue
        String queueName=channel.queueDeclare().getQueue();
        //将queue和exchange绑定
        channel.queueBind(queueName,EXCHANGE_NAME,"");
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        Consumer consumer=new DefaultConsumer(channel){
            /**
             * No-op implementation of {@link Consumer#handleDelivery}.
             *
             * @param consumerTag
             * @param envelope
             * @param properties
             * @param body
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                String message=new String(body,"UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(queueName,true,consumer);
    }
}
