package app.core.repositories;

import app.core.entites.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CompanyRepo extends JpaRepository<Company,Integer> {

    Company findByEmailAndPassword(String email,String password);
   boolean existsByEmailOrName(String email,String name);

}
