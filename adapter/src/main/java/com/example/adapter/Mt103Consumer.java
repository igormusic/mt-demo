package com.example.adapter;

import com.example.adapter.model.MessageStatus;
import com.example.adapter.model.MessageTracker;
import com.example.adapter.model.MessageTrackerRepository;
import com.prowidesoftware.swift.model.field.Field21;
import com.prowidesoftware.swift.model.mt.mt1xx.MT101;
import com.prowidesoftware.swift.model.mt.mt1xx.MT103;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.example.adapter.configuration.JmsConfig.*;


@Component
@Slf4j
public class Mt103Consumer {

    private final JmsTemplate ibmMqJmsTemplate;
    private final MessageTrackerRepository messageTrackerRepository;

    public Mt103Consumer(JmsTemplate ibmMqJmsTemplate, MessageTrackerRepository messageTrackerRepository) {
        this.ibmMqJmsTemplate = ibmMqJmsTemplate;
        this.messageTrackerRepository = messageTrackerRepository;
    }

    @JmsListener(destination = LMS_OUTGOING_WIRE_MT103_RQ)
    public void receiveMessage(String message) {

        var messageTracker = new MessageTracker();
        messageTracker.setMessageType("MT103");
        messageTracker.setRequest(message);
        messageTracker.setRequestDate(LocalDateTime.now());

        try {
            MT103 mtIn = MT103.parse(message);

            messageTracker.setSenderReference(mtIn.getField20().getValue());

            MT103 mtOut = new MT103();

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

    private void copyMessage(MT103 mtIn, MT103 mtOut) {
        // copy the sender and receiver from the incoming message
        mtOut.setSender(mtIn.getSender());
        mtOut.setReceiver(mtIn.getReceiver());

        // copy the fields from the incoming message
        // you can also add additional fields
        // or change existing fields if applicable
        for (var field : mtIn.getFields()) mtOut.addField(field);

        ibmMqJmsTemplate.send(SWIFT_OUTGOING_WIRE_MT103_RQ, session -> session.createTextMessage(mtOut.message()));
    }
}
