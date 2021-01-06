package com.sawant.subscription.util;

import com.sawant.subscription.model.Subscription;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import static java.util.Map.entry;

@Component
public class SubscriptionUtil {


    private static Map<String,Integer> dayValueMap=Map.ofEntries(
            entry("SUNDAY",1),
            entry("MONDAY",2),
            entry("TUESDAY",3),
            entry("WEDNESDAY",4),
            entry("THURSDAY",5),
            entry("FRIDAY",6),
            entry("SATURDAY",7)
    );

    public static Boolean isValidInteger(String numString){
        try{
            Integer.parseInt(numString);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public static List<String> validateSubscription(Subscription subscription){
        List<String> errorList=new ArrayList<>();

        Calendar startc=Calendar.getInstance();
        startc.setTime(subscription.getStartDate());
        LocalDate startDate=LocalDate.of(startc.get(Calendar.YEAR),startc.get(Calendar.MONTH),startc.get(Calendar.DATE));

        Calendar endc=Calendar.getInstance();
        endc.setTime(subscription.getEndDate());
        LocalDate endDate=LocalDate.of(endc.get(Calendar.YEAR),endc.get(Calendar.MONTH),endc.get(Calendar.DATE));

        if(ChronoUnit.MONTHS.between(startDate,endDate)>3){
            errorList.add("Time Period should be below 3 months.");
        }

        if(!subscription.getSubscriptionType().equalsIgnoreCase("MONTHLY")
                && !subscription.getSubscriptionType().equalsIgnoreCase("WEEKLY")){
            errorList.add("Invalid Subscription Type.");
        }

        if(subscription.getSubscriptionType().equalsIgnoreCase("MONTHLY") && !isValidInteger(subscription.getDay())){
            errorList.add("Invalid Date provided");
        }

        if(subscription.getSubscriptionType().equalsIgnoreCase("WEEKLY") && dayValueMap.get(subscription.getDay().toUpperCase())==null){

            errorList.add("Invalid Day of week provided");
        }
        return errorList;
    }
}
