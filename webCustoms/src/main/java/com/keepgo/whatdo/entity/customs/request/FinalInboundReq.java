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
	private String deliveryNm;
	private String hangName;
	private String silNo;
	private String cargoName;
	private String departPort;
	private String hangCha;
	private String containerSizeStr;
	private String weatherCondition;
	private List<Long> inboundMasters = new ArrayList<>();
	
	
	List<InboundMasterReq> inboundMasterData;
	
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
