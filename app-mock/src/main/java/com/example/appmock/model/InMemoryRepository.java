package com.example.appmock.model;

import com.example.appmock.Utility;

import java.util.ArrayList;
import java.util.List;

public class InMemoryRepository implements CustomerRepository {
    private static final List<Customer> customers = new ArrayList<Customer>();
    @Override
    public Customer findRandomRecord() {
        return Utility.getRandomRecord(customers);
    }

    @Override
    public void save(Customer customer) {
        customers.add(customer);
    }
}
