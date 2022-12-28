package com.johnson.employeeservice.service.impl;

import com.johnson.employeeservice.dto.ApiResponseDto;
import com.johnson.employeeservice.dto.DepartmentDto;
import com.johnson.employeeservice.dto.EmployeeDto;
import com.johnson.employeeservice.dto.OrganizationDto;
import com.johnson.employeeservice.exception.EmailAlreadyExistsException;
import com.johnson.employeeservice.exception.ResourceNotFoundException;
import com.johnson.employeeservice.entity.Employee;
import com.johnson.employeeservice.repository.EmployeeRepository;
import com.johnson.employeeservice.service.DepartmentAPIClient;
import com.johnson.employeeservice.service.EmployeeService;
import com.johnson.employeeservice.service.OrganizationAPIClient;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper mapper;
    private final RestTemplate restTemplate;
    private final WebClient webClient;
    private final DepartmentAPIClient departmentAPIClient;
    private final OrganizationAPIClient organizationAPIClient;

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

    //@CircuitBreaker(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    @Retry(name="${spring.application.name}", fallbackMethod = "getDefaultDepartment")
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

        log.info(">> intentando llamar al micro department-service...");
        DepartmentDto departmentDto = departmentAPIClient.getDepartmentByCode(employeeDto.getDepartmentCode());
        OrganizationDto organizationDto = organizationAPIClient.getOrganizationByCod(employeeDto.getOrganizationCode());
        log.info(">>>> comunicación con department-service successful!");
        return new ApiResponseDto(employeeDto, departmentDto, organizationDto);
    }

    private ApiResponseDto getDefaultDepartment(long id, Exception e) {
        log.info(">> comunicación fallida, estoy dentro del fallback method...");
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with id: " + id));

        EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);

        // instead of REST call, I set a default DepartmentDto
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentName("DFLT Department");
        departmentDto.setDepartmentDescription("Default department for losers");
        departmentDto.setDepartmentCode("DFLT001");

        OrganizationDto organizationDto = new OrganizationDto();
        organizationDto.setOrganizationName("DFLT Organization");
        organizationDto.setOrganizationDescription("Default organization for losers");
        organizationDto.setOrganizationCode("DFLT_ORG");

        return new ApiResponseDto(employeeDto, departmentDto, organizationDto);

    }

    @Override
    public EmployeeDto updateEmployee(EmployeeDto employeeDto) {
        Employee employee = employeeRepository.findById(employeeDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No user found with id: " + employeeDto.getId()));

        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setEmail(employeeDto.getEmail());
        employee.setDepartmentCode(employeeDto.getDepartmentCode());
        employee.setOrganizationCode(employeeDto.getOrganizationCode());

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
