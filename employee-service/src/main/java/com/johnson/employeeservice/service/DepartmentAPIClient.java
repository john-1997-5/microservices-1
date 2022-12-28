package com.johnson.employeeservice.service;

import com.johnson.employeeservice.dto.DepartmentDto;
import com.johnson.employeeservice.dto.OrganizationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
/*
    APIClient usará internamente el load balancer proporcionado por la dependencia de
    Eureka Client para llamar a la instancia del micro DEPARTMENT-SERVICE que se encuentre disponible
 */
@FeignClient(name = "DEPARTMENT-SERVICE")
public interface DepartmentAPIClient {
    /*
    aquí declaramos todos los endpoints del otro micro a los que queremos llamar
     */

    @GetMapping("/api/departments/{code}")
    DepartmentDto getDepartmentByCode(@PathVariable(value = "code") String departmentCode);

}