package com.example.adapter;

import com.example.adapter.model.MessageStatus;
import com.example.adapter.model.MessageTracker;
import com.example.adapter.model.MessageTrackerRepository;
import com.prowidesoftware.swift.model.field.Field12;
import com.prowidesoftware.swift.model.mt.mt9xx.MT920;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.example.adapter.configuration.JmsConfig.LMS_REQUEST_REPORT_MT920_RQ;
import static com.example.adapter.configuration.JmsConfig.SWIFT_REQUEST_REPORT_MT920_RQ;


@Component
@Slf4j
public class Mt920Consumer {


    private final JmsTemplate ibmMqJmsTemplate;
    private final MessageTrackerRepository messageTrackerRepository;

    public Mt920Consumer(JmsTemplate ibmMqJmsTemplate, MessageTrackerRepository messageTrackerRepository) {
        this.ibmMqJmsTemplate = ibmMqJmsTemplate;
        this.messageTrackerRepository = messageTrackerRepository;
    }

    @JmsListener(destination = LMS_REQUEST_REPORT_MT920_RQ)
    public void receiveMessage(String message) {

        var messageTracker = new MessageTracker();
        messageTracker.setMessageType("MT920");
        messageTracker.setRequest(message);
        messageTracker.setRequestDate(LocalDateTime.now());

        try {
            MT920 mtIn = MT920.parse(message);

            messageTracker.setTransactionReferenceNumber(mtIn.getField20().getValue());

            MT920 mtOut = new MT920();

            copyMessage(mtIn, mtOut);
            messageTracker.setStatus(MessageStatus.REQUEST_SENT);
        } catch (Exception e) {
            log.error("Error parsing message: " + e.getMessage());
            messageTracker.setStatus(MessageStatus.REQUEST_ERROR);
            messageTracker.setErrorMessage(e.getMessage());
        }

        try {
            messageTrackerRepository.save(messageTracker);
            log.info("Message tracker saved: {}", message);
        } catch (Exception e) {
            log.error("Error saving message tracker: " + e.getMessage());
        }
    }

    private void copyMessage(MT920 mtIn, MT920 mtOut) {
        // copy the sender and receiver from the incoming message
        mtOut.setSender(mtIn.getSender());
        mtOut.setReceiver(mtIn.getReceiver());

        for (var field : mtIn.getFields()) {
            // swap the value of field 12
            if (field.getName().equals("12")) {
                mtOut.addField(new Field12("942"));
            } else {
                mtOut.addField(field);
            }
        }

        ibmMqJmsTemplate.send(SWIFT_REQUEST_REPORT_MT920_RQ, session -> session.createTextMessage(mtOut.message()));
    }

}
