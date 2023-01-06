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
    /**
     * check if the customer exist
     * @param email customer email
     * @param password customer password
     * @return true if customer exist , else return false
     */
    public boolean login(String email, String password) {
       if(customerRepo.findByEmailAndPassword(email, password)!=null){
           customerID=customerRepo.findByEmailAndPassword(email, password).getId();
           return true;
       }else {
           return false;
       }
    }
    /**
     * buy coupon
     * @param couponID coupon id
     */
    public void purchase(int couponID) throws ClientServiceException {
        Customer customer=customerRepo.findById(customerID).orElseThrow(()->new ClientServiceException("the customer is not exist"));
        Coupon coupon = couponRepo.findById(couponID).orElseThrow(()->new ClientServiceException("the coupon is not exist"));
        if(coupon.getAmount()>0&&coupon.getEndDate().isAfter(LocalDate.now())){
            for (Coupon coup:customer.getCoupons()) {
                if(coup.getId()==coupon.getId())
                   throw new ClientServiceException("the customer already have this coupon");
            }
            customer.getCoupons().add(coupon);
            coupon.setAmount(coupon.getAmount()-1);
        }

    }

    /**
     * get all coupons for customer
     * @return list of coupons
     */
    public List<Coupon> getCustomerCoupons(){
            return customerRepo.findById(customerID).orElseThrow(()->new ClientServiceException("the coupon or customer is not exist")).getCoupons();
    }

    /**
     * get all coupon with specific category
     * @param category the category
     * @return list of coupons
     */
    public List<Coupon>getCustomerCoupons(Category category){
        List<Coupon>couponsToReturn=new ArrayList<>();
        List<Coupon>coupons=getCustomerCoupons();
        for (int i = 0; i < coupons.size(); i++) {
            if(coupons.get(i).getCategory()==category)
                couponsToReturn.add(coupons.get(i));
        }
        return couponsToReturn;
    }

    /**
     * get all customer coupons under some price
     * @param maxPrice the maximum price
     * @return list of coupons
     */
    public List<Coupon>getCustomerCoupons(double maxPrice){
        List<Coupon>couponsToReturn=new ArrayList<>();
        List<Coupon>coupons=getCustomerCoupons();
        for (int i = 0; i < coupons.size(); i++) {
            if(coupons.get(i).getPrice()<maxPrice)
                couponsToReturn.add(coupons.get(i));
        }
        return couponsToReturn;
    }

    /**
     * get the customer details
     * @return customer
     */
    public Customer getCustomerDetails(){
            return customerRepo.findById(customerID).orElseThrow(()->new ClientServiceException("the customer not exist"));
    }

}
