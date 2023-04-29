package com.example.setup;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

import static com.ibm.msg.client.wmq.compat.base.internal.MQC.MQOO_INPUT_AS_Q_DEF;
import static com.ibm.msg.client.wmq.compat.base.internal.MQC.MQOO_OUTPUT;

@Component
@Slf4j
public class IbmMqSetup implements ApplicationRunner {

    @Value("${app.ibm-mq.hostname}")
    private String hostname;
    @Value("${app.ibm-mq.channel}")
    private String channel;
    @Value("${app.ibm-mq.port}")
    private int port;
    @Value("${app.ibm-mq.userid}")
    private String userid;
    @Value("${app.ibm-mq.password}")
    private String password;
    @Value("${app.ibm-mq.queue-manager}")
    private String queueManagerName;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        MQConnectionFactory connectionFactory = new com.ibm.mq.jms.MQConnectionFactory();
        connectionFactory.setHostName(hostname);
        connectionFactory.setChannel(channel);
        connectionFactory.setPort(port);
        connectionFactory.setTransportType(1);
        connectionFactory.setQueueManager(queueManagerName);
        connectionFactory.setStringProperty(WMQConstants.USERID, userid);
        connectionFactory.setStringProperty(WMQConstants.PASSWORD, password);
        Connection connection = connectionFactory.createConnection();
        var session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        createQueue(session,  "SWIFT.REQUEST.PAYMENT.MT101.RQ");
        createQueue(session,  "SWIFT.REQUEST.REPORT.MT920.RQ");
        createQueue(session,  "SWIFT.OUTGOING.WIRE.MT103.RQ");

        session.close();
        connection.close();

    }

    private static void createQueue(Session session, String queueName) {
        try {
            int openOptions = MQOO_OUTPUT | MQOO_INPUT_AS_Q_DEF;

            session.createQueue(queueName);

            log.info("Queue {} created", queueName);

        } catch (JMSException ex) {
            log.error("JMSException: " + ex.getMessage());
        }
    }
}
