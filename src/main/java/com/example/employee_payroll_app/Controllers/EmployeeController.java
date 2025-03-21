package com.example.employee_payroll_app.Controllers;
//package com.example.employee_payroll_app.Controllers;
//
//import com.example.employee_payroll_app.dto.EmployeeDTO;
//import com.example.employee_payroll_app.model.Employee;
//import com.example.employee_payroll_app.Services.EmployeeService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/employees")
//public class EmployeeController {
//
//    @Autowired
//    private EmployeeService employeeService;
//
//    // Get all employees
//    @GetMapping
//    public List<Employee> getAllEmployees() {
//        return employeeService.getAllEmployees();
//    }
//
//    // Get employee by ID
//    @GetMapping("/get/{id}")
//    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
//        Optional<Employee> employee = employeeService.getEmployeeById(id);
//        return employee.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // Create a new employee using DTO
//    @PostMapping("/create")
//    public Employee createEmployee(@RequestBody EmployeeDTO employeeDTO) {
//        return employeeService.createEmployee(employeeDTO);
//    }
//
//    // Update an employee using DTO
//    @PutMapping("/update/{id}")
//    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO) {
//        Employee updatedEmployee = employeeService.updateEmployee(id, employeeDTO);
//        return ResponseEntity.ok(updatedEmployee);
//    }
//
//    // Delete employee by ID
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
//        boolean isDeleted = employeeService.deleteEmployee(id);
//        if (isDeleted) {
//            return ResponseEntity.ok("Employee deleted successfully.");
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//}


//import com.example.employee_payroll_app.dto.EmployeeDTO;
//import com.example.employee_payroll_app.Services.EmployeeService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/employeepayrollservice")
//public class EmployeeController {
//
//    @Autowired
//    private EmployeeService employeeService;
//
//    @GetMapping("/employees")
//    public List<EmployeeDTO> getAllEmployees() {
//        return employeeService.getAllEmployees();
//    }
//
//    @GetMapping("/employee/{id}")
//    public EmployeeDTO getEmployeeById(@PathVariable Long id) {
//        return employeeService.getEmployeeById(id);
//    }
//
//    @PostMapping("/employee")
//    public EmployeeDTO createEmployee(@RequestBody EmployeeDTO employeeDTO) {
//        return employeeService.createEmployee(employeeDTO);
//    }
//
//    @PutMapping("/update/{id}")  // Here we specify {id} in the path
//    public EmployeeDTO updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO) {
//        return employeeService.updateEmployee(id, employeeDTO);  // Pass the id and DTO to service layer
//    }
//
//    @DeleteMapping("/employee/{id}")
//    public void deleteEmployee(@PathVariable Long id) {
//        employeeService.deleteEmployee(id);
//    }
//}

// UC9 - Data Validation

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.validation.annotation.Validated;
//import jakarta.validation.Valid;
//import org.springframework.http.ResponseEntity;
//import com.example.employee_payroll_app.dto.EmployeeDTO;
//import com.example.employee_payroll_app.Services.EmployeeService;
//import java.util.List;
//
//@RestController
//@RequestMapping("/employees")
//@Validated
//public class EmployeeController {
//
//    @Autowired
//    private EmployeeService employeeService;
//
//    @GetMapping("/")
//    public List<EmployeeDTO> getAllEmployees() {
//        return employeeService.getAllEmployees();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
//        EmployeeDTO employee = employeeService.getEmployeeById(id);
//        return employee != null ? ResponseEntity.ok(employee) : ResponseEntity.notFound().build();
//    }
//
//    @PostMapping("/add")
//    public ResponseEntity<EmployeeDTO> addEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
//        EmployeeDTO savedEmployee = employeeService.createEmployee(employeeDTO);
//        return new ResponseEntity<>(savedEmp
//        loyee, HttpStatus.CREATED);
//    }
//
//    @PutMapping("/update/{id}")
//    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDTO) {
//        EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employeeDTO);
//        return ResponseEntity.ok(updatedEmployee);
//    }
//
//    @DeleteMapping("/employee/{id}")
//    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
//        employeeService.deleteEmployee(id);
//        return ResponseEntity.ok("Employee deleted successfully!");
//    }
//}

import com.example.employee_payroll_app.dto.EmployeeDTO;
import com.example.employee_payroll_app.Services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // ✅ Get All Employees
    @GetMapping("/")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    // ✅ Get Employee By ID with Exception Handling
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        try {
            EmployeeDTO employee = employeeService.getEmployeeById(id);
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found with ID: " + id);
        }
    }

    // ✅ Add Employee with Validation Errors Handled
    @PostMapping("/add")
    public ResponseEntity<?> addEmployee(@Valid @RequestBody EmployeeDTO employeeDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getValidationErrors(result));
        }
        EmployeeDTO savedEmployee = employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    // ✅ Update Employee with Exception Handling
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getValidationErrors(result));
        }
        try {
            EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employeeDTO);
            return ResponseEntity.ok(updatedEmployee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found with ID: " + id);
        }
    }

    // ✅ Delete Employee with Exception Handling
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.ok("Employee deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found with ID: " + id);
        }
    }

    // 🔹 Utility Method to Handle Validation Errors
    private Map<String, String> getValidationErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }
}


