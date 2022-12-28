package com.johnson.organizationservice.controller;

import com.johnson.organizationservice.dto.OrganizationDto;
import com.johnson.organizationservice.service.OrganizationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {
    private final OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<OrganizationDto> saveOrganization(@RequestBody OrganizationDto organizationDto) {
        return new ResponseEntity<>(organizationService.saveOrganization(organizationDto), HttpStatus.CREATED);
    }

    @GetMapping("/{organization-code}")
    public ResponseEntity<OrganizationDto> getOrganizationByCod(@PathVariable(name = "organization-code") String organizationCode) {
        return ResponseEntity.ok(organizationService.getOrganizationByCode(organizationCode));
    }
}
