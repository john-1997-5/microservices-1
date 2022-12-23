package com.johnson.employeeservice.service;

import com.johnson.employeeservice.dto.DepartmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/*
OpenFeign creará una implementación dinámicamente para esta interfaz
 */
@FeignClient(url = "http://localhost:8080", name = "department-service") // base url & nombre micro
public interface APIClient {
    /*
    aquí declaramos todos los endpoints del otro micro a los que queremos llamar
     */

    @GetMapping("/api/departments/{code}")
    DepartmentDto getDepartmentByCode(@PathVariable(value = "code") String departmentCode);

}