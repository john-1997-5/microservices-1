package com.johnson.employeeservice.service;

import com.johnson.employeeservice.dto.OrganizationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ORGANIZATION-SERVICE")
public interface OrganizationAPIClient {

    @GetMapping("/api/organizations/{organization-code}")
    OrganizationDto getOrganizationByCod(@PathVariable(name = "organization-code") String organizationCode);

}
