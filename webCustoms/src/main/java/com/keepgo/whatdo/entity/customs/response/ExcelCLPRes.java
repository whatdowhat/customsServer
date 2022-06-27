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
	@Builder.Default
	String container="백마창고";
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
	
	
	
}
