package com.example.rqchallenge.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.employees.IEmployeeController;
import com.example.rqchallenge.service.EmployeeService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/employees")
public class EmployeeController implements IEmployeeController {
	private final EmployeeService emplService;
	
	@Override
	public ResponseEntity<List<Employee>> getAllEmployees() throws IOException {
		return ResponseEntity.ok(emplService.getAllEmployees());
	}

	@Override	
	public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
		return ResponseEntity.ok(emplService.getEmployeesByNameSearch(searchString));
	}

	@Override
	public ResponseEntity<Employee> getEmployeeById(String id) {
		return ResponseEntity.ok(emplService.getEmployeeById(id));
	}

	@Override
	public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
		return ResponseEntity.ok(emplService.getHighestSalaryOfEmployees());
	}

	@Override
	public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
		return ResponseEntity.ok(emplService.getTopTenHighestEarningEmployeeNames());
	}

	@Override
	public ResponseEntity<Employee> createEmployee(Map<String, Object> employeeInput) {
		return new ResponseEntity<>(emplService.createEmployee(employeeInput), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<String> deleteEmployeeById(String id) {
		return ResponseEntity.ok(emplService.deleteEmployeeById(id));
	}

}
