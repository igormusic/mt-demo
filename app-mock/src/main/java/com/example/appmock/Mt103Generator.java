package com.example.appmock;

import com.example.appmock.model.Account;
import com.example.appmock.model.Customer;
import com.example.appmock.model.CustomerRepository;
import com.github.javafaker.Faker;
import com.prowidesoftware.swift.model.field.*;
import com.prowidesoftware.swift.model.mt.mt1xx.MT103;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.appmock.config.JmsConfig.LMS_OUTGOING_WIRE_MT103_RQ;

@Component
@Slf4j
public class Mt103Generator extends MessageGenerator {

    private final CustomerRepository customerRepository;
    private final JmsTemplate jmsTemplate;

    public Mt103Generator(CustomerRepository customerRepository, JmsTemplate jmsTemplate) {
        this.customerRepository = customerRepository;
        this.jmsTemplate = jmsTemplate;
    }

    private static void addPaymentInstruction(MT103 mt, Faker faker, Customer customer, Account account) {
        var f32 = new Field32A();
        f32.setComponent1(Utility.getDaysFromNow(0));
        f32.setAmount(Utility.getRandomDecimal(BigDecimal.valueOf(1000), BigDecimal.valueOf(100000)));
        f32.setCurrency(account.getCurrencyCode());

        mt.addField(f32);

        var f50k = new Field50K();

        List<String> components = getComponents(customer, faker.finance().iban("NL"));

        f50k.setComponents(components);

        mt.addField(f50k);

        var f59 = new Field59();

        components = getComponents(customer, account.getAccountNumber());

        f59.setComponents(components);

        mt.addField(f59);
    }

    private static List<String> getComponents(Customer customer, String accountNumber) {
        var components = Stream.of(accountNumber, customer.getName(), customer.getAddressLine1(), customer.getAddressLine2(),
                        customer.getCountryAndCode1(), customer.getCountryAndCode2())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (components.size() > 5) {
            // concatenate the last n components
            var lastComponent = String.join(" ", components.subList(4, components.size()));
            components = components.subList(0, 4);
            components.add(lastComponent);
        }
        return components;
    }

    @Override
    void createMessage() {

        MT103 mt = new MT103();
        Faker faker = new Faker();

        var customer = customerRepository.findRandomRecord();
        var accounts = customer.getAccounts();
        var randomAccount = Utility.getRandomRecord(accounts);

        mt.setSender(this.senderBic);
        mt.setReceiver(randomAccount.getBic());

        // sender's reference
        mt.addField(new Field20(Utility.generateReferenceNumber()));
        // bank operation code
        mt.addField(new Field23B("CRED")); // credit transfer
        mt.addField(new Field23B("SSTD")); // standard transfer

        mt.addField(new Field23E("INTC")); // Intra-company transfer
        mt.addField(new Field23E("SDVA")); // Same day value

        for (var account : accounts) {
            addPaymentInstruction(mt, faker, customer, account);
        }

        mt.addField(new Field71A("OUR"));

        String message = mt.message();

        jmsTemplate.convertAndSend(LMS_OUTGOING_WIRE_MT103_RQ, message);

        log.info(message);

    }
}
