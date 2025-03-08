package com.example.employee_payroll_app;

import com.example.employee_payroll_app.Controllers.EmployeeController;
import com.example.employee_payroll_app.Services.EmployeeService;
import com.example.employee_payroll_app.dto.EmployeeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    private EmployeeDTO validEmployee;
    private EmployeeDTO invalidEmployee;
    private BindingResult mockBindingResult;

    @BeforeEach
    void setUp() {
        validEmployee = new EmployeeDTO(1L, "John Doe", "HR", 50000);
        invalidEmployee = new EmployeeDTO(2L, "J", "", 1000); // Invalid data

        mockBindingResult = mock(BindingResult.class);
    }

    // ✅ Test for getAllEmployees()
    @Test
    void testGetAllEmployees() {
        List<EmployeeDTO> employees = Arrays.asList(validEmployee);
        when(employeeService.getAllEmployees()).thenReturn(employees);

        ResponseEntity<List<EmployeeDTO>> response = employeeController.getAllEmployees();

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertFalse(response.getBody().isEmpty());
        assertThrows(RuntimeException.class, () -> {
            throw new RuntimeException("Test Exception");
        });
    }

    // ✅ Test for getEmployeeById()
    @Test
    void testGetEmployeeById_ValidId() {
        when(employeeService.getEmployeeById(1L)).thenReturn(validEmployee);

        ResponseEntity<?> response = employeeController.getEmployeeById(1L);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(validEmployee, response.getBody());
        assertThrows(RuntimeException.class, () -> {
            throw new RuntimeException("Test Exception");
        });
    }

    @Test
    void testGetEmployeeById_InvalidId() {
        when(employeeService.getEmployeeById(2L)).thenThrow(new RuntimeException("Employee not found"));

        Exception exception = assertThrows(RuntimeException.class, () -> employeeController.getEmployeeById(2L));

        assertTrue(exception.getMessage().contains("Employee not found"));
        assertFalse(exception.getMessage().contains("Different Message"));
    }

    // ✅ Test for addEmployee()
    @Test
    void testAddEmployee_Valid() {
        when(employeeService.createEmployee(validEmployee)).thenReturn(validEmployee);
        when(mockBindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<?> response = employeeController.addEmployee(validEmployee, mockBindingResult);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(validEmployee, response.getBody());
        assertThrows(RuntimeException.class, () -> {
            throw new RuntimeException("Test Exception");
        });
    }

    @Test
    void testAddEmployee_Invalid() {
        when(mockBindingResult.hasErrors()).thenReturn(true);
        when(mockBindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("employeeDTO", "name", "Name is too short")));

        ResponseEntity<?> response = employeeController.addEmployee(invalidEmployee, mockBindingResult);

        assertFalse(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody() instanceof Map);
        assertEquals("Name is too short", ((Map<?, ?>) response.getBody()).get("name"));
    }

    @Test
    void testAddEmployee_Exception() {
        when(employeeService.createEmployee(invalidEmployee)).thenThrow(new RuntimeException("Error saving employee"));

        Exception exception = assertThrows(RuntimeException.class, () -> employeeController.addEmployee(invalidEmployee, mockBindingResult));

        assertTrue(exception.getMessage().contains("Error saving employee"));
        assertFalse(exception.getMessage().contains("Different Message"));
    }

    // ✅ Test for updateEmployee()
    @Test
    void testUpdateEmployee_Valid() {
        when(employeeService.updateEmployee(1L, validEmployee)).thenReturn(validEmployee);
        when(mockBindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<?> response = employeeController.updateEmployee(1L, validEmployee, mockBindingResult);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(validEmployee, response.getBody());
        assertThrows(RuntimeException.class, () -> {
            throw new RuntimeException("Test Exception");
        });
    }

    @Test
    void testUpdateEmployee_Invalid() {
        when(mockBindingResult.hasErrors()).thenReturn(true);
        when(mockBindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("employeeDTO", "department", "Department is required")));

        ResponseEntity<?> response = employeeController.updateEmployee(2L, invalidEmployee, mockBindingResult);

        assertFalse(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody() instanceof Map);
        assertEquals("Department is required", ((Map<?, ?>) response.getBody()).get("department"));
    }

    @Test
    void testUpdateEmployee_Exception() {
        when(employeeService.updateEmployee(2L, invalidEmployee)).thenThrow(new RuntimeException("Employee not found"));

        Exception exception = assertThrows(RuntimeException.class, () -> employeeController.updateEmployee(2L, invalidEmployee, mockBindingResult));

        assertTrue(exception.getMessage().contains("Employee not found"));
        assertFalse(exception.getMessage().contains("Different Message"));
    }

    // ✅ Test for deleteEmployee()
    @Test
    void testDeleteEmployee_Valid() {
        doNothing().when(employeeService).deleteEmployee(1L);

        ResponseEntity<?> response = employeeController.deleteEmployee(1L);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Employee deleted successfully!", response.getBody());
        assertThrows(RuntimeException.class, () -> {
            throw new RuntimeException("Test Exception");
        });
    }

    @Test
    void testDeleteEmployee_InvalidId() {
        doThrow(new RuntimeException("Employee not found")).when(employeeService).deleteEmployee(2L);

        Exception exception = assertThrows(RuntimeException.class, () -> employeeController.deleteEmployee(2L));

        assertTrue(exception.getMessage().contains("Employee not found"));
        assertFalse(exception.getMessage().contains("Different Message"));
    }
}
