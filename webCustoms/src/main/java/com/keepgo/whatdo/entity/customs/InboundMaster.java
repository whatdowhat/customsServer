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
	
	@Column(name = "masterBlNo")
	private String masterBlNo;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	@Column(name = "incomDt")
	private Date incomDt;

	//창구
	@Column(name = "cargo")
	private String cargo;

	//도착항구
	@Column(name = "toHarbor")
	private String toHarbor;
	
	//출발항구
	@Column(name = "fromHarbor")
	private String fromHarbor;

	
	//컨테이너번호
	@Column(name = "containerNo")
	private String containerNo;
	
	//실번호
	@Column(name = "realNo")
	private String realNo;

	//항명
	@Column(name = "hangmyung")
	private String hangmyung;

	//항차
	@Column(name = "hangcha")
	private String hangcha;
	
	//작업형태
	@JoinColumn(name = "workTypeId")
	@ManyToOne(fetch = FetchType.LAZY)
	private Common workType;
	
	//중국상검
	@Column(name = "chinaSanggumYn")
	private Boolean chinaSanggumYn;
	
	//출항지연
	
	@Column(name = "departureDelayYn")
	private Boolean departureDelayYn;
	//관리대상지정
	@Column(name = "watchTargetYn")
	private Boolean watchTargetYn;
	

	//작업형태 비고 //코로드인경우 입력값을 받음.
	@JoinColumn(name = "commonId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private Common workTypeMemo;
	
	@JoinColumn(name = "companyInfoId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private CompanyInfo CompanyInfo;
	
	@Column(name = "orderNo")
	private Integer orderNo;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	@Column(name = "workDate")
	private Date workDate;

	
	@OneToMany
	@JoinColumn(name = "inboundId")
	@JsonManagedReference  // 추가
	private List<Inbound> inbounds = new ArrayList<>();
	
	//통관사업자
	@Column(name = "companyNm")
	private String companyNm;
	//수출자
	@Column(name = "export")
	private String export;
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
