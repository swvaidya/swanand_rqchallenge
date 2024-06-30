package com.example.rqchallenge.service;

public class MessageConstants {
    public static final String OP_FAILED_FETCH_EMP_LIST_ERROR_MESSAGE = "Failed to fetch employee list";
    public static final String EMP_NOT_FOUND_ERROR_MESSAGE = "No record found for employee with id %s";
    public static final String OP_FAILED_MAX_SALARY_ERROR_MESSAGE = "Failed to fetch highest salary of employees";
    public static final String OP_FAILED_EMP_NAME_SEARCH_ERROR_MESSAGE = "Failed to search employees having name matching with %s";
    public static final String OP_FAILED_TOP_TEN_SALRIED_EMP_ERROR_MESSAGE = "Failed to fetch top 10 highest earning employee names";
    public static final String NAME_FIELD_MISSING_MESSAGE = "Name value is missing in create employee request";
    public static final String SALARY_FIELD_MISSING_ERROR_MESSAGE = "Salary value is missing in create employee request";
    public static final String INVALID_SALARY_FIELD_VALIDATION_MESSAGE = "Invalid salary value is passed in create employee request";
    public static final String INVALID_NAME_FIELD_VALIDATION_MESSAGE = "Invalid name value is passed in create employee request";
    public static final String AGE_FIELD_MISSING_MESSAGE = "Age value is missing in create employee request";
    public static final String INVALID_AGE_FIELD_VALIDATION_MESSAGE = "Invalid age value is passed in create employee request";
    public static final String INVALID_INPUT_PARAMETER_VALIDATION_MESSAGE = "Invalid input parameter sent in request";
    public static final String OP_FAILED_CREATE_EMPLOYEE_ERROR_MESSAGE = "Failed to create employee";
    public static final String DELETE_EMPLOYEE_FAILED_ERROR_MESSAGE = "Failed to delete employee with id %s";
    public static final String GENERAL_ERROR_MESSAGE = "Error occurred. Please retry after sometime";
}
