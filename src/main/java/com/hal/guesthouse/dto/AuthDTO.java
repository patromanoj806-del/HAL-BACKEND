package com.hal.guesthouse.dto;

import lombok.Data;
import java.time.LocalDate;

public class AuthDTO {

    @Data
    public static class LoginRequest {
        private String pbNo;
        private String password;
    }

    @Data
    public static class LoginResponse {
        private String result;
        private String pbNo;
        private String employeeName;
        private String role;
        private String department;
    }

    @Data
    public static class RegisterRequest {
        private String pbNo;
        private String employeeName;
        private String password;
        private String department;
        private String designation;
        private String mobileNumber;
        private String email;
        private String gender;
        private LocalDate dateOfJoining;
    }
}
