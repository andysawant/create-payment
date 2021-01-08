package com.sawant.subscription;

import com.sawant.subscription.model.Subscription;
import com.sawant.subscription.util.SubscriptionUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SubscriptionUtilTest {
    @Autowired
    SubscriptionUtil subscriptionUtil;


    @Test
    public void testValidSubscription() throws Exception{
    Subscription sub = new Subscription();
		sub.setAmount(10);
		sub.setSubscriptionType("WEEKLY");
		sub.setDay("TUESDAY");
		String sDate1="15/02/2021";
        Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
		sub.setStartDate(date1);
        String sDate2="15/02/2021";
        Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);
		sub.setEndDate(date2);
    List<String> errorList = subscriptionUtil.validateSubscription(sub);

    assertTrue(errorList.isEmpty());
    }

    @Test
    public void testInvalidSubscription() throws Exception{
        Subscription sub = new Subscription();
        sub.setAmount(10);
        sub.setSubscriptionType("WEEKLYa");
        sub.setDay("TUESDAY");
        String sDate1="15/02/2021";
        Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
        sub.setStartDate(date1);
        String sDate2="15/02/2021";
        Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);
        sub.setEndDate(date2);
        List<String> errorList = subscriptionUtil.validateSubscription(sub);

        assertTrue(!errorList.isEmpty());
    }
}
