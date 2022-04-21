package com.keepgo.whatdo.entity.customs.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommonbyCompanyRes {

	private Long id;
	private String name;
	private String value;
	private String value2;
	private String value3;
	private int preperOrder;
	private Long companyInfoExportId;
	private Long companInfoyId;
	private Boolean isChecked;
	
}

