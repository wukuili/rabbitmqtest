package producer;

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
public class NewTask {
    private final static String QUEUE_NAME = "GAOSU";
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory=new ConnectionFactory();
        factory.load(PropertiesLoader.newInstance().getPros("rabbit.properties"));
        //创建连接
        Connection connection=factory.newConnection();
        //创建频道 每个线程应该有不同的频道
        Channel channel=connection.createChannel();
        //声明一个queue
        channel.queueDeclare(QUEUE_NAME,false,false,true,null);
        //从命令行获取消息
        String message=getMessage(args);
        //发布消息
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes("UTF-8"));
        System.out.println("成功发送消息"+message);
        channel.close();
        connection.close();

    }

    private static String getMessage(String[] args) {
        if (args.length<1){
            return "hello world";
        }
        return jsonString(args,"");
    }

    private static String jsonString(String[] args, String delimiter) {
        int length=args.length;
        if (length==0) return "";
        StringBuilder words=new StringBuilder(args[0]);
        for (int i = 0; i <length ; i++) {
            words.append(delimiter).append(args[i]);
        }
        return words.toString();
    }
}
