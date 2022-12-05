package app.core.repositories;

import app.core.entites.Company;
import app.core.entites.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepo extends JpaRepository<Company,Integer> {
    List<Company> findByEmailAndPassword(String email,String password);
    List<Company> findByEmailOrName(String email,String name);

}
