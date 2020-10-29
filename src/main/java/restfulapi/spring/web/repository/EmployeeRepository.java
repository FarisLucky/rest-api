package restfulapi.spring.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restfulapi.spring.web.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
}
