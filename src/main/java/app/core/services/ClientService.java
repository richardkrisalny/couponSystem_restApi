package app.core.services;

import app.core.repositories.CompanyRepo;
import app.core.repositories.CouponRepo;
import app.core.repositories.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ClientService {
    @Autowired
    protected CompanyRepo companyRepo;
    @Autowired
    protected CouponRepo couponRepo;
    @Autowired
    protected CustomerRepo customerRepo;
}
