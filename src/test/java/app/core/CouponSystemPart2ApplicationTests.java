package app.core;

import app.core.entites.Category;
import app.core.entites.Company;
import app.core.entites.Customer;
import app.core.exeptions.ClientServiceException;
import app.core.exeptions.LoginException;
import app.core.login.ClientType;
import app.core.login.LoginManager;
import app.core.services.AdminService;
import app.core.services.CompanyService;
import app.core.services.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class CouponSystemPart2ApplicationTests {
	@Autowired
	private LoginManager loginManager;

	@Test
	@Order(1)
	void adminTest() {
		System.out.println("--------------------- ADMIN TEST ------------------------");
		RuntimeException e = Assertions.assertThrows(LoginException.class,()->loginManager.login("alex","5555",ClientType.ADMINISTRATOR));//wrong email and password
		System.out.println(e);
		 e = Assertions.assertThrows(LoginException.class,()->loginManager.login("admin@admin.com","admin",ClientType.CUSTOMER));//wrong client type
		System.out.println(e);
		 e = Assertions.assertThrows(LoginException.class,()->loginManager.login("admin@admin.com","5555",ClientType.ADMINISTRATOR));// wrong password
		System.out.println(e);
		AdminService adminService = (AdminService) loginManager.login("admin@admin.com", "admin", ClientType.ADMINISTRATOR);//correct email and password

		adminService.addCompany(new Company(0,"Intel","Intel@gmail.com","123456",null));//add company
        adminService.addCompany(new Company(0,"IBM","IBM@gmail.com","8520",null));//add company
        adminService.addCompany(new Company(0,"Microsoft","Microsoft@gmail.com","0000",null));//add company
        adminService.addCompany(new Company(0,"Monday","Monday@gmail.com","8888",null));//add company
		Company company1=adminService.addCompany(new Company(0,"Asus","Asus@gmail.com","3030",null));//add company
		Company company2 =new Company(0,"Microsoft","","",null);//same name with company no 3
		Company company3 =new Company(0,"","Asus@gmail.com","",null);//same email with company 5

		e = Assertions.assertThrows(ClientServiceException.class,()->adminService.addCompany(company1));//add same company
		System.out.println(e);
		e = Assertions.assertThrows(ClientServiceException.class,()->adminService.addCompany(company2));
		System.out.println(e);
		e = Assertions.assertThrows(ClientServiceException.class,()->adminService.addCompany(company3));
		System.out.println(e);
		Assertions.assertEquals(5,company1.getId(),"no Equals");//check the id given from database
		e = Assertions.assertThrows(ClientServiceException.class,()->adminService.updateCompany(new Company(2,"Asus","Asus@gmail.com","3030",null)));//try update company name
		System.out.println(e);
		adminService.updateCompany(new Company(5,"Asus","Asus@gmail.com","UpdatePass",null));
		e = Assertions.assertThrows(ClientServiceException.class,()->adminService.deleteCompany(8));//try to delete not exist company
		System.out.println(e);
		adminService.deleteCompany(1);
		e = Assertions.assertThrows(ClientServiceException.class,()->adminService.getOneCompany(1));//try to get not exist company
		System.out.println(e);

		System.out.println("\nGet All Companies: " + adminService.getAllCompanies() );
		System.out.println("Get Company number 3: " + adminService.getOneCompany(3) );
		System.out.println();
		adminService.addCustomer(new Customer(0,"Alex","Levi","alexLevi@gmail.com","794613",null));
		adminService.addCustomer(new Customer(0,"Moshe","Cohen","MosheCohen@gmail.com",".....",null));
		adminService.addCustomer(new Customer(0,"Valeria","kavinovich","ValeriaK@gmail.com","10.05.1995",null));
		adminService.addCustomer(new Customer(0,"Avi","Golomov","aviGolomov@gmail.com","$$$5$$$",null));
		adminService.addCustomer(new Customer(0,"Brian","bot","brianBot@gmail.com","abcdefg",null));
		Customer customer1=adminService.addCustomer(new Customer(0,"Maria","Kavaliova","MK@gmail.com","mk111",null));
		Customer customer2=new Customer(0,"Sami","Aria","MK@gmail.com","8989",null);
		e = Assertions.assertThrows(ClientServiceException.class,()->adminService.addCustomer(customer2));//try to add customer with same email
		System.out.println(e);
		Assertions.assertEquals(6,customer1.getId(),"no Equals");//check the id given from database
		customer2.setId(4);
		adminService.updateCustomer(customer2);//update customer 4
		e = Assertions.assertThrows(ClientServiceException.class,()->adminService.deleteCustomer(8));//try to delete customer das not exist
		System.out.println(e);
		adminService.deleteCustomer(5);
		System.out.println("Get All customers: " + adminService.getAllCustomers() );
		System.out.println("Get customer number 1: " + adminService.getOneCustomer(1));
		System.out.println();
	}

	//@Test
	//@Order(2)
	void companyTest() {
		System.out.println("--------------------- COMPANY TEST ------------------------");
		CompanyService companyService = (CompanyService) loginManager.login("Microsoft@gmail.com", "0000", ClientType.COMPANY);
		System.out.println("Company 3 details: " + companyService.getCompanyDetails());
		System.out.println("the coupons of company 3: " + companyService.companyCoupons());
		System.out.println("the coupons of company 3 in Camping: " + companyService.companyCoupons(Category.CAMPING));
		System.out.println("the coupons of company 3 under 50$: " + companyService.companyCoupons(50));
	}

	//@Test
	//@Order(3)
	void customerTest() {
		System.out.println("--------------------- CUSTOMER TEST ------------------------");
		CustomerService customerService = (CustomerService) loginManager.login("brianBot@gmail.com", "abcdefg", ClientType.CUSTOMER);
		System.out.println("All customer coupons: " + customerService.getCustomerCoupons());
		System.out.println("Customer coupons in Sport: " + customerService.getCustomerCoupons(Category.SPORT));
		System.out.println("Customer coupons under 45$: " + customerService.getCustomerCoupons(45));
		System.out.println("customer details: " + customerService.getCustomerDetails());
		try {
			TimeUnit.SECONDS.sleep(15);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}


//	@Autowired
//	private DictionaryServies dictionaryServies;
//	@Test
//	@Order(1)
//	void testAddEntry() {
//		System.out.println("hello junit test");
//		List<Example> examples=new ArrayList<>();
//
//		examples.add(new Example(0,"catty cat",null));
//		examples.add(new Example(0,"aaa",null));
//		examples.add(new Example(0,"bbb",null));
//		Entry entry=new Entry(0,"cat","a animal",examples);
//
//		Entry entry1=dictionaryServies.addEntry(entry);
//
//		Assertions.assertEquals(1,entry1.getId(),"no Equals");
//
//		RuntimeException e = Assertions.assertThrows(RuntimeException.class,()->dictionaryServies.addEntry(entry));
//		System.out.println(e);
//	}
//	@Test
//	@Order(2)
//	void test2AddEntry() {
//		Entry entry = dictionaryServies.getEntry(1);
////Entry entry2 = dictionaryServies.getEntry(2);
//		Assertions.assertNotNull(entry);
////Assertions.assertNotNull(entry2);
//		Assertions.assertEquals("cat",entry.getWord());
//		RuntimeException e = Assertions.assertThrows(RuntimeException.class,()->dictionaryServies.getEntry(2));
//		System.out.println(e);
//	}



	//        companyService.addCoupon(new Coupon(0,0,Category.CAMPING,"aaa","+++",LocalDate.of(2022,12,05),LocalDate.of(2023,12,25),20,10,"img",null));
//        companyService.addCoupon(new Coupon(0,0,Category.CLOTHING,"bbb","+++",LocalDate.of(2022,12,05),LocalDate.of(2023,12,25),20,100,"img",null));
//        companyService.addCoupon(new Coupon(0,0,Category.ELECTRICITY,"ccc","+++",LocalDate.of(2022,12,05),LocalDate.of(2023,12,25),20,61,"img",null));
//        companyService=(CompanyService) loginManager.login("Monday@gmail.com","8888",ClientType.COMPANY);
//
//        //CompanyService companyService=(CompanyService) loginManager.login("Microsoft@gmail.com","0000",ClientType.COMPANY);
//        //companyService.deleteCoupon(1);
//
//        System.out.println("All customer coupons: "+customerService.getCustomerCoupons());
//        customerService.purchase(1);
//        customerService.purchase(4);
//        customerService.purchase(6);
//        companyService.addCoupon(new Coupon(0,0,Category.SPORT,"ddd","+++",LocalDate.of(2022,12,05),LocalDate.of(2023,12,05),20,80,"img",null));
//        companyService.addCoupon(new Coupon(0,0,Category.CAMPING,"eee","+++",LocalDate.of(2022,12,05),LocalDate.of(2023,12,25),20,200,"img",null));
//        companyService.addCoupon(new Coupon(0,0,Category.SPORT,"fff","+++",LocalDate.of(2022,12,05),LocalDate.of(2023,12,25),20,30,"img",null));

	// companyService.updateCoupon(new Coupon(2,0,Category.ELECTRICITY,"ggg","+++",LocalDate.of(2022,12,05),LocalDate.of(2022,12,25),20,40,"img",null));

	// companyService.deleteCoupon(3);
//
//        adminService.addCustomer(new Customer(0,"Alex","Levi","alexLevi@gmail.com","794613",null));
//        adminService.addCustomer(new Customer(0,"Moshe","Cohen","MosheCohen@gmail.com",".....",null));
//        adminService.addCustomer(new Customer(0,"Valeria","kavinovich","ValeriaK@gmail.com","10.05.1995",null));
//        adminService.addCustomer(new Customer(0,"Avi","Golomov","aviGolomov@gmail.com","$$$5$$$",null));
//        adminService.addCustomer(new Customer(0,"Brian","bot","brianBot@gmail.com","abcdefg",null));
//        adminService.addCustomer(new Customer(0,"Maria","Kavaliova","MK@gmail.com","mk111",null));

	// adminService.updateCustomer(new Customer(4,"Ari","Levi","arilevi@gmail.com","0000",null));

	//  adminService.deleteCustomer(2);
//
//        adminService.addCompany(new Company(0,"Intel","Intel@gmail.com","123456",null));
//        adminService.addCompany(new Company(0,"IBM","IBM@gmail.com","8520",null));
//        adminService.addCompany(new Company(0,"Microsoft","Microsoft@gmail.com","0000",null));
//        adminService.addCompany(new Company(0,"Monday","Monday@gmail.com","8888",null));
//        adminService.addCompany(new Company(0,"Asus","Asus@gmail.com","3030",null));
//        adminService.addCompany(new Company(0,"toDelete","---","---",null));
	// adminService.updateCompany(new Company(5,"Asus","AN@gmail.com","3131",null));
//	System.out.println("\nGet All customers: " + adminService.getAllCustomers() );
//			System.out.println("\nGet customer number 1: " + adminService.getOneCustomer(1));
	// adminService.deleteCompany(6);

