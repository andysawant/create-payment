package com.sawant.subscription.service;

import com.sawant.subscription.model.Subscription;
import com.sawant.subscription.util.SubscriptionUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Service
public class SubscriptionService {

    private static String MONTHLY_SUBSCRIPTION="MONTHLY";
    private static String WEEKLY_SUBSCRIPTION="WEEKLY";

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public Mono<ServerResponse> createSubscription(ServerRequest request) {
        List<String> invoiceDates = new ArrayList<>();

        return request.bodyToMono(Subscription.class)
                .flatMap(subscription -> {
                    if(!SubscriptionUtil.validateSubscription(subscription).isEmpty()){
                        return ServerResponse.badRequest().body(fromValue(SubscriptionUtil.validateSubscription(subscription)));
                    }
                    Calendar startc=Calendar.getInstance();
                    startc.setTime(subscription.getStartDate());
                    LocalDate startDate=LocalDate.of(startc.get(Calendar.YEAR),startc.get(Calendar.MONTH),startc.get(Calendar.DATE));

                    Calendar endc=Calendar.getInstance();
                    endc.setTime(subscription.getEndDate());
                    LocalDate endDate=LocalDate.of(endc.get(Calendar.YEAR),endc.get(Calendar.MONTH),endc.get(Calendar.DATE));

                    if(subscription.getSubscriptionType().equalsIgnoreCase(MONTHLY_SUBSCRIPTION)){
                        Calendar c1 = Calendar.getInstance();
                        c1.set(Calendar.DAY_OF_MONTH, startDate.getDayOfMonth());
                        for(int i=1;i<=3;i++) {
                            Calendar c2 = Calendar.getInstance();
                            c2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(subscription.getDay()));
                            c2.set(Calendar.MONTH, c1.get(Calendar.MONTH));
                            c2.add(Calendar.MONTH, i);
                            invoiceDates.add(simpleDateFormat.format(c2.getTime()));
                            subscription.setInvoiceDates(invoiceDates);
                        }
                    }else if(subscription.getSubscriptionType().equalsIgnoreCase(WEEKLY_SUBSCRIPTION)){

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
