package com.example.adapter;

import com.example.adapter.model.*;
import com.prowidesoftware.swift.model.field.Field21;
import com.prowidesoftware.swift.model.mt.mt1xx.MT101;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.example.adapter.configuration.JmsConfig.*;


@Component
@Slf4j
public class Mt101Consumer {

    private final JmsTemplate ibmMqJmsTemplate;
    private final MessageTrackerRepository messageTrackerRepository;
    private final TransactionReferenceRepository transactionReferenceRepository;

    public Mt101Consumer(JmsTemplate ibmMqJmsTemplate, MessageTrackerRepository messageTrackerRepository, TransactionReferenceRepository transactionReferenceRepository) {
        this.ibmMqJmsTemplate = ibmMqJmsTemplate;
        this.messageTrackerRepository = messageTrackerRepository;
        this.transactionReferenceRepository = transactionReferenceRepository;
    }

    @JmsListener(destination = LMS_REQUEST_PAYMENT_MT101_RQ)
    public void receiveMessage(String message) {

        var messageTracker = new MessageTracker();
        messageTracker.setMessageType("MT101");
        messageTracker.setRequest(message);
        messageTracker.setRequestDate(LocalDateTime.now());

        MT101 mtIn = null;

        try {
            mtIn = MT101.parse(message);

            messageTracker.setSenderReference(mtIn.getField20().getValue());

            MT101 mtOut = new MT101();

            copyMessage(mtIn, mtOut);
            messageTracker.setStatus(MessageStatus.REQUEST_SENT);


        } catch (Exception e) {
            log.error("Error parsing message: " + e.getMessage());
            messageTracker.setStatus(MessageStatus.REQUEST_ERROR);
            messageTracker.setErrorMessage(e.getMessage());
        }

        try {
            messageTrackerRepository.save(messageTracker);

            saveTransactionReferences(messageTracker, mtIn);

            log.info("Message tracker saved: {}", message);
        } catch (Exception e) {
            log.error("Error saving message tracker: " + e.getMessage());
        }
    }

    private void saveTransactionReferences(MessageTracker messageTracker, MT101 mtIn) {
        if(mtIn == null)
            return;

        for(var field : mtIn.getFields()) {
            if (field instanceof Field21) {
                var field12 = (Field21) field;
                var transactionReference = new TransactionReference(field12.getValue(), messageTracker);
                transactionReferenceRepository.save(transactionReference);
            }
        }
    }

    private void copyMessage(MT101 mtIn, MT101 mtOut) {
        // copy the sender and receiver from the incoming message
        mtOut.setSender(mtIn.getSender());
        mtOut.setReceiver(mtIn.getReceiver());

        // copy the fields from the incoming message
        // you can also add additional fields
        // or change existing fields if applicable
        for (var field : mtIn.getFields()) mtOut.addField(field);

        ibmMqJmsTemplate.send(SWIFT_REQUEST_PAYMENT_MT101_RQ, session -> session.createTextMessage(mtOut.message()));
    }
}
