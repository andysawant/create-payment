package com.sawant.subscription.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class Subscription {
    private double amount;
    private String subscriptionType;
    private List<String> invoiceDates;

}
