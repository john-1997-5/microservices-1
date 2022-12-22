package com.johnson.employeeservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentDto {
    private Long id;
    private String departmentName;
    private String departmentDescription;
    private String departmentCode;
}
