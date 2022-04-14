package com.keepgo.whatdo.entity.customs.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.keepgo.whatdo.entity.customs.Common;

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
public class InboundViewRes {

	
//	private Long id;
//	private Long inboundMasterId;
//	private Long coId;
//	private int orderNo;
//	private String color;
//	private Long colorCode;
//	private String cbmStr;
//	private String korNm;
//	//비고1
//	private String memo1;
//	private String memo2;
//	private String memo3;
//	//품번
//	private String itemNo;	
//	//세번
//	private String hsCode;
//	//수량
//	private String itemCount;
//	//박스수
//	private String boxCount;
//	//무게
//	private String weight;
//	
//	private String cbm;
//	//신고단가
//	private String reportPrice;
//	private boolean coYn;
//	private String coCode;
//	private String coNm;
//	
//	private String totalPrice;
//	
//	private String engNm;
//	
//	//shipper
//	private String exportNm;
//	//manage 이름
//	private String manageName;
//	//manage 직급
//	private String manageLevel;
//	
//	
//	//shipper address
//	private String exportAddress;
//	private String businessman;
	
	
//	private String masterCompany;
//	private String masterCompanyNumber;
//	private String masterExport;
//	private String masterExportAddr;
	//미리보기용 작업일자
//	private String forViewWorkDate;
//	private String companyNm;
//	private String marking;
//	private String blNo;
	
	
	private String itemCountSum;
	private String boxCountSum;
	private String weightSum;
	private String cbmSum;
	private String totalPriceSum;	
	private String freight;
	private int freightCode;
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
	private Date workDate;
	//발송업체 //0번
	private String managerNm;
	


	
	

	private List<Map<String,Object>> inbounds = new ArrayList();
	//발송업체
	@Builder.Default
	private int companyNmSpan =1;
	@Builder.Default
	private int markingSpan = 1;
	@Builder.Default
	private int korNmSpan = 1;
	@Builder.Default
	private int itemCountSpan = 1;	
	@Builder.Default
	private int boxCountSpan = 1;
	@Builder.Default
	private int weightSpan = 1;
	@Builder.Default
	private int cbmSpan = 1;
	@Builder.Default
	private int reportPriceSpan = 1;
	@Builder.Default
	private int memo1Span = 1;
	@Builder.Default
	private int memo2Span = 1;
	@Builder.Default
	private int memo3Span = 1;
	@Builder.Default
	private int itemNoSpan = 1;
	@Builder.Default
	private int hsCodeSpan = 1;
	@Builder.Default
	private int workDateSpan = 1;
	@Builder.Default
	private int blNoSpan= 1;
	@Builder.Default
	private int masterCompanySpan = 1;
	@Builder.Default
	private int masterExportSpan =1;
	@Builder.Default
	private int exportNmSpan= 1;
	
	@Builder.Default
	private int coCodeSpan = 1;
	@Builder.Default
	private int coIdSpan = 1;
	@Builder.Default
	private int totalPriceSpan=1;
	@Builder.Default
	private int engNmSpan=1;
	
	
	
	

	
	
	
}
