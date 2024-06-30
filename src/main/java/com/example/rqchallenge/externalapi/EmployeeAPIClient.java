package com.example.rqchallenge.externalapi;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.rqchallenge.externalapi.data.EmployeeData;
import com.example.rqchallenge.externalapi.data.EmployeeListResponse;
import com.example.rqchallenge.externalapi.data.EmployeeResponse;
import com.example.rqchallenge.externalapi.data.GenericResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class EmployeeAPIClient {
	private final RestClient restClient;

	public List<EmployeeData> getAllEmployees() {
		String url = EmployeeAPIConstants.EXTERNAL_API_URL + EmployeeAPIConstants.EXTERNAL_API_GET_ALL_EMPLOYEES;
		ResponseEntity<EmployeeListResponse> responseEntity = restClient.execute(
				url, HttpMethod.GET,
				RestClient.getHttpEntity(), EmployeeListResponse.class);
		if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
			log.error("Failed to get employee list from url {}. Error: {}", url, responseEntity.getStatusCode());
			return null;
		}
		
		EmployeeListResponse emplListResponse = responseEntity.getBody();
		if (emplListResponse == null) {
			log.error("Empty employee list received from url {}", url);
			return null;
		}
		
		return emplListResponse.getData();
	}

	public EmployeeData getEmployeeById(String id) {
		String url = EmployeeAPIConstants.EXTERNAL_API_URL + String.format(EmployeeAPIConstants.EXTERNAL_API_GET_EMPLOYEE_BY_ID, id); 
		ResponseEntity<EmployeeResponse> responseEntity = restClient.execute(url, HttpMethod.GET,
				RestClient.getHttpEntity(), EmployeeResponse.class);
		if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
			log.error("Failed to get employee list from url {}. Error: {}", url, responseEntity.getStatusCode());
			return null;
		}
		
		EmployeeResponse emplResponse = responseEntity.getBody();
		if (emplResponse == null) {
			log.error("Empty employee response body received from url {}", url);
			return null;
		}
		
		return emplResponse.getData();
	}

	public EmployeeData createEmployee(Map<String, Object> employeeInput) {
		String url = EmployeeAPIConstants.EXTERNAL_API_URL + EmployeeAPIConstants.EXTERNAL_API_CREATE_EMPLOYEE;
		ResponseEntity<EmployeeResponse> responseEntity = restClient.execute(url,
				HttpMethod.POST, RestClient.getHttpEntity(employeeInput), EmployeeResponse.class);
		if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
			log.error("Failed to create employee from url {}. Error: {}", url, responseEntity.getStatusCode());
			return null;
		}
		EmployeeResponse emplResponse = responseEntity.getBody();
		if (emplResponse == null) {
			log.error("Empty employee response body received from url {}", url);
			return null;
		}
		return emplResponse.getData();
	}

	public String deleteEmployeeById(String id) {
		String url = EmployeeAPIConstants.EXTERNAL_API_URL + String.format(EmployeeAPIConstants.EXTERNAL_API_DELETE_EMPLOYEE, id);
		ResponseEntity<GenericResponse> responseEntity = restClient.execute(url,
				HttpMethod.DELETE, RestClient.getHttpEntity(), GenericResponse.class);
		if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
			log.error("Failed to delete employee from url {}. Error: {}", url, responseEntity.getStatusCode());
			return null;
		}
		
		GenericResponse genResp = responseEntity.getBody();
		if (genResp == null) {
			log.error("Empty employee response body received from url {}", url);
			return null;
		}
		
		if (!id.equals(genResp.getData())) {
			log.error("Invalid response received for employee deletion from url {} : {}", url, genResp.getData());
		}
		
		return genResp.getMessage();
	}

}
