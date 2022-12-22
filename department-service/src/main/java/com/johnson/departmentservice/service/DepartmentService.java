package com.johnson.departmentservice.service;

import com.johnson.departmentservice.dto.DepartmentDto;
import com.johnson.departmentservice.entity.Department;

import java.util.List;

public interface DepartmentService {
    DepartmentDto saveDepartment(DepartmentDto departmentDto);

    DepartmentDto getDepartmentById(Long departmentId);

    DepartmentDto getDepartmentByCode(String departmentCode);

    List<DepartmentDto> getAllDepartments();

    DepartmentDto updateDepartment(DepartmentDto departmentDto);

    void deleteDepartment(Long departmentId);
}
