package com.keepgo.whatdo.entity.customs.request;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CompanyInfoExport;
import com.keepgo.whatdo.entity.customs.CompanyInfoManage;
import com.keepgo.whatdo.entity.customs.InboundMaster;
import com.keepgo.whatdo.entity.customs.User;

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
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class FinalInboundReq {

	private Long id;
	private int gubun;
	private String title;
	private String incomeYearMonth;
	private String incomeDt;
	private String departDtStr;
	private String finalMasterBl;
	private String containerNo;
	private Double containerPrice;
	private String hangName;
	private String silNo;
	private String cargoName;
	private String hangCha;
	private String memo;
	private Integer corpId;
	
	private Date incomeDate;
	private Date departDate;
	//중국 상검
	private int chinaSanggumYn;
	//출항 지연
	private int departDelayYn;
	//관리대상지정
	private int gwanriYn;
	//도착항구
	private String incomePort;
	private Long incomePortId;
	
	private String departPort;
	private Long departPortId;
	private List<Long> inboundMasters = new ArrayList<>();
	
	
	private String containerSizeStr;
	private String containerCost;
	private String weatherCondition;
	private String workDepartDt;
	private Date workDepartDate;
	private String deliveryNm;
	private String chulhangPort;
	
	List<InboundMasterReq> inboundMasterData;
	List<FinalInboundReq> finalInboundReqData;
	private List<Long> ids = new ArrayList<>();
	
	//조인컬럼 삭제용
	private Long inboundMasterId;
	
	private Boolean isUsing;
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
	private Date createDt;
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
	private Date updateDt;
	private User user;
	//공통 컬럼.
	
}
