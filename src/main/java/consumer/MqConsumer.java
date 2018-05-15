package consumer;

import com.rabbitmq.client.*;
import utils.PropertiesLoader;

import java.io.IOException;
import java.util.Properties;

/**
 * @author: liyj
 * @Date:Created in 2018/5/14
 */
public class MqConsumer {
    private final static String QUEUE_NAME = "GAOSU";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        Properties properties=PropertiesLoader.newInstance().getPros("rabbit.properties");
        factory.load(properties);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, true, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
