package com.keepgo.whatdo.entity.customs.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.keepgo.whatdo.entity.customs.CompanyInfoExport;
import com.keepgo.whatdo.entity.customs.CompanyInfoManage;
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
public class FinalInboundRes {

	private Long id;
	private String title;
	private String gubun;
	private int gubunCode;
	private String incomeYearMonth;
	private String incomeDt;
	private String departDtStr;
	private String forViewDepartDateStr;
	private String finalMasterBl;
	private String containerNo;
	private Double containerPrice;
	
	private String hangName;
	private String silNo;
	private String cargoName;
	
	private String hangCha;
	private String memo;
	private Integer corpId;
	private String corpType;
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
	private List<Map<String,Object>> inboundMasters = new ArrayList();
	
	
	private Integer unbiDefine1; //운비
	private Integer unbiDefine2; //픽업비
	private Integer unbiDefine3; //상하차비
	private Integer unbiDefine4; //기타잡비
	
	
	private String containerSizeStr;
	private String containerCost;
	private String weatherCondition;
	private String workDepartDt;
	private String forViewWorkDepartDt;
	private String deliveryNm;
	private String chulhangPort;
	
	
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
