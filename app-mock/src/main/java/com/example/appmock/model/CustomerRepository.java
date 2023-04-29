package com.example.appmock.model;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository {

    Customer findRandomRecord();
    void save(Customer customer);

}
