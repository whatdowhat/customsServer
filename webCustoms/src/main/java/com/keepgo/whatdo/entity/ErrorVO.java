package com.keepgo.whatdo.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorVO {

	public final static String EXCEPTION = "EXCEPTION";
	public final static String PASSWORD_INCORRECT_CODE = "403_01";
	public final static String PASSWORD_INCORRECT = "PASSWORD_INCORRECT";

	public final static String EXPIREDJWT_CODE = "403_02";
	public final static String EXPIREDJWT = "EXPIREDJWT";
	
	private String errorCode;
	private String errorMessage;
	private Integer status;
}
