package tech.hidetora.application.data.repository;

import tech.hidetora.application.data.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {

}
