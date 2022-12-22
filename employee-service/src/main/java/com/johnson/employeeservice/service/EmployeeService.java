package com.johnson.employeeservice.service;

import com.johnson.employeeservice.dto.ApiResponseDto;
import com.johnson.employeeservice.dto.EmployeeDto;
import com.johnson.employeeservice.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    EmployeeDto saveEmployee(EmployeeDto employee);

    List<EmployeeDto> getAllEmployees();

    //Employee getEmployeeById(long id);
    ApiResponseDto getEmployeeById(long id);

    EmployeeDto updateEmployee(EmployeeDto employee);

    void deleteEmployee(long id);
}
