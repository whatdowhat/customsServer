package com.keepgo.whatdo.entity.customs.response;

import java.util.List;

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
public class ExcelCLPRes {

	
	//excel 파일 이름
	String fileNm;
	Integer no;
	String incomeDt;
	
	String container;
	String chulhangPort;	
	String containerNo;
	String silNo;
	String companyNm;
	String blNo;
	String marking;
	Double boxCount;
	Double cbm;
	String engNm;
	String memo1;
	Integer corpId;
	@Builder.Default
	private int blNoSpan= 1;
	@Builder.Default
	private int companyNmSpan =1;
	@Builder.Default
	private int markingSpan = 1;
	@Builder.Default
	private int orderNoStrSpan=1;
	@Builder.Default
	private int masterCompanySpan = 1;
	
}
