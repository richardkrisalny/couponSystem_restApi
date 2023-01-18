package app.core;
import app.core.entites.Category;
import app.core.entites.Company;
import app.core.entites.Coupon;
import app.core.entites.Customer;
import app.core.exeptions.ClientServiceException;
import app.core.exeptions.LoginException;
import app.core.login.ClientType;
import app.core.login.LoginManager;
import app.core.services.AdminService;
import app.core.services.CompanyService;
import app.core.services.CustomerService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CouponSystemPart2ApplicationTests {
	@Autowired
	private LoginManager loginManager;
	@Test
	@Order(1)
	void adminTest() {
		//Try logging in with an incorrect email and password
		RuntimeException e = Assertions.assertThrows(LoginException.class, () -> loginManager.login("alex", "5555", ClientType.ADMINISTRATOR));
		System.out.println(e);
		//Try logging in with an incorrect client type
		e = Assertions.assertThrows(LoginException.class, () -> loginManager.login("admin@admin.com", "admin", ClientType.CUSTOMER));
		System.out.println(e);
		//Try logging in with an incorrect password
		e = Assertions.assertThrows(LoginException.class, () -> loginManager.login("admin@admin.com", "5555", ClientType.ADMINISTRATOR));
		System.out.println(e);
		//logging in
		AdminService adminService = (AdminService) loginManager.login("admin@admin.com", "admin", ClientType.ADMINISTRATOR);
		//check if logging in successful
		Assertions.assertNotNull(adminService);
		//add companies
		adminService.addCompany(new Company(0, "Intel", "Intel@gmail.com", "123456", null));//1
		adminService.addCompany(new Company(0, "IBM", "IBM@gmail.com", "8520", null));//2
		adminService.addCompany(new Company(0, "Microsoft", "Microsoft@gmail.com", "0000", null));//3
		adminService.addCompany(new Company(0, "Monday", "Monday@gmail.com", "8888", null));//4
		Company company5 = adminService.addCompany(new Company(0, "Asus", "Asus@gmail.com", "3030", null));//5
		Company company6 = new Company(0, "Microsoft", "test", "", null);// 6 (same name with company 3)
		Company company7 = new Company(0, "test", "Asus@gmail.com", "", null);// 7 (same email with company 5)
		//try to add company 5 twice
		e = Assertions.assertThrows(ClientServiceException.class, () -> adminService.addCompany(company5));
		System.out.println(e);
		//try to add company 6 (same name with company 3)
		e = Assertions.assertThrows(ClientServiceException.class, () -> adminService.addCompany(company6));
		System.out.println(e);
		//try to add company 7 (same email with company 5)
		e = Assertions.assertThrows(ClientServiceException.class, () -> adminService.addCompany(company7));
		System.out.println(e);
		//check the id given from database
		Assertions.assertEquals(5, company5.getId(), "no Equals");
		//try to update name of company 2
		e = Assertions.assertThrows(ClientServiceException.class, () -> adminService.updateCompany(new Company(2, "Asus", "Asus@gmail.com", "3030", null)));
		System.out.println(e);
		//update password of company 5
		adminService.updateCompany(new Company(5, "Asus", "Asus@gmail.com", "UpdatePass", null));
		//check if company 5 updated
		Assertions.assertEquals("UpdatePass", adminService.getOneCompany(5).getPassword(), "no Equals");
		//try to delete not exist company
		e = Assertions.assertThrows(ClientServiceException.class, () -> adminService.deleteCompany(8));
		System.out.println(e);
		//delete company 1
		adminService.deleteCompany(1);
		//check if company 1 deleted
		e = Assertions.assertThrows(ClientServiceException.class, () -> adminService.getOneCompany(1));
		System.out.println(e);
		//add customers...
		adminService.addCustomer(new Customer(0, "Alex", "Levi", "alexLevi@gmail.com", "794613", null));//1
		adminService.addCustomer(new Customer(0, "Moshe", "Cohen", "MosheCohen@gmail.com", ".....", null));//2
		adminService.addCustomer(new Customer(0, "Valeria", "kavinovich", "ValeriaK@gmail.com", "10.05.1995", null));//3
		adminService.addCustomer(new Customer(0, "Avi", "Golomov", "aviGolomov@gmail.com", "$$$5$$$", null));//4
		adminService.addCustomer(new Customer(0, "Brian", "bot", "brianBot@gmail.com", "abcdefg", null));//5
		Customer customer6 = adminService.addCustomer(new Customer(0, "Maria", "Kavaliova", "MK@gmail.com", "mk111", null));//6
		Customer customer7 = new Customer(0, "Sami", "Aria", "MK@gmail.com", "8989", null);//7
		//try to add customer with same email 6-7 (MK@gmail.com)
		e = Assertions.assertThrows(ClientServiceException.class, () -> adminService.addCustomer(customer7));
		System.out.println(e);
		//check the id given from database to customer 7
		Assertions.assertEquals(6, customer6.getId(), "no Equals");
		//update customer 4
		customer7.setId(4);
		adminService.updateCustomer(customer7);
		//check if customer 4 updated
		Assertions.assertEquals("Aria", adminService.getOneCustomer(4).getLastName(), "no Equals");
		//try to delete customer 8  (not exist)
		e = Assertions.assertThrows(ClientServiceException.class, () -> adminService.deleteCustomer(8));
		System.out.println(e);
		//delete customer 5
		adminService.deleteCustomer(5);
		//check if customer 5 deleted
		e = Assertions.assertThrows(ClientServiceException.class, () -> adminService.getOneCustomer(5));
		System.out.println(e);
		//check the get methods
		Assertions.assertNotNull(adminService.getAllCompanies());
		Assertions.assertNotNull(adminService.getAllCustomers());
	}

	@Test
	@Order(2)
	void companyTest() {
		//Try logging in with wrong email and password
		RuntimeException e = Assertions.assertThrows(LoginException.class, () -> loginManager.login("alex", "5555", ClientType.COMPANY));
		System.out.println(e);
		//Try logging in with wrong client type
		e = Assertions.assertThrows(LoginException.class, () -> loginManager.login("IBM@gmail.com", "8520", ClientType.CUSTOMER));
		System.out.println(e);
		//Try logging in with wrong password
		e = Assertions.assertThrows(LoginException.class, () -> loginManager.login("IBM@gmail.com", "5555", ClientType.ADMINISTRATOR));
		System.out.println(e);
		//correct email and password
		CompanyService companyService2 = (CompanyService) loginManager.login("IBM@gmail.com", "8520", ClientType.COMPANY);
		//check the login company 2
		Assertions.assertNotNull(companyService2);
		Assertions.assertEquals(2, companyService2.getCompanyDetails().getId(), "no Equals");
		//add coupon 1 to company 2
		companyService2.addCoupon(new Coupon(0, 0, Category.CAMPING, "aaa", "+++", LocalDate.of(2022, 12, 05), LocalDate.of(2023, 12, 25), 20, 10, "img", null));//1
		//add coupon 2 to company 2
		Coupon coupon2 = companyService2.addCoupon(new Coupon(0, 0, Category.CLOTHING, "bbb", "+++", LocalDate.of(2022, 12, 05), LocalDate.of(2023, 12, 25), 20, 100, "img", null));
		//try to add same coupon twice
		e = Assertions.assertThrows(ClientServiceException.class, () -> companyService2.addCoupon(coupon2));
		System.out.println(e);
		//add coupon 3 to company 2
		Coupon coupon3 = companyService2.addCoupon(new Coupon(0, 0, Category.ELECTRICITY, "ccc", "+++", LocalDate.of(2022, 12, 05), LocalDate.of(2023, 12, 25), 20, 61, "img", null));
		//check if coupon 3 got correct company id (2)
		Assertions.assertEquals(2, coupon3.getCompanyID(), "no Equals");
		//login with company 4
		 CompanyService companyService4 = (CompanyService) loginManager.login("Monday@gmail.com", "8888", ClientType.COMPANY);
		//check company 4 login
		Assertions.assertNotNull(companyService4);
		Assertions.assertEquals(4, companyService4.getCompanyDetails().getId(), "no Equals");
		//add coupons 4,5,6,7 to company 4
		companyService4.addCoupon(new Coupon(0, 0, Category.CAMPING, "aaa", "+++", LocalDate.of(2022, 12, 05), LocalDate.of(2023, 12, 25), 20, 10, "img", null));
		companyService4.addCoupon(new Coupon(0, 0, Category.CAMPING, "test", "+++", LocalDate.of(2022, 12, 05), LocalDate.of(2023, 12, 25), 1, 10, "img", null));
		companyService4.addCoupon(new Coupon(0, 0, Category.CLOTHING, "bbb", "+++", LocalDate.of(2022, 12, 05), LocalDate.of(2022, 12, 25), 20, 100, "img", null));
		Coupon coupon7 = companyService4.addCoupon(new Coupon(0, 0, Category.ELECTRICITY, "ccc", "+++", LocalDate.of(2022, 12, 05), LocalDate.of(2023, 12, 25), 20, 61, "img", null));
		//check if the coupon 7 got company id 4
		Assertions.assertEquals(4, coupon7.getCompanyID(), "no Equals");
		//try to update not exists coupon
		e = Assertions.assertThrows(ClientServiceException.class, () -> companyService4.updateCoupon(new Coupon(8, 0, Category.CLOTHING, "ttt", "---", LocalDate.of(2022, 12, 05), LocalDate.of(2023, 12, 25), 20, 100, "img", null)));
		System.out.println(e);
		//try to update coupon to another company
		e = Assertions.assertThrows(ClientServiceException.class, () -> companyService4.updateCoupon(new Coupon(3, 0, Category.CLOTHING, "ttt", "---", LocalDate.of(2022, 12, 05), LocalDate.of(2023, 12, 25), 20, 100, "img", null)));
		System.out.println(e);
		//try to delete coupon to another company
		e = Assertions.assertThrows(ClientServiceException.class, () -> companyService4.deleteCoupon(3));
		System.out.println(e);
		//update coupon 4
		companyService4.updateCoupon(new Coupon(4, 0, Category.CAMPING, "ttt", "---", LocalDate.of(2022, 12, 05), LocalDate.of(2023, 12, 25), 20, 100, "img", null));
		//check the updates
		Assertions.assertEquals("ttt", companyService4.companyCoupons().get(0).getTitle(), "no Equals");
		//check get methods
		Assertions.assertEquals(2, companyService4.companyCoupons(Category.CAMPING).size(), "no Equals");
		Assertions.assertEquals(2, companyService4.companyCoupons(80).size(), "no Equals");
		Assertions.assertEquals(4, companyService4.companyCoupons().size(), "no Equals");
		//check if the services not mixed up
		Coupon couponTest = new Coupon(0, 0, Category.CAMPING, "ttt", "---", LocalDate.of(2022, 12, 05), LocalDate.of(2023, 12, 25), 20, 100, "img", null);
		companyService2.addCoupon(couponTest);
		Assertions.assertEquals(2, couponTest.getCompanyID(), "no Equals");
		companyService2.deleteCoupon(8);
	}

	@Test
	@Order(3)
	void customerTest() {
		//Try logging in with wrong email and password
		RuntimeException e = Assertions.assertThrows(LoginException.class, () -> loginManager.login("alex", "5555", ClientType.CUSTOMER));
		System.out.println(e);
		//Try logging in with wrong client type
		e = Assertions.assertThrows(LoginException.class, () -> loginManager.login("alexLevi@gmail.com", "794613", ClientType.ADMINISTRATOR));
		System.out.println(e);
		//login customer 1
		CustomerService customerService1 = (CustomerService) loginManager.login("alexLevi@gmail.com", "794613", ClientType.CUSTOMER);
		//check the login
		Assertions.assertNotNull(customerService1);
		Assertions.assertEquals(1, customerService1.getCustomerDetails().getId(), "no Equals");
		//costumer 1 purchase the coupons 1,5,3,4,7
		customerService1.purchase(1);
		customerService1.purchase(5);
		customerService1.purchase(3);
		customerService1.purchase(4);
		customerService1.purchase(7);
		//try to purchase expired coupon
		e = Assertions.assertThrows(ClientServiceException.class, () -> customerService1.purchase(6));//date
		System.out.println(e);
		//try to purchase not exists coupon
		e = Assertions.assertThrows(ClientServiceException.class, () -> customerService1.purchase(8));
		System.out.println(e);
		//login customer 2
		CustomerService customerService2 = (CustomerService) loginManager.login("MosheCohen@gmail.com", ".....", ClientType.CUSTOMER);
		//check the login
		Assertions.assertNotNull(customerService2);
		Assertions.assertEquals(2, customerService2.getCustomerDetails().getId(), "no Equals");
		//costumer 1 purchase the coupons 1,3,4,7
		customerService2.purchase(1);
		customerService2.purchase(3);
		customerService2.purchase(4);
		customerService2.purchase(7);
		//try to purchase coupon with 0 amount
		e = Assertions.assertThrows(ClientServiceException.class, () -> customerService2.purchase(5));
		System.out.println(e);
		//try to purchase coupon 1 twice
		e = Assertions.assertThrows(ClientServiceException.class, () -> customerService2.purchase(1));
		System.out.println(e);
		//check if the coupons purchased and The amount has decreased
		Assertions.assertEquals(18, customerService2.getCustomerCoupons().get(0).getAmount(), "no Equals");
		Assertions.assertEquals(2, customerService2.getCustomerCoupons(Category.CAMPING).size(), "no Equals");
		Assertions.assertEquals(1, customerService2.getCustomerCoupons(45).size(), "no Equals");

	}
	@Test
	@Order(4)
	void dependentTest() {
		//customer 2 login
		CustomerService customerService2 = (CustomerService) loginManager.login("MosheCohen@gmail.com", ".....", ClientType.CUSTOMER);
		//check the login
		Assertions.assertNotNull(customerService2);
		Assertions.assertEquals(2, customerService2.getCustomerDetails().getId(), "no Equals");
		//company 2 login
		CompanyService companyService2 = (CompanyService) loginManager.login("IBM@gmail.com", "8520", ClientType.COMPANY);
		//check the login
		Assertions.assertNotNull(companyService2);
		//check amount of coupons customer 2 have
		Assertions.assertEquals(4, customerService2.getCustomerCoupons().size(), "no Equals");
		//company 2 try to delete company 1s coupon
		RuntimeException e = Assertions.assertThrows(ClientServiceException.class, () -> companyService2.deleteCoupon(4));
		System.out.println(e);
		//company 2 update coupon 3 (title = test)
		companyService2.updateCoupon(new Coupon(3, 0, Category.CAMPING, "test", "+++", LocalDate.of(2022, 12, 05), LocalDate.of(2023, 12, 25), 1, 10, "img", null));
		//check the update in customer 2 coupons
		Assertions.assertEquals("test",customerService2.getCustomerCoupons().get(1).getTitle(), "no Equals");
		Assertions.assertEquals(4, customerService2.getCustomerCoupons().size(), "no Equals");
		Assertions.assertEquals(3, customerService2.getCustomerCoupons(Category.CAMPING).size(), "no Equals");
		//company 2 delete coupon 3 (Category CAMPING)
		companyService2.deleteCoupon(3);
		//check if the coupon 3 deleted from customer 2
		Assertions.assertEquals(3, customerService2.getCustomerCoupons().size(), "no Equals");
		Assertions.assertEquals(2, customerService2.getCustomerCoupons(Category.CAMPING).size(), "no Equals");
		//login admin
		System.out.println(companyService2.companyCoupons());
		AdminService adminService = (AdminService) loginManager.login("admin@admin.com", "admin", ClientType.ADMINISTRATOR);
		Assertions.assertNotNull(adminService);
		//delete company 2 (and all coupons)
		adminService.deleteCompany(2);
		//check if the coupons deleted from customer 2
		Assertions.assertEquals(1, customerService2.getCustomerCoupons(Category.CAMPING).size(), "no Equals");
		Assertions.assertEquals(2, customerService2.getCustomerCoupons().size(), "no Equals");
		//check if company 2 deleted
		e = Assertions.assertThrows(LoginException.class, () -> loginManager.login("IBM@gmail.com", "8520", ClientType.COMPANY));
		System.out.println(e);
		Assertions.assertEquals(0, companyService2.companyCoupons().size(), "no Equals");
	}
	@Test
	@Order(5)
	void jobTest() {
		CompanyService companyService = (CompanyService) loginManager.login("Monday@gmail.com", "8888", ClientType.COMPANY);
		Assertions.assertNotNull(companyService);
		//check haw many coupons has
		Assertions.assertEquals(4, companyService.companyCoupons().size(), "no Equals");
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		companyService.addCoupon(new Coupon(0, 0, Category.CAMPING, "a", "a", LocalDate.of(2022, 12, 5), LocalDate.of(2023, 1, 17), 1, 10, "img", null));
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		//check if the jod deleted coupons 6,9 (Expired)
		Assertions.assertEquals(3, companyService.companyCoupons().size(), "no Equals");
	}
}

