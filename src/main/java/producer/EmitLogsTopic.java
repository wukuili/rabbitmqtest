package producer;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import consumer.ReveiverLogTopic;
import factory.TjMqChannelFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: liyj
 * @Date:Created in 2018/5/14
 */
public class EmitLogsTopic {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel=TjMqChannelFactory.getChannel();
        //声明exchange
        channel.exchangeDeclare(ReveiverLogTopic.TOPIC_EXCHANGE,BuiltinExchangeType.TOPIC);
        String routeKey="kern.haha.sasas";
        String message="测试topic";
        channel.basicPublish(ReveiverLogTopic.TOPIC_EXCHANGE,routeKey,null,message.getBytes());
        channel.close();
        TjMqChannelFactory.getConnection().close();
    }
}
