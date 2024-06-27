package com.example.rqchallenge.service;

import java.util.List;
import java.util.Map;

import com.example.rqchallenge.dto.Employee;

public interface EmployeeService {
	List<Employee> getAllEmployees();
	List<Employee> getEmployeesByNameSearch(String searchString);
	Employee getEmployeeById(String id);
	Integer getHighestSalaryOfEmployees();
	List<String> getTopTenHighestEarningEmployeeNames();
	Employee createEmployee(Map<String, Object> employeeInput);
	String deleteEmployeeById(String id);
}
