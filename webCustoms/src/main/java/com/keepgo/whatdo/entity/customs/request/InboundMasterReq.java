package com.keepgo.whatdo.entity.customs.request;

import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;

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
public class InboundMasterReq {

//	@Builder.Default
	private Long id; 
	
	@Column(name = "blNo")
	private String blNo;
	
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
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
	
	private Long companyInfoId;
	private Long exportId;
	
	@Column(name = "orderNo")
	private int orderNo;
	
//	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
//	@Column(name = "workDate")
//	private Date workDate;

	
	
	private String workDate;
	
	
	
	
	private Boolean isUsing;
	
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
	@Column(name = "createDt")
	private Date createDt;
	
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
	@Column(name = "updateDt")
	private Date updateDt;
	
	
	private Long userId;
	
	
	
}
