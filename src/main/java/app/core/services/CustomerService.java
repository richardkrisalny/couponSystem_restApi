package app.core.services;

import app.core.entites.Category;
import app.core.entites.Coupon;
import app.core.entites.Customer;
import app.core.exeptions.ClientServiceException;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Scope("prototype")

public class CustomerService extends ClientService{
    private int customerID;

    public boolean login(String email, String password) {
       if(customerRepo.findByEmailAndPassword(email, password)!=null){
           customerID=customerRepo.findByEmailAndPassword(email, password).getId();
           return true;
       }else {
           return false;
       }
    }
    public void purchase(int couponID) throws ClientServiceException {
        Customer customer=customerRepo.findById(customerID).orElseThrow(()->new ClientServiceException("the customer: "+customerID+" dose not exist"));
        Coupon coupon = couponRepo.findById(couponID).orElseThrow(()->new ClientServiceException("the coupon: "+couponID+" dose not exist"));
        if(coupon.getAmount()>0&&coupon.getEndDate().isAfter(LocalDate.now())){
            for (Coupon coup:customer.getCoupons()) {
                if(coup.getId()==coupon.getId())
                   throw new ClientServiceException("the customer: "+customerID+" already have this coupon");
            }
            customer.getCoupons().add(coupon);
            coupon.setAmount(coupon.getAmount()-1);
        }else {
            throw new ClientServiceException("can't purchase the coupon: the amount is 0 or dead line is over");
        }

    }

    public List<Coupon> getCustomerCoupons(){
            return customerRepo.findById(customerID).orElseThrow(()->new ClientServiceException("the customer "+customerID+" dose not exist")).getCoupons();
    }

    public List<Coupon>getCustomerCoupons(Category category){
        List<Coupon>couponsToReturn=new ArrayList<>();
        List<Coupon>coupons=getCustomerCoupons();
        for (int i = 0; i < coupons.size(); i++) {
            if(coupons.get(i).getCategory()==category)
                couponsToReturn.add(coupons.get(i));
        }
        return couponsToReturn;
    }

    public List<Coupon>getCustomerCoupons(double maxPrice){
        List<Coupon>couponsToReturn=new ArrayList<>();
        List<Coupon>coupons=getCustomerCoupons();
        for (int i = 0; i < coupons.size(); i++) {
            if(coupons.get(i).getPrice()<maxPrice)
                couponsToReturn.add(coupons.get(i));
        }
        return couponsToReturn;
    }

    public Customer getCustomerDetails(){
            return customerRepo.findById(customerID).orElseThrow(()->new ClientServiceException("the customer "+customerID+" dose not exist"));
    }

}
