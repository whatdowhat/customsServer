package com.keepgo.whatdo.entity.customs.request;

import java.util.Date;
import java.util.List;

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
public class UnbiReq {

	
	private Long id;
	private int orderNo;
	
	private int no;
	private String noStr;
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
	private Date workDate;
	private String workDateStr;
	
	
	private Integer unbiDefine1; //운비
	private Integer unbiDefine2; //픽업비
	private Integer unbiDefine3; //상하차비
	private Integer unbiDefine4; //기타잡비
	
	
	//typebTitle(발송업체,마킹,한글품명,출발지) mergecolumn
	private String typebTitle;
	
	private String companyNm;
	private String marking;
	private String korNm;
	private String departPort;
	private Double unbi;
	private Double pickupCost;
	private Double sanghachaCost;
	private Double officeName;
	private Double etcCost;
	private Double hacksodanCost;
	private Double coCost;
	private Double hwajumiUnbi;
	private Double hwajumiPickupCost;
	private Double containerWorkCost;
	private Double containerWorkCost2;
	private Double containerMoveCost;
	private String memo1;
	private String memo2;
	private String color;
	
	private String unbiYn;
	private String pickupCostYn;
	private String sanghachaCostYn;
	private String officeNameYn;
	private String hacksodanCostYn;
	private String coCostYn;
	//합계
	private Double totalSum;
	private Double officeNameSum;
	private Double unbiSum;
	private Double pickupCostSum;
	private Double sanghachaCostSum;
	private Double etcCostSum;
	private Double hacksodanCostSum;
	private Double coCostSum;
	private Double hwajumiUnbiSum;
	private Double hwajumiPickupCostSum;
	private Double containerWorkCostSum;
	private Double containerWorkCost2Sum;
	private Double containerMoveCostSum;
	private String type;
	private Long finalInboundId;
	List<UnbiReq> unbiReqData;
	
	
	
}
