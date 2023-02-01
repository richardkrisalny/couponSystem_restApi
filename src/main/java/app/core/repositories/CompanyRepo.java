package app.core.repositories;

import app.core.entites.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CompanyRepo extends JpaRepository<Company,Integer> {

    Optional<Company> findByEmailAndPassword(String email, String password);
    Optional<Company> findByEmail(String email);
   boolean existsByEmailOrName(String email,String name);

}
