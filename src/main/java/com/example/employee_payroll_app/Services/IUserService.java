package com.example.employee_payroll_app.Services;

import com.example.employee_payroll_app.dto.UserDTO;

public interface IUserService {
    public String registerUser(UserDTO userdto);
    public String authenticateUser(String email, String password);
}
