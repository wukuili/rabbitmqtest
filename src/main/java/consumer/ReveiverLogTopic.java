package consumer;

import com.rabbitmq.client.*;
import factory.TjMqChannelFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: liyj
 * @Date:Created in 2018/5/14
 */
public class ReveiverLogTopic {
    public static final String TOPIC_EXCHANGE="topic_logs";
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel=TjMqChannelFactory.getChannel();
        //绑定exchange
        channel.exchangeDeclare(TOPIC_EXCHANGE,BuiltinExchangeType.TOPIC);
        //创建随机queue
        String queueName=channel.queueDeclare().getQueue();
        String bindKey="kern.#";
        //绑定queue与exchange
        channel.queueBind(queueName,TOPIC_EXCHANGE,bindKey);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        //创建consumer
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
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };
        channel.basicConsume(queueName,true,consumer);

    }
}
