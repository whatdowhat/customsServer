package com.keepgo.whatdo.entity.customs.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class CompanyInfoRes {

	private Long id;
	private String coNum;
	private String coNm;
	private String coAddress;
//	private Map<Long,String> exports = new HashMap<>();
//	private Map<Long,String> manages = new HashMap<>();
//	private Map<String,String> exports = new HashMap<>();
	private List<Map<String,Object>> exports = new ArrayList();
	private List<Map<String,Object>> managers = new ArrayList();
//	private Map<Long,String[]> exports = new HashMap<>();
//	private Map<Long,String[]> manages = new HashMap<>();
//	private List<CompanyInfoManage> manages = new ArrayList<>();
	private String coInvoice;
	private String manager;
	private Boolean isUsing;
	private String consignee;
	private String coNmEn;
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
	private Date createDt;
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
	private Date updateDt;
	private User user;
	//공통 컬럼.
	private Integer corpId;
	private String corpShowName;
	private String forwarding;
}
