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
public class ExcelBlRes {

	
	//excel 파일 이름
	String fileNm;
	//박스 포장단위
	String consignor;
	String shipper;
	String consignee;
	String consigneeAdd;
	String notifyParty;
	String vessel;
	String voyageNum;
	String portLoading;
	String portDischarge;
	String portDelivery;
	String finalDestination;
	String blNo;
	String delivery;
	String deliveryAdd;
	String marking;
	String ctns;
	String ctnsUnit;
	String goods;
	String weight;
	String cbm;
	String conNo;
	String silNo;
	String conSize;
	String departDt;
	String freight;
	String type;
	String totalNo;
	String originalBl;
	String packingType;
	String packingType2;
	String gubunType;
	
	
}
