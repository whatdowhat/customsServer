package com.keepgo.whatdo.entity.customs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.keepgo.whatdo.util.ExcelColumn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "web_companyInfo")
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class CompanyInfo {

	@Id
	@Column(name = "id")
//	@ExcelColumn(headerName="id",order = 1)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ExcelColumn(headerName="사업자번호",order = 1)
	@Column(name = "coNum")
	private String coNum;
	
	@ExcelColumn(headerName="업체이름",order = 2)
	@Column(name = "coNm")
	private String coNm;

	@ExcelColumn(headerName="업체이름영문",order = 2)
	@Column(name = "coNmEn")
	private String coNmEn;
	
	@ExcelColumn(headerName="컨사이니코드",order = 2)
	@Column(name = "consignee")
	private String consignee;
	
	@ExcelColumn(headerName="사업자주소지",order = 3)
	@Column(name = "coAddress")
	private String coAddress;
	
	@OneToMany( cascade = {CascadeType.REMOVE})
	@JoinColumn(name = "companInfoyId")
	@ExcelColumn(headerName="수출자(쉬퍼)",order = 4)
	@JsonManagedReference  // 추가
	private List<CompanyInfoExport> exports = new ArrayList<>();
	
//	@OneToMany( cascade = {CascadeType.REMOVE})
//	@JoinColumn(name = "companInfoyId")
//	@ExcelColumn(headerName="담당자",order = 4)
//	@JsonManagedReference  // 추가
//	private List<CompanyInfoManage> manages = new ArrayList<>();
	
	@ExcelColumn(headerName="인보이스",order = 5)
	@Column(name = "coInvoice")
	private String coInvoice;

	@Column(name = "manager")
	private String manager;
	
	@ExcelColumn(headerName="포워딩부호",order = 5)
	@Column(name = "forwarding")
	private String forwarding;
	
	@Transient
	private String managerLevel;
	@Transient
	private String shopper;
	@Transient
	private String shopperAdress;
	
	//공통 컬럼.
	@ExcelColumn(headerName="사용여부",order = 4)
	@Column(name = "isUsing",nullable = false,columnDefinition="tinyint(1) default 1")
	private Boolean isUsing;
	
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
	@Column(name = "createDt")
	private Date createDt;
	
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
	@Column(name = "updateDt")
	private Date updateDt;
	
	@ExcelColumn(headerName="사용자",order = 5)
	@OneToOne
	@JoinColumn(name = "id")
	private User user;
	//공통 컬럼.
	
}
