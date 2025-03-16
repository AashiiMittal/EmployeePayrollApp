package com.example.employee_payroll_app.Services;

public interface IEmailService {
    public void sendEmail(String toEmail, String subject, String body);
}
