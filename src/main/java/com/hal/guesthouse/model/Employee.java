package com.hal.guesthouse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Employee {

    @Id
    @Column(name = "pb_no", unique = true, nullable = false)
    private String pbNo;

    @Column(nullable = false)
    private String employeeName;

    @Column(nullable = false)
    private String password;

    private String department;
    private String designation;
    private String mobileNumber;
    private String email;
    private String gender;
    private LocalDate dateOfJoining;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.EMPLOYEE;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    public enum Role { EMPLOYEE, ADMIN }
    public enum Status { ACTIVE, INACTIVE }
}
