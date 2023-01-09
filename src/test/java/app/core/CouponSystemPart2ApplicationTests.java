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
		System.out.println("--------------------- ADMIN TEST ------------------------");
		RuntimeException e = Assertions.assertThrows(LoginException.class, () -> loginManager.login("alex", "5555", ClientType.ADMINISTRATOR));//wrong email and password
		System.out.println(e);
		e = Assertions.assertThrows(LoginException.class, () -> loginManager.login("admin@admin.com", "admin", ClientType.CUSTOMER));//wrong client type
		System.out.println(e);
		e = Assertions.assertThrows(LoginException.class, () -> loginManager.login("admin@admin.com", "5555", ClientType.ADMINISTRATOR));// wrong password
		System.out.println(e);
		AdminService adminService = (AdminService) loginManager.login("admin@admin.com", "admin", ClientType.ADMINISTRATOR);//correct email and password
		Assertions.assertNotNull(adminService);
		adminService.addCompany(new Company(0, "Intel", "Intel@gmail.com", "123456", null));//add company
		adminService.addCompany(new Company(0, "IBM", "IBM@gmail.com", "8520", null));//add company
		adminService.addCompany(new Company(0, "Microsoft", "Microsoft@gmail.com", "0000", null));//add company
		adminService.addCompany(new Company(0, "Monday", "Monday@gmail.com", "8888", null));//add company
		Company company1 = adminService.addCompany(new Company(0, "Asus", "Asus@gmail.com", "3030", null));//add company
		Company company2 = new Company(0, "Microsoft", "", "", null);//same name with company no 3
		Company company3 = new Company(0, "", "Asus@gmail.com", "", null);//same email with company 5

		e = Assertions.assertThrows(ClientServiceException.class, () -> adminService.addCompany(company1));//add same company
		System.out.println(e);
		e = Assertions.assertThrows(ClientServiceException.class, () -> adminService.addCompany(company2));
		System.out.println(e);
		e = Assertions.assertThrows(ClientServiceException.class, () -> adminService.addCompany(company3));
		System.out.println(e);
		Assertions.assertEquals(5, company1.getId(), "no Equals");//check the id given from database
		e = Assertions.assertThrows(ClientServiceException.class, () -> adminService.updateCompany(new Company(2, "Asus", "Asus@gmail.com", "3030", null)));//try update company name
		System.out.println(e);
		adminService.updateCompany(new Company(5, "Asus", "Asus@gmail.com", "UpdatePass", null));
		e = Assertions.assertThrows(ClientServiceException.class, () -> adminService.deleteCompany(8));//try to delete not exist company
		System.out.println(e);
		adminService.deleteCompany(1);
		e = Assertions.assertThrows(ClientServiceException.class, () -> adminService.getOneCompany(1));//try to get not exist company
		System.out.println(e);

//		System.out.println("\nGet All Companies: " + adminService.getAllCompanies());
//		System.out.println("Get Company number 3: " + adminService.getOneCompany(3));
//		System.out.println();

		adminService.addCustomer(new Customer(0, "Alex", "Levi", "alexLevi@gmail.com", "794613", null));
		adminService.addCustomer(new Customer(0, "Moshe", "Cohen", "MosheCohen@gmail.com", ".....", null));
		adminService.addCustomer(new Customer(0, "Valeria", "kavinovich", "ValeriaK@gmail.com", "10.05.1995", null));
		adminService.addCustomer(new Customer(0, "Avi", "Golomov", "aviGolomov@gmail.com", "$$$5$$$", null));
		adminService.addCustomer(new Customer(0, "Brian", "bot", "brianBot@gmail.com", "abcdefg", null));
		Customer customer1 = adminService.addCustomer(new Customer(0, "Maria", "Kavaliova", "MK@gmail.com", "mk111", null));
		Customer customer2 = new Customer(0, "Sami", "Aria", "MK@gmail.com", "8989", null);
		e = Assertions.assertThrows(ClientServiceException.class, () -> adminService.addCustomer(customer2));//try to add customer with same email
		System.out.println(e);
		Assertions.assertEquals(6, customer1.getId(), "no Equals");//check the id given from database
		customer2.setId(4);
		adminService.updateCustomer(customer2);//update customer 4
		e = Assertions.assertThrows(ClientServiceException.class, () -> adminService.deleteCustomer(8));//try to delete customer das not exist
		System.out.println(e);
		adminService.deleteCustomer(5);
