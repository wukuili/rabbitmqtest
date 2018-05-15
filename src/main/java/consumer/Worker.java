package consumer;

import com.rabbitmq.client.*;
import utils.PropertiesLoader;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

/**
 * @author: liyj
 * @Date:Created in 2018/5/14
 */
public class Worker {
    private final static String QUEUE_NAME = "GAOSU";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Properties properties=PropertiesLoader.newInstance().getPros("rabbit.properties");
        factory.load(properties);
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, true, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                try {
                    doWork(message);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    System.out.println("任务完成");
                    //防止任务被停止的时候  消息没有被消费完导致消息丢失
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };
        channel.basicConsume(QUEUE_NAME, false, consumer);
    }

    private static void doWork(String message) throws InterruptedException {
        for (char ch:message.toCharArray()){
            if ('.'==ch){
                Thread.sleep(1000);
            }
        }
    }
}
