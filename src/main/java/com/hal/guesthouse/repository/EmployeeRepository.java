package com.hal.guesthouse.repository;

import com.hal.guesthouse.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Optional<Employee> findByPbNoAndPassword(String pbNo, String password);
    boolean existsByPbNo(String pbNo);
    boolean existsByEmail(String email);
}
