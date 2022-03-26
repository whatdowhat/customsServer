package com.keepgo.whatdo.entity.customs.request;

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
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InboundReq {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "orderNo")
	private int orderNo;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	@Column(name = "workDate")
	private Date workDate;

	
	//발송업체 회사
	@JoinColumn(name = "companyInfoId")
	@ManyToOne(fetch = FetchType.LAZY)
	private Common companyInfo;
	
	
	@Column(name = "companyNm")
	private String companyNm;
	
	
	
	@Column(name = "marking")
	private String marking;
	
	
	
	@Column(name = "korNm")
	private String korNm;
	
	@Column(name = "itemCount")
	private Double itemCount;
	
	@Column(name = "boxCount")
	private Double boxCount;
	
	@Column(name = "weight")
	private Double weight;
	
	@Column(name = "cbm")
	private Double cbm;
	
	@Column(name = "reportPrice")
	private Double reportPrice;
	
	@Column(name = "memo1")
	private String memo1;
	
	
	@Column(name = "memo2")
	private String memo2;
	
	@Column(name = "memo3")
	private String memo3;
	
	@Column(name = "itemNo")
	private String itemNo;
	
	//세번
	@Column(name = "hsCode")
	private String hsCode;
	
	@JoinColumn(name = "coId")
	@ManyToOne(fetch = FetchType.LAZY)
	private Common co;
	
	@Transient
//	@Column(name = "coYn")
	private boolean coYn;
	@Transient
//	@Column(name = "coCode")
	private String coCode;
	@Transient
//	@Column(name = "coCode")
	private String coNm;
	
	@Column(name = "totalPrice")
	private Double totalPrice;
	
	@Column(name = "engNm")
	private String engNm;
	
	//shipper
	@Transient
	private String exportNm;

	
	
	//shipper address
	@Transient
	private String exportAddress;
	
	@JoinColumn(name = "exportId")
	@ManyToOne(fetch = FetchType.LAZY)
	private Common exportId;
	
	@Column(name = "businessman")
	private String businessman;
	
	@Column(name = "blNo")
	private String blNo;

	
	//담당자
	@JoinColumn(name = "manageId")
	@ManyToOne(fetch = FetchType.LAZY)
	private Common manage;
	
	//manage 이름
	@Transient
	private String manageName;

	//manage 직급
	@Transient
	private String manageLevel;
	
	
	@Transient
	@Builder.Default
	private int memo1Span = 1;
	@Builder.Default
	@Transient
	private int companyNmSpan =1;
	@Transient
	@Builder.Default
	private int blNoSpan= 1;
	@Transient
	@Builder.Default
	private int exportNmSpan= 1;
	@Transient
	@Builder.Default
	private int markingSpan = 1;
	
	@Transient
	@Builder.Default
	private int workDateSpan = 1;

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
