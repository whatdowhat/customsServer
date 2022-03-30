package com.keepgo.whatdo.entity.customs.request;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CompanyInfo;
import com.keepgo.whatdo.entity.customs.Inbound;
import com.keepgo.whatdo.entity.customs.User;
import com.keepgo.whatdo.util.ExcelColumn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InboundMasterReq {

	
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
	
	private String workTypeId;
	
	//통관사업자
	private String companyNm;
	//수출자
	private String export;
	//중국상검
	@Column(name = "chinaSanggumYn")
	private boolean chinaSanggumYn;
	//출항지연
	@Column(name = "departureDelayYn")
	private boolean departureDelayYn;
	//관리대상지정
	@Column(name = "watchTargetYn")
	private boolean watchTargetYn;
	

	//작업형태 비고 //코로드인경우 입력값을 받음.
	
	private Long commonId;
	
	
	private Long companyInfoId;
	
	@Column(name = "orderNo")
	private int orderNo;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	@Column(name = "workDate")
	private Date workDate;

	
	
	private List<InboundReq> Inbounds = new ArrayList<>();
	
	
	private Boolean isUsing;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	@Column(name = "createDt")
	private Date createDt;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	@Column(name = "updateDt")
	private Date updateDt;
	
	
	private Long userId;
	
	
	
}
