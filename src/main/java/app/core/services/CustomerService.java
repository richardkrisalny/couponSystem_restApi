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
import org.springframework.stereotype.Service;
import org.w3c.dom.css.Counter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService extends ClientService{
    private int customerID;
    /**
     * check if the customer exist
     * @param email customer email
     * @param password customer password
     * @return true if customer exist , else return false
     */
    public boolean login(String email, String password) {
       if(!customerRepo.findByEmailAndPassword(email, password).isEmpty()){
           customerID=customerRepo.findByEmailAndPassword(email, password).get(0).getId();
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
        Optional<Customer>optionalCustomer=customerRepo.findById(customerID);
        Optional<Coupon>optionalCoupon=couponRepo.findById(couponID);
        if(optionalCustomer.isPresent()&&optionalCoupon.isPresent()){
            optionalCustomer.get().getCoupons().add(optionalCoupon.get());
        }else{
            throw new ClientServiceException("the coupon or customer is not exist");
        }
    }

    /**
     * get all coupons for customer
     * @return list of coupons
     */
    public List<Coupon> getCustomerCoupons(){
        Optional<Customer>optionalCustomer=customerRepo.findById(customerID);
        if(optionalCustomer.isPresent()){
            return optionalCustomer.get().getCoupons();
        }else{
            throw new ClientServiceException("the coupon or customer is not exist");
        }
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
            if (coupons.get(i).getCategory()==category){
                couponsToReturn.add(coupons.get(i));
            }
        }return couponsToReturn;
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
            if (coupons.get(i).getPrice()<maxPrice){
                couponsToReturn.add(coupons.get(i));
            }
        }return couponsToReturn;
    }

    /**
     * get the customer details
     * @return customer
     */
    public Customer getCustomerDetails(){
        Optional<Customer>optionalCustomer=customerRepo.findById(customerID);
        if(optionalCustomer.isPresent()){
            return optionalCustomer.get();
        }else {
            throw new ClientServiceException("the customer not exist");
        }
    }

}
