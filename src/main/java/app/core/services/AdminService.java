package app.core.services;

import app.core.entites.Company;
import app.core.entites.Customer;
import app.core.exeptions.ClientServiceException;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Scope("prototype")
public class AdminService extends ClientService {
    public static final String EMAIL = "admin@admin.com";
    public static final String PASSWORD = "admin";

    public boolean login(String email, String password) {
        return (EMAIL.equals(email) && PASSWORD.equals(password));
    }


    public Company addCompany(Company company) throws ClientServiceException {
        if(!companyRepo.existsByEmailOrName(company.getEmail(), company.getName())){
            return companyRepo.save(company);
        }else{
            throw new ClientServiceException("the company: ("+company.getName()+ ","+company.getEmail()+ ") already exist");
        }
    }


    public Company updateCompany(Company company) throws ClientServiceException {
        Company company1 = companyRepo.findById(company.getId()).orElseThrow(()->new ClientServiceException("the company: "+company.getId()+" already exist"));
        if(Objects.equals(company1.getName(), company.getName())){
           return companyRepo.save(company);
        }else{
            throw new ClientServiceException("the companies name should by same("+company.getName()+" != "+company1.getName()+")");
        }
    }


    public void deleteCompany(int companyID) throws ClientServiceException{
            companyRepo.delete(companyRepo.findById(companyID).orElseThrow(()->new ClientServiceException("can't delete: the company: "+companyID+" dose not exist")));
    }


    public List<Company> getAllCompanies(){
        return companyRepo.findAll();
    }


    public Company getOneCompany(int companyID)throws ClientServiceException{
            return companyRepo.findById(companyID).orElseThrow(()->new ClientServiceException("the company: "+companyID+" dose not exist"));
    }

    public Customer addCustomer(Customer customer) throws ClientServiceException {
        if(!customerRepo.existsByEmail(customer.getEmail())){
           return customerRepo.save(customer);
        }else{
            throw new ClientServiceException("the customer: "+customer.getId()+" already exist");
        }
    }

    public Customer updateCustomer(Customer customer) throws ClientServiceException {
        customerRepo.findById(customer.getId()).orElseThrow(()->new ClientServiceException("the customer: "+customer.getId()+" dose not exist"));
        return customerRepo.save(customer);
    }

    public void deleteCustomer(int customerID) throws ClientServiceException{
            customerRepo.delete(customerRepo.findById(customerID).orElseThrow(()->new ClientServiceException("the customer: "+customerID+" dose not exist")));
    }

    public List<Customer> getAllCustomers(){
        return customerRepo.findAll();
    }

    public Customer getOneCustomer(int customerID) throws ClientServiceException{
            return customerRepo.findById(customerID).orElseThrow(()->new ClientServiceException("the customer: "+customerID+" dose not exist"));
    }

}
