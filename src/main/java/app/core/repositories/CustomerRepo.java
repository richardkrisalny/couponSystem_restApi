package app.core.repositories;

import app.core.entites.Company;
import app.core.entites.Coupon;
import app.core.entites.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepo extends JpaRepository<Customer,Integer> {
    List<Customer> findByEmailAndPassword(String email, String password);
    List<Customer> findByEmail(String email);
}
