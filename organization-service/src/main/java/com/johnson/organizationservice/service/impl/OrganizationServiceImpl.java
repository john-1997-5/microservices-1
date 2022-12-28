package com.johnson.organizationservice.service.impl;

import com.johnson.organizationservice.dto.OrganizationDto;
import com.johnson.organizationservice.entity.Organization;
import com.johnson.organizationservice.repository.OrganizationRepository;
import com.johnson.organizationservice.service.OrganizationService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final ModelMapper mapper;

    @Override
    public OrganizationDto saveOrganization(OrganizationDto organizationDto) {
        Organization organization = mapper.map(organizationDto, Organization.class);
        Organization savedOrg = organizationRepository.save(organization);
        return mapper.map(savedOrg, OrganizationDto.class);
    }

    @Override
    public OrganizationDto getOrganizationByCode(String code) {
        Organization organization = organizationRepository.findByOrganizationCode(code)
                .orElseThrow(() -> new NoSuchElementException("no organization found with code: " + code));
        return mapper.map(organization, OrganizationDto.class);
    }
}
