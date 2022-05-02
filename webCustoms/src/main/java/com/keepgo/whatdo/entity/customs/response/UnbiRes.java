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
	private String officeName;
	private String memo1;
	private String memo2;
	
	private String unbi;
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
	private String type;
	private Long finalInboundId;
	
	
	
	
}
