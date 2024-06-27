package com.example.rqchallenge.externalapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.rqchallenge.externalapi.data.EmployeeData;

@SpringBootTest
public class EmployeeAPIClientTests {
	@Autowired
	private EmployeeAPIClient extEmpAPIClient;
	
	@BeforeAll
	static void setup() {
	}
	
	@Test
    @DisplayName("Get all employees list from client")
    public void testGetAllEmployeesAPI() {	
		List<EmployeeData> emplDataList = extEmpAPIClient.getAllEmployees();
		Assertions.assertNotNull(emplDataList);
        Assertions.assertEquals(24, emplDataList.size());
    }
	
	@Test
    @DisplayName("Get specific employee from client")
    public void testGetEmployeeByIdAPI() {
		EmployeeData emplData = extEmpAPIClient.getEmployeeById("2");
		Assertions.assertNotNull(emplData);
    }
	
	@Test
    @DisplayName("Create employee from client")
    public void testCreateEmployeeAPI() {
		Map<String, Object> emplInput = new HashMap<>();
		final String name = "jon";
		final int age = 30;
		final int salary = 4000;
		emplInput.put("name", name);
		emplInput.put("age", age);
		emplInput.put("salary", salary);
		
		EmployeeData emplData = extEmpAPIClient.createEmployee(emplInput);
		Assertions.assertNotNull(emplData);
		Assertions.assertEquals(emplData.getName(), name);
		Assertions.assertEquals(emplData.getAge(), age);
		Assertions.assertEquals(emplData.getSalary(), salary);
    }
	
	@Test
    @DisplayName("Delete employee from client")
    public void testDelEmployeeByIdAPI() {
		final String emplId = "2";
		String emplName = extEmpAPIClient.deleteEmployeeById(emplId);
		Assertions.assertNotNull(emplName);
    }
}
