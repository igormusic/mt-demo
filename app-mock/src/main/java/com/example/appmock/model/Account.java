package com.example.appmock.model;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class Account {

    private Long id;

    @Size(min = 0, max = 35)
    private String accountNumber;

    @Size(min = 3, max = 12)
    private String bic;

    @Size(min = 0, max = 35)
    private String currencyCode;

    private Double balance;

}
