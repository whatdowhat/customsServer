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
@Table(name = "web_final_inbound")
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FinalInbound {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 타이틀
	@Column(name = "title")
	private String title;
	
	// 구분
	@Column(name = "gubun")
	private int gubun;
	
	// 입항일
	@Column(name = "incomeDt")
	private String incomeDt;
	
	// 출발일
	@Column(name = "departDt")
	private String departDtStr;

	// 마스터비엘
	@Column(name = "finalMasterBl")
	private String finalMasterBl;

	// containerNo
	@Column(name = "containerNo")
	private String containerNo;

	
	// 항명
	@Column(name = "hangName")
	private String hangName;

	// 실번호
	@Column(name = "silNo")
	private String silNo;

	// 배정창고
	@Column(name = "cargoName")
	private String cargoName;

	// 출항항구
	@Column(name = "departPort")
	private Long departPort;
	
	// 도착항구
	@Column(name = "incomePort")
	private Long incomePort;
	

	// 항차
	@Column(name = "hangCha")
	private String hangCha;
	
	// container가격
	@Column(name = "containerPrice")
	private Double containerPrice;
	
	
	//중국 상검
	@Column(name = "chinaSanggumYn")
	private int chinaSanggumYn;
	
	//출항 지연
	@Column(name = "departDelayYn")
	private int departDelayYn;
	
	//관리대상지정
	@Column(name = "gwanriYn")
	private int gwanriYn;
	
	
	@OneToMany(cascade = { CascadeType.REMOVE })
	@JoinColumn(name = "finalInboundId")
	@JsonManagedReference // 추가
	private List<FinalInboundInboundMaster> inboundMasters = new ArrayList<>();

	// 공통 컬럼.
	@ExcelColumn(headerName = "사용여부", order = 4)
	@Column(name = "isUsing", nullable = false, columnDefinition = "tinyint(1) default 1")
	private Boolean isUsing;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	@Column(name = "createDt")
	private Date createDt;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	@Column(name = "updateDt")
	private Date updateDt;

	@ExcelColumn(headerName = "사용자", order = 5)
	@OneToOne
	@JoinColumn(name = "id")
	private User user;
	// 공통 컬럼.
	
	@Column(name = "unbiDefine1")
	private Integer unbiDefine1; //운비
	
	@Column(name = "unbiDefine2")
	private Integer unbiDefine2; //픽업비
	
	@Column(name = "unbiDefine3")
	private Integer unbiDefine3; //상하차비
	
	@Column(name = "unbiDefine4")
	private Integer unbiDefine4; //기타잡비
	
	// 컨테이너 사이즈
	@Column(name = "containerSizeStr")
	private String containerSizeStr;
	//컨테이너 비용
	@Column(name = "containerCost")
	private String containerCost;
	// 중국 작업 당시 날씨
	@Column(name = "weatherCondition")
	private String weatherCondition;
	
	// 작업지 출발일
	@Column(name = "workDepartDt")
	private String workDepartDt;
	// 운송처
	@Column(name = "deliveryNm")
	private String deliveryNm;
	// 출항항구
	@Column(name = "chulhangPort")
	private String chulhangPort;
	
	@Column(name = "memo")
	private String memo;
}
