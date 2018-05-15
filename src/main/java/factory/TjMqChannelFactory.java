package factory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import utils.PropertiesLoader;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: liyj
 * @Date:Created in 2018/5/14
 */
public class TjMqChannelFactory {
    private static Channel channel;
    private static Connection connection;

    public static Channel getChannel() throws IOException, TimeoutException {
        if (channel == null) {

            Connection connection = getConnection();
            channel = connection.createChannel();
        }

        return channel;
    }

    public static Connection getConnection() throws IOException, TimeoutException {
        if (connection==null){

            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.load(PropertiesLoader.newInstance().getPros("rabbit.properties"));
            connection=connectionFactory.newConnection();
        }
        return connection;
    }
}
