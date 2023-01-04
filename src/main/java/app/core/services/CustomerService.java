package app.core.services;

import app.core.entites.Category;
import app.core.entites.Coupon;
import app.core.entites.Customer;
import app.core.exeptions.ClientServiceException;
import app.core.repositories.CompanyRepo;
import app.core.repositories.CouponRepo;
import app.core.repositories.CustomerRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.css.Counter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            customerRepo.findById(customerID).orElseThrow(()->new ClientServiceException("the coupon or customer is not exist"))
                    .getCoupons().add(couponRepo.findById(couponID).orElseThrow(()->new ClientServiceException("the coupon or customer is not exist")));
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
