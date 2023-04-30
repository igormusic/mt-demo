package com.example.appmock.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;

@Configuration
public class JmsConfig {
    @Value("${app.activemq.broker-url}")
    private String activeMqBrokerUrl;

    public static final String LMS_BALANCE_REPORT_941_RS = "LMS.BALANCE.REPORT.941.RS";
    public static final String LMS_REQUEST_PAYMENT_MT101_RQ = "LMS.REQUEST.PAYMENT.MT101.RQ";
    public static final String LMS_ACK_OUTGOING_WIRE_MT103_RS = "LMS.ACK.OUTGOING.WIRE.MT103.RS";
    public static final String LMS_OUTGOING_WIRE_MT103_RQ = "LMS.OUTGOING.WIRE.MT103.RQ";
    public static final String LMS_INCOMING_WIRE_MT103_RS = "LMS.INCOMING.WIRE.MT103.RS";
    public static final String LMS_REQUEST_REPORT_MT920_RQ = "LMS.REQUEST.REPORT.MT920.RQ";

    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(activeMqBrokerUrl); // Replace with your ActiveMQ broker URL
        return activeMQConnectionFactory;
    }

    @Bean
    public Session session(ConnectionFactory connectionFactory) throws JMSException {
        Connection connection = connectionFactory.createConnection();
        connection.start();
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory, Session session) throws JMSException {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory());

        return jmsTemplate;
    }


}






