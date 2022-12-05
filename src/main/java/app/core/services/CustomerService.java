package app.core.services;

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

}
