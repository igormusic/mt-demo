package com.example.appmock;

import com.example.appmock.model.Account;
import com.example.appmock.model.Customer;
import com.example.appmock.model.CustomerRepository;
import com.prowidesoftware.swift.model.field.*;
import com.prowidesoftware.swift.model.mt.mt1xx.MT101;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.appmock.config.JmsConfig.LMS_REQUEST_PAYMENT_MT101_RQ;

@Component
@Slf4j
public class Mt101Generator extends MessageGenerator {

    private final CustomerRepository customerRepository;
    private final JmsTemplate jmsTemplate;

    public Mt101Generator(CustomerRepository customerRepository, JmsTemplate jmsTemplate) {
        this.customerRepository = customerRepository;
        this.jmsTemplate = jmsTemplate;
    }

    private static void addPaymentInstruction(MT101 mt, Customer customer, Account account) {
        // transaction reference
        mt.addField(new Field21(Utility.generateReferenceNumber()));

        // instruction codes
        mt.addField(new Field23E("CMSW")); // sweep the account
        mt.addField(new Field23E("CMZB")); // Zero balance the account
        //mt.addField(new Field23E("CMTO")); // Top the account - This transaction contains a cash management instruction, requesting to top the account of the ordering customer above a certain floor amount. The floor amount, if not pre-agreed by the parties involved, may be specified after the code.
        mt.addField(new Field23E("INTC")); // Intra-company transfer - This transaction contains a cash management instruction, requesting to transfer funds between accounts of the same ordering customer.

        //mt.addField(new Field32A("210101USD1000,00")); // value date, currency, amount
        var f32 = new Field32B();
        f32.setComponent1(Utility.getDaysFromNow(0));
        f32.setAmount(Utility.getRandomDecimal(BigDecimal.valueOf(1000), BigDecimal.valueOf(100000)));
        f32.setCurrency(account.getCurrencyCode());

        mt.addField(f32);

        var f59 = new Field59();

        var components = Stream.of(account.getAccountNumber(), customer.getName(), customer.getAddressLine1(), customer.getAddressLine2(),
                        customer.getCountryAndCode1(), customer.getCountryAndCode2())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (components.size() > 5) {
            // concatenate the last n components
            var lastComponent = String.join(" ", components.subList(4, components.size()));
            components = components.subList(0, 4);
            components.add(lastComponent);
        }

        f59.setComponents(components);

        mt.addField(f59);
    }

    @Override
    void createMessage() {

        MT101 mt = new MT101();

        var customer = customerRepository.findRandomRecord();
        var accounts = customer.getAccounts();
        var randomAccount = Utility.getRandomRecord(accounts);

        mt.setSender(this.senderBic);
        mt.setReceiver(randomAccount.getBic());

        // sender's reference
        mt.addField(new Field20(Utility.generateReferenceNumber()));
        // message index / total
        mt.addField(new Field28D("1/1"));

        // requested execution date
        mt.addField(new Field30(Utility.getDaysFromNow(0)));

        for (var account : accounts) {
            addPaymentInstruction(mt, customer, account);
        }

        mt.addField(new Field71A("OUR"));

        String message = mt.message();

        jmsTemplate.convertAndSend(LMS_REQUEST_PAYMENT_MT101_RQ, message);

        log.info(message);
    }
}
