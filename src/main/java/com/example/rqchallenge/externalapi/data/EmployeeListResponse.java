package com.example.rqchallenge.externalapi.data;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class EmployeeListResponse extends BaseResponse {
	List<EmployeeData> data;
}
