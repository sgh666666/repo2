package com.xiaoshu.test;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

public class TestTopicProducer {
    @Test
    public void test01(){
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.135.129:61616");
            Connection connection = null;
            connection = connectionFactory.createConnection();
            //参数一代表是否启动事务，参数2代表消息确认模式
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //创建消息队列对象
            Topic topic = session.createTopic("topic");
            MessageProducer producer = session.createProducer(topic);
            TextMessage textMessage = session.createTextMessage("i am topic messiage");
            producer.send(textMessage);
            producer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
