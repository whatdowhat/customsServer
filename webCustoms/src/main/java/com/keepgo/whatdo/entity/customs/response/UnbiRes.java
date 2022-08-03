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
public class UnbiRes {

	
	private Long id;
	private int orderNo;
	private int no;
	private String noStr;
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
	private Date workDate;
	private String workDateStr;
	
	
	private String companyNm;
	private String marking;
	private String korNm;
	private String departPort;
	
	private String memo1;
	private String memo2;
	private String colorCode;
	private Long colorId;
	
	//typebTitle(발송업체,마킹,한글품명,출발지) mergecolumn
	private String typebTitle;
	
	private String unbi;
	private String officeName;
	private String pickupCost;
	private String sanghachaCost;
	private String etcCost;
	private String hacksodanCost;
	private String coCost;
	private String hwajumiUnbi;
	private String hwajumiPickupCost;
	private String containerWorkCost;
	private String containerWorkCost2;
	private String containerMoveCost;
	
	private Double unbiD;
	private Double officeNameD;
	private Double pickupCostD;
	private Double sanghachaCostD;
	private Double etcCostD;
	private Double hacksodanCostD;
	private Double coCostD;
	private Double hwajumiUnbiD;
	private Double hwajumiPickupCostD;
	private Double containerWorkCostD;
	private Double containerWorkCost2D;
	private Double containerMoveCostD;
	
	private String unbiYn;
	private String pickupCostYn;
	private String sanghachaCostYn;
	private String officeNameYn;
	private String hacksodanCostYn;
	private String coCostYn;
	//합계
	private Double totalSum;
	private Double totalSumFinal;
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
	private Double officeNameSum;
	private String totalSums;
	private String totalSumFinals;
	private String unbiSums;
	private String pickupCostSums;
	private String sanghachaCostSums;
	private String etcCostSums;
	private String hacksodanCostSums;
	private String coCostSums;
	private String hwajumiUnbiSums;
	private String hwajumiPickupCostSums;
	private String containerWorkCostSums;
	private String containerWorkCost2Sums;
	private String containerMoveCostSums;
	private String officeNameSums;
	private String type;
	private Long finalInboundId;
	
	
	
	
}
