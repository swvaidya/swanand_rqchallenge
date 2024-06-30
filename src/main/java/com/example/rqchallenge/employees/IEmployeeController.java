package com.example.rqchallenge.employees;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.rqchallenge.dto.Employee;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public interface IEmployeeController {

    /**
     * Get list of all employees
     * @return List of employee data
     * @throws IOException
     */
	@GetMapping()
    ResponseEntity<List<Employee>> getAllEmployees() throws IOException;

    /**
     * Search the employees whose name contains the search string
     * @param searchString
     * @return List of employee data
     */
    @GetMapping("/search/{searchString}")
    ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString);

    /**
     * Get employee with specified Id
     * @param id
     * @return Employee data
     */
    @GetMapping("/{id}")
    ResponseEntity<Employee> getEmployeeById(@PathVariable String id);

    /**
     * Get maximum salary from all employees
     * @return Salary value
     */
    @GetMapping("/highestSalary")
    ResponseEntity<Integer> getHighestSalaryOfEmployees();

    /**
     * Get names of 10 employees having top salaries
     * @return List of employee names
     */
    @GetMapping("/topTenHighestEarningEmployeeNames")
    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames();

    /**
     * Create a new employee record.
     * @param employeeInput map containing name, age, salary as mandatory and
     * profileImage as optional field
     * @return Employee data for the newly created employee
     */
    @PostMapping()
    ResponseEntity<Employee> createEmployee(@RequestBody Map<String, Object> employeeInput);

    /**
     * Delete the employee with specified Id
     * @param id
     * @return Name of the employee deleted
     */
    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteEmployeeById(@PathVariable String id);

}
