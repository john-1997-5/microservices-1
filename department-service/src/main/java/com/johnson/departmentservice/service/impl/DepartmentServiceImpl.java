package com.johnson.departmentservice.service.impl;

import com.johnson.departmentservice.dto.DepartmentDto;
import com.johnson.departmentservice.entity.Department;
import com.johnson.departmentservice.exception.ResourceNotFoundException;
import com.johnson.departmentservice.repository.DepartmentRepository;
import com.johnson.departmentservice.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final ModelMapper mapper;


    @Override
    public DepartmentDto saveDepartment(DepartmentDto departmentDto) {
        Department department = mapper.map(departmentDto, Department.class);
        Department savedDepartment = departmentRepository.save(department);
        return mapper.map(savedDepartment, DepartmentDto.class);
    }

    @Override
    public DepartmentDto getDepartmentById(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", String.valueOf(departmentId)));
        return mapper.map(department, DepartmentDto.class);
    }

    @Override
    public DepartmentDto getDepartmentByCode(String departmentCode) {
        Department department = departmentRepository.findByDepartmentCode(departmentCode)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "code", departmentCode));
        return mapper.map(department, DepartmentDto.class);
    }

    @Override
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(department -> mapper.map(department, DepartmentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDto updateDepartment(DepartmentDto departmentDto) {
        Department department = departmentRepository.findById(departmentDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", String.valueOf(departmentDto.getId())));

        department.setDepartmentName(departmentDto.getDepartmentName());
        department.setDepartmentDescription(departmentDto.getDepartmentDescription());
        department.setDepartmentCode(departmentDto.getDepartmentCode());

        Department updatedDepartment = departmentRepository.save(department);
        return mapper.map(updatedDepartment, DepartmentDto.class);
    }

    @Override
    public void deleteDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", String.valueOf(departmentId)));
        departmentRepository.delete(department);
    }


}
