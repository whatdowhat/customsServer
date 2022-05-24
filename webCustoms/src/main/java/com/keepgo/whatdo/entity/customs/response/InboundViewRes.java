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

	

	
	private Long inboundMasterId;
	private Long companyId;
	private String itemCountSum;
	private String boxCountSum;
	private String weightSum;
	private String cbmSum;
	private Double itemCountSumD;
	private Double boxCountSumD;
	private Double weightSumD;
	private Double cbmSumD;
	private String totalPriceSum;	
	private Double itemCountSumFinal;
	private Double boxCountSumFinal;
	private Double cbmSumFinal;
	private Double weightSumFinal;
	private String freight;
	private int freightCode;
	
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
	private Date workDate;
	//발송업체 //0번
	private String managerNm;
	
	private String shipper;
	private String consignee;
	private String companyNum;
	private String notify;
	private String blNo;
	private int no;
	private int checkImportYn;
	private String memo;
	private String currencyType;
	private String packingType;

	private Long aTypeCount;
	private Long bTypeCount;
	private Long cTypeCount;
	private Long dTypeCount;
	private Long eTypeCount;
	private Long fTypeCount;
	private Long gTypeCount;
	private Long hTypeCount;

	private String aTypeNm;
	private String bTypeNm;
	private String cTypeNm;
	private String dTypeNm;
	private String eTypeNm;
	private String fTypeNm;
	private String gTypeNm;
	private String hTypeNm;
	
	private String aTypeFileNm;
	private String bTypeFileNm;
	private String cTypeFileNm;
	private String dTypeFileNm;
	private String eTypeFileNm;
	private String fTypeFileNm;
	private String gTypeFileNm;
	private String hTypeFileNm;
	
	
	
	private String aTypeInfo;
	private String bTypeInfo;
	private String cTypeInfo;
	private String dTypeInfo;
	private String eTypeInfo;
	private String fTypeInfo;
	private String gTypeInfo;
	private String hTypeInfo;
	private String fileTotalInfo;
	

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
	@Builder.Default
	private int workDateStrSpan=1;
	@Builder.Default
	private int orderNoStrSpan=1;
	@Builder.Default
	private int jejilSpan=1;
	
	

	
	
	
}
