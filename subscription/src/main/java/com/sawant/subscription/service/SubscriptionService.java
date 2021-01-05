package com.sawant.subscription.service;

import com.sawant.subscription.model.Subscription;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static java.util.Map.entry;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Service
public class SubscriptionService {

    private static String MONTHLY_SUBSCRIPTION="MONTHLY";
    private static String WEEKLY_SUBSCRIPTION="WEEKLY";

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");


    private static Map<String,Integer> dayValueMap=Map.ofEntries(
            entry("SUNDAY",1),
            entry("MONDAY",2),
            entry("TUESDAY",3),
            entry("WEDNESDAY",4),
            entry("THURSDAY",5),
            entry("FRIDAY",6),
            entry("SATURDAY",7)
    );

    public Mono<ServerResponse> createSubscription(ServerRequest request) {
        List<String> invoiceDates = new ArrayList<>();

        return request.bodyToMono(Subscription.class)
                .flatMap(subscription -> {
                    LocalDate startDate=LocalDate.of(subscription.getStartDate().getYear(),subscription.getStartDate().getMonth(),subscription.getStartDate().getDate());
                    LocalDate endDate=LocalDate.of(subscription.getEndDate().getYear(),subscription.getEndDate().getMonth(),subscription.getEndDate().getDate());
                    if(ChronoUnit.MONTHS.between(startDate,endDate)>3){
                        return ServerResponse.badRequest().body(fromValue("Time Period should be below 3 months."));
                    }
                    if(subscription.getSubscriptionType().equalsIgnoreCase(MONTHLY_SUBSCRIPTION)){
                        Calendar c1 = Calendar.getInstance();
                        c1.set(Calendar.DAY_OF_MONTH, startDate.getDayOfMonth());
                        for(int i=1;i<=3;i++) {
                            Calendar c2 = Calendar.getInstance();
                            c2.set(Calendar.DAY_OF_MONTH, c1.getTime().getDate());
                            c2.set(Calendar.MONTH, c1.getTime().getMonth());
                            c2.add(Calendar.MONTH, i);
                            invoiceDates.add(simpleDateFormat.format(c2.getTime()));
                            subscription.setInvoiceDates(invoiceDates);
                        }
                    }else if(subscription.getSubscriptionType().equalsIgnoreCase(WEEKLY_SUBSCRIPTION)){

                        if(dayValueMap.get(subscription.getDay().toUpperCase())==null){
                            return ServerResponse.badRequest().body(fromValue("Invalid Day provided."));
                        }

                        LocalDate stDate = startDate.with(TemporalAdjusters.next(DayOfWeek.valueOf(subscription.getDay().toUpperCase())));

                        while(stDate.isBefore(endDate)){
                            invoiceDates.add(simpleDateFormat.format(Date.from(stDate.atStartOfDay(ZoneId.systemDefault()).toInstant())));
                            stDate=stDate.plusDays(7);
                        }
                        subscription.setInvoiceDates(invoiceDates);
                    }
                    else {
                        return ServerResponse.badRequest().body(fromValue("Invalid Subscription Type"));
                    }

                    return ServerResponse.ok().body(fromValue(subscription));
                });
    }
}
