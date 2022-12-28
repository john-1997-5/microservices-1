package com.johnson.employeeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ApiResponseDto {
    private EmployeeDto employee;
    private DepartmentDto departmentDto;
    private OrganizationDto organizationDto;
}
