package app.core.services;

import app.core.entites.Company;
import app.core.entites.Customer;
import app.core.exeptions.ClientServiceException;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
/**
 * Service class that handles operations related to admin functionality
 *
 * @author admin
 */
@Service
@Transactional
@Scope("prototype")
public class AdminService extends ClientService {
    public static final String EMAIL = "admin@admin.com";
    public static final String PASSWORD = "admin";
    /**
     *
     * @param email the email of the admin
     * @param password the password of the admin
     * @return true if the email and password match the predefined admin credentials, false otherwise
     */
    public boolean login(String email, String password) {
        return (EMAIL.equals(email) && PASSWORD.equals(password));
    }

    /**
     * Adds a new company to the database
     * @param company the new company to be added
     * @return the added company
     * @throws ClientServiceException if a company with the same email or name already exists in the database
     */
    public Company addCompany(Company company) throws ClientServiceException {
        if(!companyRepo.existsByEmailOrName(company.getEmail(), company.getName())){
            return companyRepo.save(company);
        }else{
            throw new ClientServiceException("the company: ("+company.getName()+ ","+company.getEmail()+ ") already exist");
        }
    }

    /**
     * Updates an existing company in the database.
     * @param company the updated company
     * @return the updated company
     * @throws ClientServiceException if the company to be updated is not found in the database or if the name of the company to be updated is different from the name of the existing company
     */
    public Company updateCompany(Company company) throws ClientServiceException {
        Company company1 = companyRepo.findById(company.getId()).orElseThrow(()->new ClientServiceException("the company: "+company.getId()+" already exist"));
        if(Objects.equals(company1.getName(), company.getName())){
           return companyRepo.save(company);
        }else{
            throw new ClientServiceException("the companies name should by same("+company.getName()+" != "+company1.getName()+")");
        }
    }

    /**
     * Deletes a company from the database
     * @param companyID the id of the company to be deleted
     * @throws ClientServiceException if the company does not exist in the database
     */
    public void deleteCompany(int companyID) throws ClientServiceException{
            companyRepo.delete(companyRepo.findById(companyID).orElseThrow(()->new ClientServiceException("can't delete: the company: "+companyID+" dose not exist")));
    }

    /**
     * Get all the companies in the database
     * @return a list of all the companies in the database
     */
    public List<Company> getAllCompanies(){
        return companyRepo.findAll();
    }

    /**
     * Get a company by id
     * @param companyID the id of the company to be retrieved
     * @throws ClientServiceException if the company does not exist in the database
     * @return the company with the specified id
     */
    public Company getOneCompany(int companyID)throws ClientServiceException{
            return companyRepo.findById(companyID).orElseThrow(()->new ClientServiceException("the company: "+companyID+" dose not exist"));
    }
    /**
     * Adds a new customer to the database
     * @param customer the new customer to be added
     * @return the added customer
     * @throws ClientServiceException if a customer with the same email already exists in the database
     */
    public Customer addCustomer(Customer customer) throws ClientServiceException {
        if(!customerRepo.existsByEmail(customer.getEmail())){
           return customerRepo.save(customer);
        }else{
            throw new ClientServiceException("the customer: "+customer.getId()+" already exist");
        }
    }
    /**
     * Updates an existing customer in the database.
     * @param customer the updated customer
     * @return the updated customer
     * @throws ClientServiceException if the customer to be updated is not found in the database
     */
    public Customer updateCustomer(Customer customer) throws ClientServiceException {
        customerRepo.findById(customer.getId()).orElseThrow(()->new ClientServiceException("the customer: "+customer.getId()+" dose not exist"));
        return customerRepo.save(customer);
    }
    /**
     * Deletes a customer from the database
     * @param customerID the id of the customer to be deleted
     * @throws ClientServiceException if the customer does not exist in the database
     */
    public void deleteCustomer(int customerID) throws ClientServiceException{
            customerRepo.delete(customerRepo.findById(customerID).orElseThrow(()->new ClientServiceException("the customer: "+customerID+" dose not exist")));
    }
    /**
     * Get all the customers in the database
     * @return a list of all the customers in the database
     */
    public List<Customer> getAllCustomers(){
        return customerRepo.findAll();
    }
    /**
     * Get a customer by id
     * @param customerID the id of the customer to be retrieved
     * @throws ClientServiceException if the customer does not exist in the database
     * @return the customer with the specified id
     */
    public Customer getOneCustomer(int customerID) throws ClientServiceException{
            return customerRepo.findById(customerID).orElseThrow(()->new ClientServiceException("the customer: "+customerID+" dose not exist"));
    }

}
