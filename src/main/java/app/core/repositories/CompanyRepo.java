package app.core.repositories;

import app.core.entites.Company;
import app.core.entites.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepo extends JpaRepository<Company,Integer> {
    Company findByEmailAndPassword(String email,String password);
   boolean existsByEmailOrName(String email,String name);

}
