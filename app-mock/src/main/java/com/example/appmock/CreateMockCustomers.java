package com.example.appmock;

import com.example.appmock.model.Account;
import com.example.appmock.model.Customer;
import com.example.appmock.model.CustomerRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Locale;

@Component
public class CreateMockCustomers implements ApplicationRunner {

    @Value("${app.mock.customers.count}")
    private int customersCount;

    private final CustomerRepository customerRepository;

    public CreateMockCustomers(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Faker faker = new Faker(new Locale("en-US"));

        var customers = new ArrayList<Customer>();

        for(int i = 0; i < customersCount; i++) {

            var customer = new Customer();

            String name = truncateString(faker.company().name(), 35);
            String addressLine1 = faker.address().streetAddressNumber() + " " + faker.address().streetName();
            String addressLine2 = faker.address().secondaryAddress();
            String addressLine3 = truncateString(faker.address().cityName() + " - " + faker.address().stateAbbr(),34);

            String country = truncateString(faker.address().country(), 35);
            String postalCode = getPostalCode(faker);

            customer.setName(name);
            customer.setAddressLine1(addressLine1);
            customer.setAddressLine2(addressLine2);
            customer.setAddressLine3(addressLine3);
            customer.setCountryAndCode1(postalCode);
            customer.setCountryAndCode2(country);


            for (int j = 1; j < faker.random().nextInt(3, 6); j++) {
                var account = new Account();

                account.setAccountNumber(faker.finance().iban());
                account.setCurrencyCode(faker.currency().code());
                account.setBic(faker.finance().bic());
                account.setBalance(faker.number().randomDouble(2, 1000, 1000000));
                customer.addAccount(account);
            }

            System.out.println(customer.toString());

            customerRepository.save(customer);
        }
    }

    public static String truncateString(String str, int maxLength) {
        if (str == null) {
            return null;
        }
        if (str.length() > maxLength) {
            return str.substring(0, maxLength);
        }
        return str;
    }

    private static String getPostalCode(Faker faker) {
        try {
            return faker.address().zipCodeByState(faker.address().stateAbbr());
        }
        catch (Exception e) {
            return faker.address().zipCode();
        }

    }

}
