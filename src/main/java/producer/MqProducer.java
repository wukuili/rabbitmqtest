package producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.lang.time.DateUtils;
import utils.PropertiesLoader;
import utils.TjDateUtils;

import java.util.Properties;

/**
 * @author: liyj
 * @Date:Created in 2018/5/14
 */
public class MqProducer {
    private final static String QUEUE_NAME = "GAOSU";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        Properties properties = PropertiesLoader.newInstance().getPros("rabbit.properties");
        factory.load(properties);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, true, null);
        String message = TjDateUtils.getDate("yyyy-MM-dd HH:mm:ss") + "   Hello World!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + message + "'");
        channel.close();
        connection.close();
    }
}
