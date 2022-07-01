package com.keepgo.whatdo.entity.customs.request;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CompanyInfo;
import com.keepgo.whatdo.entity.customs.User;
import com.keepgo.whatdo.util.ExcelColumn;

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
public class CompanyInfoReq {
	@ExcelColumn(headerName="CompanyInfoId",order = 1)
	private Long id;
	@ExcelColumn(headerName="담당자",order = 2)
	private String manager;
	@ExcelColumn(headerName="coInvoice",order = 3)
	private String coInvoice;
	@ExcelColumn(headerName="사업자이름",order = 4)
	private String coNm;
	@ExcelColumn(headerName="사업자영문명",order = 5)
	private String coNmEn;
	@ExcelColumn(headerName="사업자번호",order = 6)
	private String coNum;
	@ExcelColumn(headerName="사업자주소",order = 7)
	private String coAddress;
	@ExcelColumn(headerName="consignee",order = 8)
	private String consignee;
	private List<Long> exports = new ArrayList<>();
//	private List<Long> manages = new ArrayList<>();
	
	
	private Boolean isUsing;
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
	private Date createDt;
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
	private Date updateDt;
	private User user;
	
	private Integer corpId;
	

	private List<Long> ids = new ArrayList<>();
	
	
	List<CompanyInfoReq> companyInfoReqData;
	//common -> commonreq로 수정 해야됨.
	List<CommonReq> companyExportData;
//	List<Common> companyManageData;
	
}
