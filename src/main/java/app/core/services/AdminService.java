package app.core.services;

import app.core.entites.Company;
import app.core.entites.Customer;
import app.core.exeptions.ClientServiceException;
import app.core.repositories.CompanyRepo;
import app.core.repositories.CouponRepo;
import app.core.repositories.CustomerRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@Scope("prototype")
public class AdminService extends ClientService {
    public static final String EMAIL = "admin@admin.com";
    public static final String PASSWORD = "admin";

    /**
     * check if the admin email and password is correct
     * @param email the admin email
     * @param password the admin password
     * @return true if the email and password correct else returned false
     */
    public boolean login(String email, String password) {
        return (EMAIL.equals(email) && PASSWORD.equals(password));
    }

    /**
     * check if company email or name exist in database.
     * if the not exist ,add the company
     * @param company the company to add
     */
    public void addCompany(Company company) throws ClientServiceException {
        if(!companyRepo.existsByEmailOrName(company.getEmail(), company.getName())){
            companyRepo.save(company);
        }else{
            throw new ClientServiceException("the company already exist");
        }
    }


    /**
     * find the company in database and update it
     * @param company the company to update
     */
    public void updateCompany(Company company) throws ClientServiceException {
        Optional<Company>opt=companyRepo.findById(company.getId());
        if(opt.isPresent()&& Objects.equals(opt.get().getName(), company.getName())){
            companyRepo.save(company);
        }else{
            throw new ClientServiceException("the company not exist or the name not the same");
        }
    }

    /**
     * delete company from database
     * @param companyID the company to delete
     */
    public void deleteCompany(int companyID) throws ClientServiceException{
            companyRepo.delete(companyRepo.findById(companyID).orElseThrow(()->new ClientServiceException("the company not exist")));
    }

    /**
     * get all companies from database
     * @return list of companies
     */
    public List<Company> getAllCompanies(){
        return companyRepo.findAll();
    }

    /**
     * get specific company
     * @param companyID company id
     * @return the company from database (with company coupon's)
     */
    public Company getOneCompany(int companyID)throws ClientServiceException{
            return companyRepo.findById(companyID).orElseThrow(()->new ClientServiceException("the company not exist"));
    }

    /**
     * add customer to the database
     * @param customer the customer to add
     */
    public void addCustomer(Customer customer) throws ClientServiceException {
        if(!customerRepo.existsByEmail(customer.getEmail())){
            customerRepo.save(customer);
        }else{
            throw new ClientServiceException("the customer already exist");
        }
    }

    /**
     * update customer in database
     * @param customer the customer to update
     */
    public void updateCustomer(Customer customer) throws ClientServiceException {
        customerRepo.findById(customer.getId()).orElseThrow(()->new ClientServiceException("the customer not found "));
        customerRepo.save(customer);
    }

    /**
     * delete customer from database
     * @param customerID the customer id
     */
    public void deleteCustomer(int customerID) throws ClientServiceException{
            customerRepo.delete(customerRepo.findById(customerID).orElseThrow(()->new ClientServiceException("the customer not exist")));
    }
    /**
     * get all customers from database
     * @return list of customers
     */
    public List<Customer> getAllCustomers(){
        return customerRepo.findAll();
    }

    /**
     * get specific customer from database
     * @param customerID the customer id
     * @return the specific customer
     */
    public Customer getOneCustomer(int customerID) throws ClientServiceException{
            return customerRepo.findById(customerID).orElseThrow(()->new ClientServiceException("the customer not exist"));
    }
}
