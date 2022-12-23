package com.johnson.employeeservice.service.impl;

import com.johnson.employeeservice.dto.ApiResponseDto;
import com.johnson.employeeservice.dto.DepartmentDto;
import com.johnson.employeeservice.dto.EmployeeDto;
import com.johnson.employeeservice.exception.EmailAlreadyExistsException;
import com.johnson.employeeservice.exception.ResourceNotFoundException;
import com.johnson.employeeservice.entity.Employee;
import com.johnson.employeeservice.repository.EmployeeRepository;
import com.johnson.employeeservice.service.APIClient;
import com.johnson.employeeservice.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper mapper;
    private final RestTemplate restTemplate;
    private final WebClient webClient;
    private final APIClient apiClient;

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        Employee employee = mapper.map(employeeDto, Employee.class);
        employeeRepository.getByEmail(employee.getEmail())
                .ifPresent(emp -> {
                    throw new EmailAlreadyExistsException();
                });

        Employee savedEmployee = employeeRepository.save(employee);
        return mapper.map(savedEmployee, EmployeeDto.class);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employee -> mapper.map(employee, EmployeeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ApiResponseDto getEmployeeById(long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with id: " + id));

        EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);

        // REST call
        String url = "http://localhost:8080/api/departments/" + employee.getDepartmentCode();
/*        ResponseEntity<DepartmentDto> responseEntity = restTemplate
                .getForEntity(url, DepartmentDto.class);
        DepartmentDto departmentDto = responseEntity.getBody();*/

/*        DepartmentDto departmentDto = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(DepartmentDto.class)
                .block(); // to make it sync*/

        DepartmentDto departmentDto = apiClient.getDepartmentByCode(employeeDto.getDepartmentCode());

        return new ApiResponseDto(employeeDto, departmentDto);

    }

    @Override
    public EmployeeDto updateEmployee(EmployeeDto employeeDto) {
        Employee employee = employeeRepository.findById(employeeDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No user found with id: " + employeeDto.getId()));

        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setEmail(employeeDto.getEmail());
        employee.setDepartmentCode(employeeDto.getDepartmentCode());

        Employee updatedEmployee = employeeRepository.save(employee);

        return mapper.map(updatedEmployee, EmployeeDto.class);
    }

    @Override
    public void deleteEmployee(long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with id: " + id));
        employeeRepository.delete(employee);
    }
}
