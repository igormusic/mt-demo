package com.example.appmock.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class Customer {

    private Long id;
    @Size(min = 3, max = 35)
    private String name;
    @Size(min = 0, max = 35)
    private String addressLine1;
    @Size(min = 0, max = 35)
    private String addressLine2;
    @Size(min = 0, max = 35)
    private String addressLine3;
    @Size(min = 0, max = 35)
    private String countryAndCode1;
    @Size(min = 0, max = 35)
    private String countryAndCode2;

    @Setter(AccessLevel.NONE)
    private List<Account> accounts = new ArrayList<>();

    public void addAccount(Account account) {
        accounts.add(account);
    }

}
