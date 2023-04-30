package com.example.adapter.configuration;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.BeanFactoryDestinationResolver;

import javax.jms.*;

@Configuration
public class JmsConfig {
    public static final String LMS_BALANCE_REPORT_941_RS = "LMS.BALANCE.REPORT.941.RS";
    public static final String LMS_REQUEST_PAYMENT_MT101_RQ = "LMS.REQUEST.PAYMENT.MT101.RQ";
    public static final String LMS_ACK_OUTGOING_WIRE_MT103_RS = "LMS.ACK.OUTGOING.WIRE.MT103.RS";
    public static final String LMS_OUTGOING_WIRE_MT103_RQ = "LMS.OUTGOING.WIRE.MT103.RQ";
    public static final String LMS_INCOMING_WIRE_MT103_RS = "LMS.INCOMING.WIRE.MT103.RS";
    public static final String LMS_REQUEST_REPORT_MT920_RQ = "LMS.REQUEST.REPORT.MT920.RQ";
    public static final String SWIFT_REQUEST_PAYMENT_MT101_RQ = "SWIFT.REQUEST.PAYMENT.MT101.RQ";
    public static final String SWIFT_REQUEST_REPORT_MT920_RQ = "SWIFT.REQUEST.REPORT.MT920.RQ";
    public static final String SWIFT_OUTGOING_WIRE_MT103_RQ = "SWIFT.OUTGOING.WIRE.MT103.RQ";
    @Value("${app.activemq.broker-url}")
    private String activeMqBrokerUrl;
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

    @Bean
    public JmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory activeMqConnectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(activeMqConnectionFactory);
        return factory;
    }

    @Bean
    public ConnectionFactory activeMqConnectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(activeMqBrokerUrl); // Replace with your ActiveMQ broker URL
        return activeMQConnectionFactory;
    }

    @Bean
    ConnectionFactory ibmMqConnectionFactory() throws JMSException {
        MQConnectionFactory connectionFactory = new com.ibm.mq.jms.MQConnectionFactory();
        connectionFactory.setHostName(hostname);
        connectionFactory.setChannel(channel);
        connectionFactory.setPort(port);
        connectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
        connectionFactory.setQueueManager(queueManagerName);
        connectionFactory.setStringProperty(WMQConstants.USERID, userid);
        connectionFactory.setStringProperty(WMQConstants.PASSWORD, password);
        return connectionFactory;
    }

    @Bean
    public Session activeMqSession(ConnectionFactory activeMqConnectionFactory) throws JMSException {
        Connection connection = activeMqConnectionFactory.createConnection();
        connection.start();
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    @Bean
    public Session ibmMqSession(ConnectionFactory ibmMqConnectionFactory) throws JMSException {
        Connection connection = ibmMqConnectionFactory.createConnection();
        connection.start();
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    @Bean
    public JmsTemplate activeMqJmsTemplate(ConnectionFactory activeMqConnectionFactory, Session activeMqSession) throws JMSException {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(activeMqConnectionFactory());
        jmsTemplate.setDestinationResolver(new BeanFactoryDestinationResolver());

        return jmsTemplate;
    }

    @Bean
    public JmsTemplate ibmMqJmsTemplate(ConnectionFactory ibmMqConnectionFactory, Session ibmMqSession) throws JMSException {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(ibmMqConnectionFactory);
        return jmsTemplate;
    }

   /* @Bean
    public JmsTemplate jmsTemplate920RqActiveMq(ConnectionFactory activeMqConnectionFactory, Session activeMqSession) throws JMSException {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(activeMqConnectionFactory());
        Destination destination = activeMqSession.createQueue(LMS_REQUEST_REPORT_MT920_RQ);
        jmsTemplate.setDefaultDestination(destination);

        return jmsTemplate;
    }

    @Bean
    public JmsTemplate jmsTemplate920RqIbmMq(ConnectionFactory ibmMqConnectionFactory, Session ibmMqSession) throws JMSException {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(ibmMqConnectionFactory);
        Destination destination = ibmMqSession.createQueue(SWIFT_REQUEST_REPORT_MT920_RQ);
        jmsTemplate.setDefaultDestination(destination);

        return jmsTemplate;
    }

    @Bean
    public JmsTemplate jmsTemplate103Rs(ConnectionFactory activeMqConnectionFactory, Session activeMqSession) throws JMSException {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(activeMqConnectionFactory);
        Destination destination = activeMqSession.createQueue(LMS_INCOMING_WIRE_MT103_RS);
        jmsTemplate.setDefaultDestination(destination);

        return jmsTemplate;
    }

    @Bean
    public JmsTemplate jmsTemplate103RqActiveMq(ConnectionFactory activeMqConnectionFactory, Session activeMqSession) throws JMSException {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(activeMqConnectionFactory);
        Destination destination = activeMqSession.createQueue(LMS_OUTGOING_WIRE_MT103_RQ);
        jmsTemplate.setDefaultDestination(destination);

        return jmsTemplate;
    }

    @Bean
    public JmsTemplate jmsTemplate103RqIbmMq(ConnectionFactory ibmMqConnectionFactory, Session ibmMqSession) throws JMSException {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(ibmMqConnectionFactory);
        Destination destination = ibmMqSession.createQueue(SWIFT_OUTGOING_WIRE_MT103_RQ);
        jmsTemplate.setDefaultDestination(destination);

        return jmsTemplate;
    }

    @Bean
    public JmsTemplate jmsTemplate101RqActiveMq(ConnectionFactory activeMqConnectionFactory, Session activeMqSession) throws JMSException {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(activeMqConnectionFactory);
        Destination destination = activeMqSession.createQueue(LMS_REQUEST_PAYMENT_MT101_RQ);
        jmsTemplate.setDefaultDestination(destination);

        return jmsTemplate;
    }

    @Bean
    public JmsTemplate jmsTemplate101RqIbmMq(ConnectionFactory ibmMqConnectionFactory, Session ibmMqSession) throws JMSException {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(ibmMqConnectionFactory);
        Destination destination = ibmMqSession.createQueue(SWIFT_REQUEST_PAYMENT_MT101_RQ);
        jmsTemplate.setDefaultDestination(destination);

        return jmsTemplate;
    }

    @Bean
    public JmsTemplate jmsTemplate941Rs(ConnectionFactory activeMqConnectionFactory, Session activeMqSession) throws JMSException {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(activeMqConnectionFactory);
        Destination destination = activeMqSession.createQueue(LMS_BALANCE_REPORT_941_RS);
        jmsTemplate.setDefaultDestination(destination);

        return jmsTemplate;
    }

    @Bean
    public JmsTemplate jmsTemplate103AckRs(ConnectionFactory activeMqConnectionFactory, Session activeMqSession) throws JMSException {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(activeMqConnectionFactory);
        Destination destination = activeMqSession.createQueue(LMS_ACK_OUTGOING_WIRE_MT103_RS);
        jmsTemplate.setDefaultDestination(destination);

        return jmsTemplate;
    }*/

}
