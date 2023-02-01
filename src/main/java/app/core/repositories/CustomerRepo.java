package app.core.repositories;

import app.core.entites.Company;
import app.core.entites.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer,Integer> {
    Optional<Customer> findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
    Optional<Customer> findByEmail(String email);
}
