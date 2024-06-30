package com.example.rqchallenge.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.rqchallenge.exception.InvalidRequestException;

@SpringBootTest
public class EmployeeServiceTests {
	@Autowired
	private EmployeeService emplService;
	
    @Test
    @DisplayName("Search employees with blank search string")
    public void testSearchEmployeeWithBlankSearchString() {
		assertThrows(InvalidRequestException.class, ()-> emplService.getEmployeesByNameSearch(""));
    }
    
    @Test
    @DisplayName("Create employee with bad input")
    public void testCreateEmployeeWithBadInput() {
    	// Null inputs
		assertThrows(InvalidRequestException.class, () -> emplService.createEmployee(null));
		
		// No inputs
		Map<String, Object> crInputMap = new HashMap<>();
		assertThrows(InvalidRequestException.class, () -> emplService.createEmployee(crInputMap));
		
		// Blank name
		crInputMap.put("name", "");
		assertThrows(InvalidRequestException.class, () -> emplService.createEmployee(crInputMap));
		
		// Valid name but no age
		crInputMap.put("name", "Jon");
		assertThrows(InvalidRequestException.class, () -> emplService.createEmployee(crInputMap));
		
		// Valid name, age but no salary
		crInputMap.put("name", "Jon");
		crInputMap.put("age", 30);
		assertThrows(InvalidRequestException.class, () -> emplService.createEmployee(crInputMap));
    }
}
