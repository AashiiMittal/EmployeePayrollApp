//package com.example.employee_payroll_app.dto;
//
//public class EmployeeDTO {
//    private String name;
//    private String department;
//    private double salary;
//
//    // Constructors
//    public EmployeeDTO() {}
//
//    public EmployeeDTO(String name, String department, double salary) {
//        this.name = name;
//        this.department = department;
//        this.salary = salary;
//    }
//
//    // Getters and Setters
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDepartment() {
//        return department;
//    }
//
//    public void setDepartment(String department) {
//        this.department = department;
//    }
//
//    public double getSalary() {
//        return salary;
//    }
//
//    public void setSalary(double salary) {
//        this.salary = salary;
//    }
//}

// USING LOMBOK LIBRARY:
//package com.example.employee_payroll_app.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class EmployeeDTO {
//
//    private Long id;
//    private String name;
//    private String department;  // Should be String
//    private double salary;      // Should be double
//
//    public EmployeeDTO(Long id, String name, double salary, String department) {
//        this.id = id;
//        this.name = name;
//        this.salary = salary;
//        this.department = department;
//    }
//}

// UC9 -  Data Validation and Exception Handling in Employee Payroll Service

package com.example.employee_payroll_app.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO implements Serializable {
    private static final long serialVersionUID = 1L; // Required for serialization
    private Long id;

    @NotBlank(message = "Employee name cannot be empty")
    @Pattern(regexp = "^[A-Z][a-zA-Z ]{2,}$", message = "Name must start with a capital letter and have at least 3 characters")
    private String name;

    @NotBlank(message = "Department cannot be empty")
    private String department;

    @Min(value = 20000, message = "Salary must be at least 20000")
    private double salary;

//    public EmployeeDTO(Long id, String name, String department, double salary) {
//        this.id =id;
//        this.name= name;
//        this.department=department;
//        this.salary=salary;
//    }
//    public EmployeeDTO() {}
}
