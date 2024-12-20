package com.keepgo.whatdo.entity.customs.response;

import java.util.Date;

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
import lombok.Builder.Default;
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
public class InboundRes {

	
	private Long id;
	private Long inboundMasterId;
	private Long coId;
	private int orderNo;
	private String orderNoStr;
	private String color;
	private String colorCode;
	private Long colorId;
	private String backgroundColor;
	private String backgroundColorCode;
	private Long backgroundColorId;
	private String masterCompany;
	private String masterCompanyNumber;
	private String masterExport;
	private String masterExportAddr;
	private String cbmStr;
	private String currencyType;
	
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
	private Date workDate;
	
	private String workDateStr;
	//미리보기용 작업일자
	private String forViewWorkDateStr;
	//미리보기용 작업일자
	private String forViewWorkDate;
//	//발송업체 회사
//	@JoinColumn(name = "companyInfoId")
//	@ManyToOne(fetch = FetchType.LAZY)
//	private Common companyInfo;
//	
	//발송업체 //0번
	private String managerNm;
	private String companyNm;
	private String korNm;
	private String marking;
	@Builder.Default
	private String companyNmYn="";
	@Builder.Default
	private String markingYn="";
	@Builder.Default
	private String workDateStrYn="";
	@Builder.Default
	private String korNmYn="";
	@Builder.Default
	private String itemCountYn="";
	@Builder.Default
	private String boxCountYn="";
	@Builder.Default
	private String weightYn="";
	@Builder.Default
	private String cbmYn="";
	@Builder.Default
	private String reportPriceYn="";
	@Builder.Default
	private String memo1Yn="";
	@Builder.Default
	private String memo2Yn="";
	@Builder.Default
	private String itemNoYn="";
	@Builder.Default
	private String jejilYn="";
	@Builder.Default
	private String hsCodeYn="";
	@Builder.Default
	private String totalPriceYn=""; 
	@Builder.Default
	private String engNmYn="";
	@Builder.Default
	private String coIdYn="";
	//수량
	private Double itemCount;
	//박스수
	private Double boxCount;
	//무게
	private Double weight;
	
	private Double cbm;
	//신고단가
	private Double reportPrice;
	//비고1
	private String memo1;
	
	
	private String memo2;
	
	private String memo3;
	//품번
	private String itemNo;
	
	//세번
	private String hsCode;
	
//	@JoinColumn(name = "coId")
//	@ManyToOne(fetch = FetchType.LAZY)
//	private Common co;
	
	private boolean coExistYn;
	private String coCode;
	private String coNm;
	
	private Double totalPrice;
	
	private String engNm;
	
	//shipper
	private String exportNm;

	
	
	//shipper address
	private String exportAddress;
	private String businessman;
	
	private String blNo;
	
	private String jejil;
	private String amountType;
//	
//	//담당자
//	@JoinColumn(name = "manageId")
//	@ManyToOne(fetch = FetchType.LAZY)
//	private Common manage;
	
	//manage 이름
	private String manageName;

	//manage 직급
	private String manageLevel;
	

	
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
	@Builder.Default
	private int workDateStrSpan=1;
	@Builder.Default
	private int orderNoStrSpan=1;
	@Builder.Default
	private int jejilSpan=1;
	
	
	private Double itemCountSum;
	private Double boxCountSum;
	private Double weightSum;
	private Double cbmSum;
	private Double totalPriceSum;
	
	
	private String freight;
	
	private int freightCode;
	
//	@Override
//	public String toString() {
////		return "workData " + getWorkDate() +" : " + getWorkDateSpan();
//		return "order : "+orderNo+ "- workDate :" + workDate +" - " + workDateSpan
//				+ "- companyNm :" + companyNm +" - " + companyNmSpan
//				+ "- exportNm :" + (exportNm != null ? exportNm.substring(0, 5):exportNm) +" - " + exportNmSpan
//				+ "- blNo :" + blNo +" - " + blNoSpan
//				+ "- memo1 :" + memo1 +" - " + memo1Span;
//	}
	
	
	
}
