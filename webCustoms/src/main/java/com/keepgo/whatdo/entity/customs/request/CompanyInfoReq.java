package com.keepgo.whatdo.entity.customs.request;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class CompanyInfoReq {

	private Long id;
	private String coNum;
	private String coNm;
	private String coAddress;
	private List<Long> exports = new ArrayList<>();
	private List<Long> manages = new ArrayList<>();
	private String coInvoice;
	private Boolean isUsing;
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date createDt;
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date updateDt;
	private User user;
	
	private List<Long> ids = new ArrayList<>();
	
//	private List<Long> manages;
//	private List<Long> manages;
	
	//공통 컬럼.
	
}