//		System.out.println("Get All customers: " + adminService.getAllCustomers());
//		System.out.println("Get customer number 1: " + adminService.getOneCustomer(1));
//		System.out.println();
	}

	@Test
	@Order(2)
	void companyTest() {
		System.out.println("--------------------- COMPANY TEST ------------------------");
		RuntimeException e = Assertions.assertThrows(LoginException.class, () -> loginManager.login("alex", "5555", ClientType.COMPANY));//wrong email and password
		System.out.println(e);
		e = Assertions.assertThrows(LoginException.class, () -> loginManager.login("IBM@gmail.com", "8520", ClientType.CUSTOMER));//wrong client type
		System.out.println(e);
		e = Assertions.assertThrows(LoginException.class, () -> loginManager.login("IBM@gmail.com", "5555", ClientType.ADMINISTRATOR));// wrong password
		System.out.println(e);

		CompanyService companyService = (CompanyService) loginManager.login("IBM@gmail.com", "8520", ClientType.COMPANY);//correct email and password
		Assertions.assertNotNull(companyService);

		companyService.addCoupon(new Coupon(0, 0, Category.CAMPING, "aaa", "+++", LocalDate.of(2022, 12, 05), LocalDate.of(2023, 12, 25), 20, 10, "img", null));
		Coupon coupon1 = companyService.addCoupon(new Coupon(0, 0, Category.CLOTHING, "bbb", "+++", LocalDate.of(2022, 12, 05), LocalDate.of(2023, 12, 25), 20, 100, "img", null));
		CompanyService finalCompanyService = companyService;
		e = Assertions.assertThrows(ClientServiceException.class, () -> finalCompanyService.addCoupon(coupon1));
		System.out.println(e);
		Coupon coupon = companyService.addCoupon(new Coupon(0, 0, Category.ELECTRICITY, "ccc", "+++", LocalDate.of(2022, 12, 05), LocalDate.of(2023, 12, 25), 20, 61, "img", null));
		Assertions.assertEquals(2, coupon.getCompanyID(), "no Equals");

		companyService = (CompanyService) loginManager.login("Monday@gmail.com", "8888", ClientType.COMPANY);
		Assertions.assertNotNull(companyService);
		companyService.addCoupon(new Coupon(0, 0, Category.CAMPING, "aaa", "+++", LocalDate.of(2022, 12, 05), LocalDate.of(2023, 12, 25), 20, 10, "img", null));
		companyService.addCoupon(new Coupon(0, 0, Category.CAMPING, "test", "+++", LocalDate.of(2022, 12, 05), LocalDate.of(2023, 12, 25), 1, 10, "img", null));
		companyService.addCoupon(new Coupon(0, 0, Category.CLOTHING, "bbb", "+++", LocalDate.of(2022, 12, 05), LocalDate.of(2022, 12, 25), 20, 100, "img", null));
		coupon = companyService.addCoupon(new Coupon(0, 0, Category.ELECTRICITY, "ccc", "+++", LocalDate.of(2022, 12, 05), LocalDate.of(2023, 12, 25), 20, 61, "img", null));
		Assertions.assertEquals(4, coupon.getCompanyID(), "no Equals");
		e = Assertions.assertThrows(ClientServiceException.class, () -> finalCompanyService.updateCoupon(new Coupon(8, 0, Category.CLOTHING, "ttt", "---", LocalDate.of(2022, 12, 05), LocalDate.of(2023, 12, 25), 20, 100, "img", null)));
		System.out.println(e);

		e = Assertions.assertThrows(ClientServiceException.class, () -> finalCompanyService.deleteCoupon(4));
		System.out.println(e);

		companyService.updateCoupon(new Coupon(4, 0, Category.CAMPING, "ttt", "---", LocalDate.of(2022, 12, 05), LocalDate.of(2023, 12, 25), 20, 100, "img", null));
//		System.out.println("Company 4 details: " + companyService.getCompanyDetails());
//		System.out.println("the coupons of company 4: " + companyService.companyCoupons());
		Assertions.assertEquals(2, companyService.companyCoupons(Category.CAMPING).size(), "no Equals");
		Assertions.assertEquals(2, companyService.companyCoupons(80).size(), "no Equals");
	}

	@Test
	@Order(3)
	void customerTest() {
		System.out.println("--------------------- CUSTOMER TEST ------------------------");
		RuntimeException e = Assertions.assertThrows(LoginException.class, () -> loginManager.login("alex", "5555", ClientType.CUSTOMER));//wrong email and password
		System.out.println(e);
		 e = Assertions.assertThrows(LoginException.class, () -> loginManager.login("alexLevi@gmail.com", "794613", ClientType.ADMINISTRATOR));//wrong email and password
		System.out.println(e);
		CustomerService customerService = (CustomerService) loginManager.login("alexLevi@gmail.com", "794613", ClientType.CUSTOMER);

		customerService.purchase(1);
		customerService.purchase(5);
		customerService.purchase(3);
		customerService.purchase(4);
		customerService.purchase(7);
		CustomerService finalCustomerService = customerService;
		e = Assertions.assertThrows(ClientServiceException.class, () -> finalCustomerService.purchase(6));//date
		System.out.println(e);
		e = Assertions.assertThrows(ClientServiceException.class, () -> finalCustomerService.purchase(8));
		System.out.println(e);

		customerService = (CustomerService) loginManager.login("MosheCohen@gmail.com", ".....", ClientType.CUSTOMER);
		customerService.purchase(1);
		customerService.purchase(3);
		customerService.purchase(4);
		customerService.purchase(7);
		e = Assertions.assertThrows(ClientServiceException.class, () -> finalCustomerService.purchase(5));//amount
		System.out.println(e);
		e = Assertions.assertThrows(ClientServiceException.class, () -> finalCustomerService.purchase(1));//bayed before
		System.out.println(e);

		Assertions.assertEquals(18, customerService.getCustomerCoupons().get(0).getAmount(), "no Equals");
		Assertions.assertEquals(2, customerService.getCustomerCoupons(Category.CAMPING).size(), "no Equals");
		Assertions.assertEquals(1, customerService.getCustomerCoupons(45).size(), "no Equals");
//		System.out.println("All customer coupons: " + customerService.getCustomerCoupons());
//		System.out.println("Customer coupons in Sport: " + customerService.getCustomerCoupons(Category.CAMPING));
//		System.out.println("Customer coupons under 45$: " + customerService.getCustomerCoupons(45));
//		System.out.println("customer details: " + customerService.getCustomerDetails());

		CompanyService companyService = (CompanyService) loginManager.login("IBM@gmail.com", "8520", ClientType.COMPANY);//correct email and password
		Assertions.assertNotNull(companyService);
		Assertions.assertEquals(4, customerService.getCustomerCoupons().size(), "no Equals");

		e = Assertions.assertThrows(ClientServiceException.class, () -> companyService.deleteCoupon(4));//not yours
		System.out.println(e);
		companyService.deleteCoupon(3);
		Assertions.assertEquals(3, customerService.getCustomerCoupons().size(), "no Equals");
		Assertions.assertEquals(2, customerService.getCustomerCoupons(Category.CAMPING).size(), "no Equals");

		AdminService adminService = (AdminService) loginManager.login("admin@admin.com", "admin", ClientType.ADMINISTRATOR);//correct email and password
		Assertions.assertNotNull(adminService);
		adminService.deleteCompany(2);
		Assertions.assertEquals(1, customerService.getCustomerCoupons(Category.CAMPING).size(), "no Equals");
		Assertions.assertEquals(2, customerService.getCustomerCoupons().size(), "no Equals");
		 e = Assertions.assertThrows(LoginException.class, () -> loginManager.login("IBM@gmail.com", "8520", ClientType.COMPANY));//wrong email and password
		System.out.println(e);
	}
	@Test
	@Order(3)
	void jobTest() {
		CompanyService companyService = (CompanyService) loginManager.login("Monday@gmail.com", "8888", ClientType.COMPANY);
		Assertions.assertNotNull(companyService);
		Assertions.assertEquals(4, companyService.companyCoupons().size(), "no Equals");
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		//System.out.println(companyService.companyCoupons());
		Assertions.assertEquals(3, companyService.companyCoupons().size(), "no Equals");
	}
}

