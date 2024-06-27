package com.example.rqchallenge.service;

import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.externalapi.data.EmployeeData;

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
