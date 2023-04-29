package com.example.setup;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;

@Component
@Slf4j
public class ActiveMqSetup implements ApplicationRunner {

    @Value("${app.active-mq.broker-url}")
    private String brokerUrl;

    @Value("${app.active-mq.user}")
    private String user;

    @Value("${app.active-mq.password}")
    private String password;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
        connectionFactory.setUserName(user);
        connectionFactory.setPassword(password);

        // Create a connection
        Connection connection = connectionFactory.createConnection();
        connection.start();

        // Create a session
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

        // Create a queue
        createQueue(session, "LMS.BALANCE.REPORT.941.RS");
        createQueue(session,"LMS.REQUEST.PAYMENT.MT101.RQ");
        createQueue(session,"LMS.ACK.OUTGOING.WIRE.MT103.RS");
        createQueue(session,"LMS.OUTGOING.WIRE.MT103.RQ");
        createQueue(session,"LMS.INCOMING.WIRE.MT103.RS");
        createQueue(session,"LMS.REQUEST.REPORT.MT920.RQ");


        // Close the session and connection
        session.close();
        connection.close();
    }

    private void createQueue(Session session, String queueName)
    {
        try {
            session.createQueue(queueName);
            log.info("Queue {} created", queueName);
        } catch (JMSException e) {
            log.info("Queue {} already exists", queueName);
        }

    }
}
