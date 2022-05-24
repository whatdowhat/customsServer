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
public class ExcelInpackSubRes {

	//no
	private int orderNo;
	private String engNm;
	private String jejil;
	private String hsCode;
	private Double boxCount;
	private Double itemCount;
	private Double itemPrice;
	private Double totalPrice;
	private Double weight;
	private Double totalWeight;
	private Double cbm;
	
	private String currencyType;
	private String packingType;
}
