package com.example.rqchallenge.externalapi.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class GenericResponse extends BaseResponse {
	String data;
}
