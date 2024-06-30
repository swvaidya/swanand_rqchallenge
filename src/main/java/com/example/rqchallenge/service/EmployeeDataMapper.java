package com.example.rqchallenge.service;

import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.externalapi.data.EmployeeData;

/**
 * Mapper class to map employee object returned by external API
 * to the employee DTO that our application returns
 * This is to avoid coupling the two 
 */
public class EmployeeDataMapper {
	public static Employee mapToEmployeeDto(EmployeeData emplData) {
		return new Employee(
				emplData.getId(), 
				emplData.getName(), 
				emplData.getSalary(), 
				emplData.getAge(), 
				emplData.getProfileImage());
	}
}
