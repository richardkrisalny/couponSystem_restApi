package app.core.services;

import app.core.entites.Category;
import app.core.entites.Coupon;
import app.core.entites.Customer;
import app.core.exeptions.ClientServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CustomerService extends ClientService{
    /**
     * Purchase a coupon
     * @param couponID coupon ID
     * @throws ClientServiceException if
     * 		- the customer not exist
     * 		- the coupon not exist
     * 		- the customer already have this coupon
     * 		- the coupon amount is 0 or deadline is over
     */
    public void purchase(int couponID,int customerID) throws ClientServiceException {
        Customer customer=customerRepo.findById(customerID).orElseThrow(()->new ClientServiceException("the customer: "+customerID+" dose not exist"));
        Coupon coupon = couponRepo.findById(couponID).orElseThrow(()->new ClientServiceException("the coupon: "+couponID+" dose not exist"));
        if(coupon.getAmount()>0&&coupon.getEndDate().isAfter(LocalDate.now())){
            for (Coupon coup:customer.getCoupons()) {
                if(coup.getId()==coupon.getId())
                   throw new ClientServiceException("the customer: "+customerID+" already have this coupon");
            }
            customer.addCoupon(coupon);
            coupon.setAmount(coupon.getAmount()-1);
        }else {
            throw new ClientServiceException("can't purchase the coupon: the amount is 0 or dead line is over");
        }

    }
    /**
     Returns a List of all the coupons associated with the customer.
     @throws ClientServiceException when the customer with the given customerID does not exist in the repository
     @return A List of Coupon objects
     */
    public List<Coupon> getCustomerCoupons(int customerID){
            return customerRepo.findById(customerID).orElseThrow(()->new ClientServiceException("the customer "+customerID+" dose not exist")).getCoupons();
    }
    /**
     Returns a List of all the coupons associated with the customer that belong to a specific category.
     @param category the category of the coupons to be returned
     @throws ClientServiceException when the customer with the given customerID does not exist in the repository
     @return A List of Coupon objects
     */
    public List<Coupon>getCustomerCoupons(Category category,int customerID){
        List<Coupon>couponsToReturn=new ArrayList<>();
        List<Coupon>coupons=getCustomerCoupons(customerID);
        for (int i = 0; i < coupons.size(); i++) {
            if(coupons.get(i).getCategory()==category)
                couponsToReturn.add(coupons.get(i));
        }
        return couponsToReturn;
    }
    /**
     Returns a List of all the coupons associated with the customer that have a price lower than the given maximum price.
     @param maxPrice the maximum price of the coupons to be returned
     @throws ClientServiceException when the customer with the given customerID does not exist in the repository
     @return A List of Coupon objects
     */
    public List<Coupon>getCustomerCoupons(double maxPrice,int customerID){
        List<Coupon>couponsToReturn=new ArrayList<>();
        List<Coupon>coupons=getCustomerCoupons(customerID);
        for (int i = 0; i < coupons.size(); i++) {
            if(coupons.get(i).getPrice()<maxPrice)
                couponsToReturn.add(coupons.get(i));
        }
        return couponsToReturn;
    }
    /**
     Returns the customer details associated with the customer ID.
     @throws ClientServiceException when the customer with the given customerID does not exist in the repository
     @return A Customer object
     */
    public Customer getCustomerDetails(int customerID){
            return customerRepo.findById(customerID).orElseThrow(()->new ClientServiceException("the customer "+customerID+" dose not exist"));
    }

}
