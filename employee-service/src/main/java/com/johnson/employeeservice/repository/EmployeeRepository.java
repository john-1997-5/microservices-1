package com.johnson.employeeservice.repository;

import com.johnson.employeeservice.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> getByEmail(String email);

    @Query("select e from Employee e where e.firstName = :firstName and e.lastName = :lastName")
    Employee findByJPQL(String firstName, String lastName);

    @Query(value = "select * from employees where first_name = ?1 and email = ?2", nativeQuery = true)
    Employee findBySQL(String firstName, String email);

    @Query(value = "select * from employees where first_name = :firstName and email = :email", nativeQuery = true)
    Employee findByNamedSQL(@Param("firstName") String firstName, @Param("email") String email);
}
