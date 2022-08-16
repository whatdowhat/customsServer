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
public class ExcelYATAISubRes {

	//no
	private int orderNo;
	private String hsCode;
	private String making;
	private String engNm;
	private Double boxCount;
	private String data08;
	private String itemCount;
	private Double itemCountD;
	private String amountType;
	private String companyInvoice;
	private String departDtStr;
	
	
}
