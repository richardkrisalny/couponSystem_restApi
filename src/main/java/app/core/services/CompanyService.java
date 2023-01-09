package app.core.services;

import app.core.entites.Category;
import app.core.entites.Company;
import app.core.entites.Coupon;
import app.core.exeptions.ClientServiceException;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@Transactional
@Scope("prototype")
public class CompanyService extends ClientService{
    private int companyID;
    public boolean login(String email, String password) {
      if(companyRepo.findByEmailAndPassword(email,password)!=null) {
          companyID = companyRepo.findByEmailAndPassword(email, password).getId();
          return true;
      }
      else{
          return false;
      }

    }
    public Coupon addCoupon(Coupon coupon) throws ClientServiceException {
        if(!couponRepo.existsByTitleAndCompanyID(coupon.getTitle(),companyID)){
            coupon.setCompanyID(companyID);
            getCompanyDetails().getCoupons().add(coupon);
            return couponRepo.save(coupon);
        }else{
            throw new ClientServiceException("the company: "+companyID+" have coupon with same title");
        }
    }

    public Coupon updateCoupon(Coupon coupon) throws ClientServiceException {
       Coupon coupon1= couponRepo.findById(coupon.getId()).orElseThrow(()->new ClientServiceException("the coupon: "+coupon.getId()+" dose not exist"));
        if(coupon1.getCompanyID()==companyID){
            coupon.setCompanyID(companyID);
            return couponRepo.save(coupon);
        }else{
            throw new ClientServiceException("the companies id should by same: ("+coupon.getId()+" != "+coupon1.getId());
        }
    }

    public void deleteCoupon(int couponID) throws ClientServiceException {
        Coupon coupon = couponRepo.findById(couponID).orElseThrow(()->new ClientServiceException("the coupon: "+couponID+" dose not exist"));
        if(coupon.getCompanyID()==companyID)
            couponRepo.delete(coupon);
        else
            throw new ClientServiceException("the company "+companyID+" can't delete coupon "+couponID);
    }
    public List<Coupon> companyCoupons(){
        return couponRepo.findByCompanyID(companyID);
    }

    public List<Coupon> companyCoupons(Category category){
        return couponRepo.findByCompanyIDAndCategory(companyID,category);
    }

    public List<Coupon> companyCoupons(double maxPrice){
        return couponRepo.findByCompanyIDAndPriceLessThan(companyID,maxPrice);
    }
    public Company getCompanyDetails(){
            return companyRepo.findById(companyID).orElseThrow(()->new ClientServiceException("the company: "+companyID +"dose not exist"));
    }
}
