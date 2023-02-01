package app.core.services;

import app.core.entites.Category;
import app.core.entites.Company;
import app.core.entites.Coupon;
import app.core.exeptions.ClientServiceException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CompanyService extends ClientService{

    /**
     * Add a new coupon to the database and associate it with the logged in company
     * @param coupon the new coupon to be added
     * @return the added coupon
     * @throws ClientServiceException if a coupon with the same title already exists for the logged in company
     */
    public Coupon addCoupon(Coupon coupon ,int companyID) throws ClientServiceException {
        if(!couponRepo.existsByTitleAndCompanyID(coupon.getTitle(),companyID)){
            coupon.setCompanyID(companyID);
            getCompanyDetails(companyID).getCoupons().add(coupon);
            System.out.println(coupon);
            return couponRepo.save(coupon);
        }else{
            throw new ClientServiceException("the company: "+companyID+" have coupon with same title");
        }
    }
    /**
     * Update an existing coupon in the database
     * @param coupon the updated coupon
     * @return the updated coupon
     * @throws ClientServiceException if the coupon does not exist in the database or the coupon does not belong to the logged in company.
     */
    public Coupon updateCoupon(Coupon coupon,int companyID) throws ClientServiceException {
       Coupon coupon1= couponRepo.findById(coupon.getId()).orElseThrow(()->new ClientServiceException("the coupon: "+coupon.getId()+" dose not exist"));
        if(coupon1.getCompanyID()==companyID){
            coupon.setCompanyID(companyID);
            coupon.setCustomers(coupon1.getCustomers());
            return couponRepo.save(coupon);
        }else{
            throw new ClientServiceException("the company: "+companyID+" can't update coupon: "+coupon.getId()+" because don't have it");
        }
    }
    /**
     * Delete a coupon from the database that is associated with the logged in company
     * @param couponID the id of the coupon to be deleted
     * @throws ClientServiceException if the coupon does not exist in the database or the coupon does not belong to the logged in company
     */
    public void deleteCoupon(int couponID,int companyID) throws ClientServiceException {
        Coupon coupon = couponRepo.findById(couponID).orElseThrow(()->new ClientServiceException("the coupon: "+couponID+" dose not exist"));
        if(coupon.getCompanyID()==companyID)
            couponRepo.delete(coupon);
        else
            throw new ClientServiceException("the company "+companyID+" can't delete coupon "+couponID);
    }
    /**
     * Get a list of all coupons that belong to the logged in company
     * @return a list of coupons
     */
    public List<Coupon> companyCoupons(int companyID){
        return couponRepo.findByCompanyID(companyID);
    }
    /**
     * Get a list of all coupons that belong to the logged in company and match the specified category
     * @param category the category of the coupons
     * @return a list of coupons
     */
    public List<Coupon> companyCoupons(Category category,int companyID){
        return couponRepo.findByCompanyIDAndCategory(companyID,category);
    }
    /**
     * Get a list of all coupons that belong to the logged in company and have a price less than the specified max price
     * @param maxPrice the maximum price for the coupons
     * @return a list of coupons
     */
    public List<Coupon> companyCoupons(double maxPrice,int companyID){
        return couponRepo.findByCompanyIDAndPriceLessThan(companyID,maxPrice);
    }
    /**
     * Get details of the logged in company
     * @return the company object
     * @throws ClientServiceException if the company with companyID not exists
     */
    public Company getCompanyDetails(int companyID){
            return companyRepo.findById(companyID).orElseThrow(()->new ClientServiceException("the company: "+companyID +"dose not exist"));
    }
}
