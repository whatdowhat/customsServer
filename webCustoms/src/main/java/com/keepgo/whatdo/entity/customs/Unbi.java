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
@Table(name = "web_unbi")
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@ToString
public class Unbi {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(name = "orderNo")
	private int orderNo;
	
	@Column(name = "no")
	private int no;
	
	@Column(name = "noStr")
	private String noStr;
	//작업일자
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
	@Column(name = "workDate")
	private Date workDate;
	
	@Column(name = "workDateStr")
	private String workDateStr;
	//발송업체
	@Column(name = "companyNm")
	private String companyNm;
	//마킹
	@Column(name = "marking")
	private String marking;
	//한글품명
	@Column(name = "korNm")
	private String korNm;
	//출발지
	@Column(name = "departPort")
	private String departPort;
	//운비
	@Column(name = "unbi")
	private Double unbi;
	@Column(name = "unbiYn")
	private String unbiYn;
	//픽업비
	@Column(name = "pickupCost")
	private Double pickupCost;
	@Column(name = "pickupCostYn")
	private String pickupCostYn;
	//상하차비
	@Column(name = "sanghachaCost")
	private Double sanghachaCost;
	@Column(name = "sanghachaCostYn")
	private String sanghachaCostYn;
	//화물회사 사무실
	@Column(name = "officeName")
	private Double officeName;
	@Column(name = "officeNameYn")
	private String officeNameYn;
	//기타잡비
	@Column(name = "etcCost")
	private Double etcCost;
	//핵소단신고비
	@Column(name = "hacksodanCost")
	private Double hacksodanCost;
	//co발급비
	@Column(name = "coCost")
	private Double coCost;
	//화주미청구 운비
	@Column(name = "hwajumiUnbi")
	private Double hwajumiUnbi;
	//화주미청구 픽업비
	@Column(name = "hwajumiPickupCost")
	private Double hwajumiPickupCost;
	//컨테이너 작업비1
	@Column(name = "containerWorkCost")
	private Double containerWorkCost;
	//컨테이너 작업비1
	@Column(name = "containerWorkCost2")
	private Double containerWorkCost2;
	//컨테이너 이동비
	@Column(name = "containerMoveCost")
	private Double containerMoveCost;
	
	
	//typebTitle(발송업체,마킹,한글품명,출발지) mergecolumn
	@Column(name = "typebTitle")
	private String typebTitle;
	
	//비고1
	@Column(name = "memo1")
	private String memo1;
	
	//비고2
	@Column(name = "memo2")
	private String memo2;
	//구분
	@Column(name = "type")
	private String type;
	//색상코드
	@Column(name = "color")
	private Integer color;
	
	@JoinColumn(name = "finalInboundId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private FinalInbound finalInbound;

	
	
}
