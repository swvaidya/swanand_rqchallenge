package com.example.rqchallenge.externalapi.data;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeData {
    private Long id;

    @JsonAlias({"employee_name"})
    private String name;

    @JsonAlias({"employee_salary"})
    private Integer salary;

    @JsonAlias({"employee_age"})
    private Integer age;

    @JsonAlias({"profile_image"})
    private String profileImage;
}
