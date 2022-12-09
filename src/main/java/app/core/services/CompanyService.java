package app.core.services;

import app.core.entites.Category;
import app.core.entites.Company;
import app.core.entites.Coupon;
import app.core.exeptions.ClientServiceException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompanyService extends ClientService{
    private int companyID;

    /**
     * check if the company exist in database
     * @param email the company email
     * @param password the company password
     * @return true if company exist and false if not
     */
    public boolean login(String email, String password) {
      if(!companyRepo.findByEmailAndPassword(email,password).isEmpty()) {
          companyID = companyRepo.findByEmailAndPassword(email, password).get(0).getId();
          return true;
      }
      else
          return false;
    }

    /**
     * add coupon to database and check if the company have same coupon
     * @param coupon the coupon to add
     */
    public void addCoupon(Coupon coupon) throws ClientServiceException {
        if(couponRepo.findByTitleAndCompanyID(coupon.getTitle(),companyID).isEmpty()){
            coupon.setCompanyID(companyID);
            couponRepo.save(coupon);
            getCompanyDetails().getCoupons().add(coupon);
        }else{
            throw new ClientServiceException("the company: "+companyID+" have coupon with same title");
        }
    }


    /**
     * update coupon
     * @param coupon the coupon to update
     */
    public void updateCoupon(Coupon coupon) throws ClientServiceException {
        Optional<Coupon>opt=couponRepo.findById(coupon.getId());
        if(opt.isPresent()&&couponRepo.findByTitleAndCompanyID(coupon.getTitle(),companyID).isEmpty()){
            coupon.setCompanyID(companyID);
            opt.get().setAmount(coupon.getAmount());
            opt.get().setCategory(coupon.getCategory());
            opt.get().setCustomers(coupon.getCustomers());
            opt.get().setImage(coupon.getImage());
            opt.get().setTitle(coupon.getTitle());
            opt.get().setEndDate(coupon.getEndDate());
            opt.get().setStartDate(coupon.getStartDate());
            opt.get().setPrice(coupon.getPrice());
            opt.get().setDescription(coupon.getDescription());
            couponRepo.save(opt.get());
        }else{
            throw new ClientServiceException("the company id is not the same or coupon not exist");
        }
    }

    /**
     * delete coupon from database
     * @param couponID coupon id to delete
     */
    public void deleteCoupon(int couponID) throws ClientServiceException {
        Optional<Coupon>opt=couponRepo.findById(couponID);
        if(opt.isPresent()){
            couponRepo.delete(opt.get());
        }else{
            throw new ClientServiceException("the coupon not exist");
        }
    }

    /**
     * get all company coupons
     * @return list of coupons
     */
    public List<Coupon> companyCoupons(){
        return couponRepo.findByCompanyID(companyID);
    }
    /**
     * get all company coupons with specific category
     * @param category the category
     * @return list of coupons
     */
    public List<Coupon> companyCoupons(Category category){
        return couponRepo.findByCompanyIDAndCategory(companyID,category);
    }
    /**
     * get all company coupons under specific price
     * @param maxPrice the maximum price
     * @return list of coupons
     */
    public List<Coupon> companyCoupons(double maxPrice){
        return couponRepo.findByCompanyIDAndPriceLessThan(companyID,maxPrice);
    }
    /**
     * get the company details
     * @return company
     */
    public Company getCompanyDetails(){
        Optional<Company>opt=companyRepo.findById(companyID);
        if(opt.isPresent()){
            return opt.get();
        }else{
            throw new ClientServiceException("the company not exist");
        }
    }
}
