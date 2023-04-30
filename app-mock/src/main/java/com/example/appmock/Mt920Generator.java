package com.example.appmock;

import com.example.appmock.model.CustomerRepository;
import com.prowidesoftware.swift.model.field.Field12;
import com.prowidesoftware.swift.model.field.Field20;
import com.prowidesoftware.swift.model.field.Field25;
import com.prowidesoftware.swift.model.mt.mt9xx.MT920;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static com.example.appmock.config.JmsConfig.LMS_REQUEST_REPORT_MT920_RQ;

@Component
@Slf4j
public class Mt920Generator extends MessageGenerator {

    private final CustomerRepository customerRepository;
    private final JmsTemplate jmsTemplate;


    public Mt920Generator(CustomerRepository customerRepository, JmsTemplate jmsTemplate) {
        this.customerRepository = customerRepository;
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    void createMessage() {
        MT920 mt = new MT920();

        var customer = customerRepository.findRandomRecord();
        var accounts = customer.getAccounts();
        var randomAccount = Utility.getRandomRecord(accounts);

        mt.setSender(this.senderBic);
        mt.setReceiver(randomAccount.getBic());
        mt.addField(new Field20(Utility.generateReferenceNumber()));
        mt.addField(new Field12("941"));

        for (var account: accounts) {
            mt.addField(new Field25(account.getAccountNumber()));
        }

        String message = mt.message();

        jmsTemplate.convertAndSend(LMS_REQUEST_REPORT_MT920_RQ, message);

        log.info(message);
    }


}
