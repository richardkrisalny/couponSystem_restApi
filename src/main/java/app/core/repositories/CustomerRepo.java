package app.core.repositories;

import app.core.entites.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer,Integer> {
    Customer findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
}
