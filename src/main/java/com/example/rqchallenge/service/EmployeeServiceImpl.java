package com.example.rqchallenge.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.exception.InvalidRequestException;
import com.example.rqchallenge.exception.OperationFailedException;
import com.example.rqchallenge.exception.ResourceNotFoundException;
import com.example.rqchallenge.externalapi.EmployeeAPIClient;
import com.example.rqchallenge.externalapi.data.EmployeeData;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeAPIClient emplAPIClient;
	
	@Override
	public List<Employee> getAllEmployees() {
		try {
			List<EmployeeData> emplDataList = emplAPIClient.getAllEmployees();
			
			if (emplDataList == null) {
				throw new OperationFailedException(MessageConstants.OP_FAILED_FETCH_EMP_LIST_ERROR_MESSAGE);
			}
			
			return emplDataList.
					stream().
					map(emplData -> EmployeeDataMapper.mapToEmployeeDto(emplData)).
					collect(Collectors.toList());
			
		} catch (Exception ex) {
			log.error("Exception while getting employee list", ex);
			throw ex;
		}
	}

	@Override
	public List<Employee> getEmployeesByNameSearch(String searchString) {
		try {
			if (searchString == null || searchString.isBlank()) {
				throw new InvalidRequestException(MessageConstants.INVALID_INPUT_PARAMETER_VALIDATION_MESSAGE);
			}
			
			// Get list of all employees and then filter the results because the
			// external endpoint doesn't have a search function of its own
			List<EmployeeData> emplDataList = emplAPIClient.getAllEmployees();
			
			if (emplDataList == null) {
				throw new OperationFailedException(MessageConstants.OP_FAILED_EMP_NAME_SEARCH_ERROR_MESSAGE);
			}
			
			return emplDataList.
					stream().
					// filter employees matching the name
					filter(emplData -> emplData.getName() != null && emplData.getName().contains(searchString)).
					map(emplData -> EmployeeDataMapper.mapToEmployeeDto(emplData)).
					collect(Collectors.toList());
			
		} catch (Exception ex) {
			log.error(String.format("Exception while getting employees matching search", searchString), ex);
			throw ex;
		}
	}

	@Override
	public Employee getEmployeeById(String id) {
		try {
			EmployeeData emplData = emplAPIClient.getEmployeeById(id);
			if (emplData == null) {
				throw new ResourceNotFoundException(String.format(MessageConstants.EMP_NOT_FOUND_ERROR_MESSAGE, id));
			}

			return EmployeeDataMapper.mapToEmployeeDto(emplData);
		} catch (Exception ex) {
			log.error(String.format("Exception while getting employee with id ", id), ex);
			throw ex;
		}
	}

	@Override
	public Integer getHighestSalaryOfEmployees() {
		try {
			// Need to get list of all employees and then filter the results because the
			// external endpoint doesn't have a query function with specific criteria
			List<EmployeeData> emplDataList = emplAPIClient.getAllEmployees();
			
			if (emplDataList == null) {
				throw new OperationFailedException(MessageConstants.OP_FAILED_MAX_SALARY_ERROR_MESSAGE);
			}

			return emplDataList.
					stream().
					map(emplData -> emplData.getSalary()).
					reduce(0, Integer::max);

		} catch (Exception ex) {
			log.error("Exception while getting highest employee salary", ex);
			throw ex;
		}
	}

	@Override
	public List<String> getTopTenHighestEarningEmployeeNames() {
		try {
			// Need to get list of all employees and then filter the results
			List<EmployeeData> emplDataList = emplAPIClient.getAllEmployees();
			
			if (emplDataList == null) {
				throw new OperationFailedException(MessageConstants.OP_FAILED_TOP_TEN_SALRIED_EMP_ERROR_MESSAGE);
			}
			
			return emplDataList.
					stream().
					// sort by salary DESC
					sorted((ed1, ed2) -> ed2.getSalary() - ed1.getSalary()).
					limit(10).
					map(emplData -> emplData.getName()).
					collect(Collectors.toList());
			
		} catch (Exception ex) {
			log.error("Exception while getting top 10 earning employees", ex);
			throw ex;
		}
	}

	@Override
	public Employee createEmployee(Map<String, Object> employeeInput) {
		try {
			validateEmpCreationInput(employeeInput);
			
			EmployeeData emplData = emplAPIClient.createEmployee(employeeInput);
			if (emplData == null) {
				throw new OperationFailedException(MessageConstants.OP_FAILED_CREATE_EMPLOYEE_ERROR_MESSAGE);
			}

		return EmployeeDataMapper.mapToEmployeeDto(emplData);
		} catch (Exception ex) {
			log.error("Exception while creating employee", ex);
			throw ex;
		}
	}
	
	// Perform various validations on data passed as creation input
	private void validateEmpCreationInput(Map<String, Object> employeeInput) throws InvalidRequestException {
		if (employeeInput == null || employeeInput.isEmpty()) {
			throw new InvalidRequestException(MessageConstants.INVALID_INPUT_PARAMETER_VALIDATION_MESSAGE);
		}
		
		String name = (String) employeeInput.get("name");
		if (name == null) {
			throw new InvalidRequestException(MessageConstants.NAME_FIELD_MISSING_MESSAGE);
		} else if (name.isBlank()) {
			throw new InvalidRequestException(MessageConstants.INVALID_NAME_FIELD_VALIDATION_MESSAGE);
		}
		
		Integer salary = (Integer) employeeInput.get("salary");
		if (salary == null) {
			throw new InvalidRequestException(MessageConstants.SALARY_FIELD_MISSING_ERROR_MESSAGE);
		} else if (salary < 0) {
			throw new InvalidRequestException(MessageConstants.INVALID_SALARY_FIELD_VALIDATION_MESSAGE);
		}
		
		Integer age = (Integer) employeeInput.get("age");
		if (age == null) {
			throw new InvalidRequestException(MessageConstants.AGE_FIELD_MISSING_MESSAGE);
		} else if (age < 0) {
			throw new InvalidRequestException(MessageConstants.INVALID_AGE_FIELD_VALIDATION_MESSAGE);
		}
	}
	
	@Override
	// Delete the employee by Id and return the name of the employee
	public String deleteEmployeeById(String id) {
		try {
			// First check whether the said employee exists
			EmployeeData emplData = emplAPIClient.getEmployeeById(id);
			if (emplData == null) {
				throw new ResourceNotFoundException(String.format(MessageConstants.EMP_NOT_FOUND_ERROR_MESSAGE, id));
			}
			
			// Now delete the employee
			String respMsg = emplAPIClient.deleteEmployeeById(id);
			if (respMsg == null) {
				throw new OperationFailedException(MessageConstants.DELETE_EMPLOYEE_FAILED_ERROR_MESSAGE);
			}
			return emplData.getName();
		} catch (Exception ex) {
			log.error(String.format("Exception while deleting employee with id ", id), ex);
			throw ex;
		}
	}

}
