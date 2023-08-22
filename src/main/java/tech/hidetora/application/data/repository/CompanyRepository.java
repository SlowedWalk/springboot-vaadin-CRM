package tech.hidetora.application.data.repository;

import tech.hidetora.application.data.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}
