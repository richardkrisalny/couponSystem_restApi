package app.core.tests;

import app.core.thred.CouponExpirationDailyJod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class Test implements CommandLineRunner {
    @Autowired
    private CouponExpirationDailyJod couponExpirationDailyJod;
    @Autowired
    private TestAll testAll;
    @Override
    public void run(String... args) throws Exception {
        try {
            couponExpirationDailyJod.startJob();
            testAll.testAdmin();
            testAll.testCompany();
            testAll.testCustomer();
            TimeUnit.SECONDS.sleep(15);
            couponExpirationDailyJod.stop();
        }
        catch (Exception e){
            e.printStackTrace();
            couponExpirationDailyJod.stop();
            System.out.println("\n\n-------------------Error in test--------------------------------------\n\n");
        }
    }
}
