package com.hal.guesthouse.service;

import com.hal.guesthouse.dto.AuthDTO;
import com.hal.guesthouse.model.Employee;
import com.hal.guesthouse.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final EmployeeRepository employeeRepository;

    public AuthDTO.LoginResponse login(AuthDTO.LoginRequest request) {
        Optional<Employee> emp = employeeRepository
                .findByPbNoAndPassword(request.getPbNo(), request.getPassword());

        AuthDTO.LoginResponse response = new AuthDTO.LoginResponse();

        if (emp.isPresent()) {
            Employee e = emp.get();
            response.setResult("LOGIN_SUCCESS");
            response.setPbNo(e.getPbNo());
            response.setEmployeeName(e.getEmployeeName());
            response.setRole(e.getRole().name());
            response.setDepartment(e.getDepartment());
        } else {
            response.setResult("LOGIN_FAILED");
        }

        return response;
    }

    public String register(AuthDTO.RegisterRequest request) {
        if (employeeRepository.existsByPbNo(request.getPbNo())) {
            return "PB_NO_ALREADY_EXISTS";
        }

        Employee employee = Employee.builder()
                .pbNo(request.getPbNo())
                .employeeName(request.getEmployeeName())
                .password(request.getPassword())
                .department(request.getDepartment())
                .designation(request.getDesignation())
                .mobileNumber(request.getMobileNumber())
                .email(request.getEmail())
                .gender(request.getGender())
                .dateOfJoining(request.getDateOfJoining())
                .role(Employee.Role.EMPLOYEE)
                .status(Employee.Status.ACTIVE)
                .build();

        employeeRepository.save(employee);
        return "REGISTRATION_SUCCESS";
    }

    public Employee getProfile(String pbNo) {
        return employeeRepository.findById(pbNo)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }
}
