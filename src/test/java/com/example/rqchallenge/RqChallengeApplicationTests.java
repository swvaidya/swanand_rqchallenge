package com.example.rqchallenge;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.service.MessageConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RqChallengeApplicationTests {
	private static final String LOCAL_HOST_BASE_URL= "http://localhost:";
	private static final String API_BASE = "/api/employees";
	
	@LocalServerPort
	private int serverPort;
	
	private static RestTemplate restTemplate;
	private static ObjectMapper objectMapper;
	
    @Test
    void contextLoads() {
    }
    
    @BeforeAll
	static void setup() {
		restTemplate = new RestTemplate();
		objectMapper = new ObjectMapper();
	}
    
    private StringBuilder getBaseApiUrl() {
        return new StringBuilder().append(LOCAL_HOST_BASE_URL).append(serverPort).append(API_BASE);
    }
	
    @Test
    @DisplayName("Invalid URL test")
    public void testInvalidUrl() throws URISyntaxException {
    	System.out.println("serverPort = " + serverPort);
    	StringBuilder invalURI = new StringBuilder().append(LOCAL_HOST_BASE_URL).
    			append(serverPort).append("/abcd");
    
        URI uri = new URI(invalURI.toString());
        assertThrows(HttpClientErrorException.class,
                ()-> restTemplate.getForEntity(uri, String.class));
    }
    
    @Test
    @DisplayName("Get all employees list")
    public void testGetAllEmployeesAPI() throws URISyntaxException, JsonProcessingException {
        URI uri = new URI(getBaseApiUrl().toString());
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Employee>>(){});

        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        List<Employee> employeeList = objectReader.readValue(result.getBody());
        Assertions.assertNotNull(employeeList);
        Assertions.assertEquals(24, employeeList.size());
    }
    
    @Test
    @DisplayName("Search employee API with search String")
    public void testSearchEmployeeBySearchString() throws URISyntaxException, JsonProcessingException {
        URI uri = new URI(getBaseApiUrl().append("/search/lle").toString());
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Employee>>(){});

        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        List<Employee> employeeList = objectReader.readValue(result.getBody());
        Assertions.assertNotNull(employeeList);
        employeeList.forEach(emp->{
            Assertions.assertTrue(emp.getName().toLowerCase().contains("lle"));
        });
    }
    
    @Test
    @DisplayName("Fetch employee details API with ID")
    public void testFetchEmployeeWithValidID() throws URISyntaxException, JsonProcessingException {
        URI uri = new URI(getBaseApiUrl().append("/3").toString());
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<Employee>(){});
        Employee expectedEmployee = new Employee(3L, "Ashton Cox", 86000, 66, "");

        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        Employee actualEmployee = objectReader.readValue(result.getBody());
        Assertions.assertNotNull(actualEmployee);
        Assertions.assertEquals(expectedEmployee, actualEmployee);
    }

    @Test
    @DisplayName("Fetch employee details API with Invalid ID")
    public void testFetchEmployeeWithInvalidID() throws URISyntaxException, JsonProcessingException {
    	String id = "89345";
        URI uri = new URI(getBaseApiUrl().append("/").append(id).toString());

        HttpClientErrorException ex = assertThrows(HttpClientErrorException.class,
                ()-> restTemplate.getForEntity(uri, String.class));
        Assertions.assertTrue(ex.getMessage().contains(String.format(MessageConstants.EMP_NOT_FOUND_ERROR_MESSAGE, id)));
    }
    

    @Test
    @DisplayName("highest salary of employee among all test")
    public void testHighestSalaryOfEmployeesAPI() throws URISyntaxException {
        URI uri = new URI(getBaseApiUrl().append("/highestSalary").toString());

        ResponseEntity<Integer> result = restTemplate.getForEntity(uri, Integer.class);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals(725000, result.getBody());
    }

    @Test
    @DisplayName("Top ten highest earning employee list API test")
    public void testTopTenHighestEarningEmployeeNames() throws URISyntaxException, JsonProcessingException {
        URI uri = new URI(getBaseApiUrl().append("/topTenHighestEarningEmployeeNames").toString());
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<String>>(){});
        List<String> expectedNameList = Arrays.asList("Paul Byrd", "Yuri Berry",
                "Charde Marshall", "Cedric Kelly", "Tatyana Fitzpatrick", "Brielle Williamson",
                "Jenette Caldwell", "Quinn Flynn", "Rhona Davidson", "Tiger Nixon");

        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        List<String> actualNameList = objectReader.readValue(result.getBody());
        Assertions.assertNotNull(actualNameList);
        Assertions.assertEquals(10, actualNameList.size());
        Assertions.assertEquals(expectedNameList, actualNameList);
    }


    @Test
    @DisplayName("Create employee API test with all valid inputs")
    public void testCreateEmployee() throws URISyntaxException {
        URI uri = new URI(getBaseApiUrl().toString());

        Map<String, Object> inputRequestPayload = new HashMap<>();
        inputRequestPayload.put("name", "Jon Doe");
        inputRequestPayload.put("salary", 30120);
        inputRequestPayload.put("age", 30);

        ResponseEntity<Employee> result = restTemplate.postForEntity(uri, inputRequestPayload, Employee.class);

        //assertions
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        Employee actualEmployee = result.getBody();
        Assertions.assertNotNull(actualEmployee);
        Assertions.assertNotNull(actualEmployee.getId());
        Assertions.assertEquals(inputRequestPayload.get("name"), actualEmployee.getName());
        Assertions.assertEquals(inputRequestPayload.get("age"), actualEmployee.getAge());
        Assertions.assertEquals(inputRequestPayload.get("salary"), actualEmployee.getSalary());
    }

    @Test
    @DisplayName("Create employee name missing in input")
    public void testCreateEmployeeNameMissing() throws URISyntaxException {
        URI uri = new URI(getBaseApiUrl().toString());
        Map<String, Object> inputRequestPayload = new HashMap<>();
        inputRequestPayload.put("salary", 30120);
        inputRequestPayload.put("age", 30);

        HttpClientErrorException ex = assertThrows(HttpClientErrorException.class,
                ()-> restTemplate.postForEntity(uri, inputRequestPayload, Employee.class));

        Assertions.assertTrue(ex.getMessage().contains(MessageConstants.NAME_FIELD_MISSING_MESSAGE));
    }

    @Test
    @DisplayName("Create employee age missing API test")
    public void testCreateEmployeeAgeMissing() throws URISyntaxException {
        URI uri = new URI(getBaseApiUrl().toString());
        Map<String, Object> inputRequestPayload = new HashMap<>();
        inputRequestPayload.put("name", "Jon Doe");
        inputRequestPayload.put("salary", 300120);

        HttpClientErrorException ex = assertThrows(HttpClientErrorException.class,
                ()-> restTemplate.postForEntity(uri, inputRequestPayload, Employee.class));

        Assertions.assertTrue(ex.getMessage().contains(MessageConstants.AGE_FIELD_MISSING_MESSAGE));
    }

    @Test
    @DisplayName("Create employee salary missing API test")
    public void testCreateEmployeeSalaryMissing() throws URISyntaxException {
        URI uri = new URI(getBaseApiUrl().toString());
        Map<String, Object> inputRequestPayload = new HashMap<>();
        inputRequestPayload.put("name", "Jon Doe");
        inputRequestPayload.put("age", 30);

        HttpClientErrorException ex = assertThrows(HttpClientErrorException.class,
                ()-> restTemplate.postForEntity(uri, inputRequestPayload, Employee.class));

        Assertions.assertTrue(ex.getMessage().contains(MessageConstants.SALARY_FIELD_MISSING_ERROR_MESSAGE));
    }

    @Test
    @DisplayName("Create employee invalid salary in input")
    public void testCreateEmployeeInvalidSalary() throws URISyntaxException {
        URI uri = new URI(getBaseApiUrl().toString());
        Map<String, Object> inputRequestPayload = new HashMap<>();
        inputRequestPayload.put("salary", -1);
        inputRequestPayload.put("age", 30);
        inputRequestPayload.put("name", "Jon Doe");

        HttpClientErrorException ex = assertThrows(HttpClientErrorException.class,
                ()-> restTemplate.postForEntity(uri, inputRequestPayload, Employee.class));

        Assertions.assertTrue(ex.getMessage().contains(MessageConstants.INVALID_SALARY_FIELD_VALIDATION_MESSAGE));
    }
    @Test
    @DisplayName("Create employee invalid age in input")
    public void testCreateEmployeeInvalidAge() throws URISyntaxException {
        URI uri = new URI(getBaseApiUrl().toString());
        Map<String, Object> inputRequestPayload = new HashMap<>();
        inputRequestPayload.put("salary", 1000);
        inputRequestPayload.put("age", -1);
        inputRequestPayload.put("name", "Jon Doe");

        HttpClientErrorException ex = assertThrows(HttpClientErrorException.class,
                ()-> restTemplate.postForEntity(uri, inputRequestPayload, Employee.class));

        Assertions.assertTrue(ex.getMessage().contains(MessageConstants.INVALID_AGE_FIELD_VALIDATION_MESSAGE));
    }

    @Test
    @DisplayName("Delete employee by Id API test")
    public void testDeleteEmployeeById() throws URISyntaxException {
        URI uri = new URI(getBaseApiUrl().append("/23").toString());
        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.DELETE,null,String.class);
        Assertions.assertEquals("Caesar Vance", result.getBody());
    }
    
    @Test
    @DisplayName("Delete employee by invalid Id API test")
    public void testDeleteEmployeeByInvalidId() throws URISyntaxException {
    	final String id = "23456";
        URI uri = new URI(getBaseApiUrl().append("/").append(id).toString());
        HttpClientErrorException ex = assertThrows(HttpClientErrorException.class,
                ()-> restTemplate.exchange(uri, HttpMethod.DELETE,null,String.class));
        Assertions.assertTrue(ex.getMessage().contains(String.format(MessageConstants.EMP_NOT_FOUND_ERROR_MESSAGE, id)));
    }
}
