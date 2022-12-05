package app.core;

import app.core.entites.Company;
import app.core.entites.Customer;
import app.core.services.AdminService;
import app.core.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Test implements CommandLineRunner {
    @Autowired
    private AdminService adminService;
    @Autowired
    private CompanyService companyService;
    @Override
    public void run(String... args) throws Exception {
        System.out.println(adminService.login("admin@admin.com","admin"));

        adminService.addCompany(new Company(0,"aaa","aaa@gmail.com","123456",null));
        adminService.addCompany(new Company(0,"bbb","bbb@gmail.com","qwer",null));
        adminService.addCompany(new Company(0,"ccc","ccc@gmail.com","asdf",null));
        adminService.addCompany(new Company(0,"ddd","ddd@gmail.com","zxcv",null));
        adminService.addCompany(new Company(0,"eee","eee@gmail.com","0000",null));

        adminService.updateCompany(new Company(3,"ccc","richard","88888",null));

        adminService.deleteCompany(1);

        System.out.println("----------------------------------------\n\n");
        System.out.println(adminService.getAllCompanies());

        System.out.println("----------------------------------------\n\n");
        System.out.println(adminService.getOneCompany(3));
        System.out.println("----------------------------------------\n\n");

        adminService.addCustomer(new Customer(0,"richard","krisalny","richard@ggg","8520145",null));
        adminService.addCustomer(new Customer(0,"qqq","fff","uu@ggg","8520145",null));
        adminService.addCustomer(new Customer(0,"kkk","ppp","ppp@ggg","fgf",null));

        adminService.updateCustomer(new Customer(2,"????","????","????","8520145",null));

        adminService.deleteCustomer(2);

        System.out.println("----------------------------------------\n\n");
        System.out.println(adminService.getAllCustomers());
        System.out.println("----------------------------------------\n\n");
        System.out.println(adminService.getOneCustomer(1));
        System.out.println("----------------------------------------\n\n");



    }
}
