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
public class ExcelFTASubRes {

	//no
	private int orderNo;
	private String making;
	private String engNm;
	private String hsCode;
	private String data10;
	private Double itemCount;
	private Double boxCount;
	private String companyInvoice;
	
	private String departDtStr;
	
	
}
