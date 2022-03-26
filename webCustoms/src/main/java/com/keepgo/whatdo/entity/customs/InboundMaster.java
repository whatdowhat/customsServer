package com.keepgo.whatdo.entity.customs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.keepgo.whatdo.util.ExcelColumn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "web_inboundMaster")
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InboundMaster {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "blNo")
	private String blNo;
	
	@Column(name = "orderNo")
	private int orderNo;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	@Column(name = "workDate")
	private Date workDate;

	
	//발송업체 회사
	@JoinColumn(name = "companyInfoId")
	@ManyToOne(fetch = FetchType.LAZY)
	private Common companyInfo;
	
	@OneToMany
	@JoinColumn(name = "inboundMasterId")
	@JsonManagedReference  // 추가
	private List<Inbound> Inbounds = new ArrayList<>();
	
	//공통 컬럼.
	@ExcelColumn(headerName="사용여부",order = 4)
	@Column(name = "isUsing",nullable = false,columnDefinition="tinyint(1) default 1")
	private Boolean isUsing;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	@Column(name = "createDt")
	private Date createDt;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	@Column(name = "updateDt")
	private Date updateDt;
	
	@ExcelColumn(headerName="사용자",order = 5)
	@OneToOne
	@JoinColumn(name = "id")
	private User user;
//	//공통 컬럼.

//	@JoinColumn(name = "id")
//	@ManyToOne(fetch = FetchType.LAZY)
//	private User user;
	
}
