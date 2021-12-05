package integration.testing.spring.repositories;

import integration.testing.spring.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {

}
