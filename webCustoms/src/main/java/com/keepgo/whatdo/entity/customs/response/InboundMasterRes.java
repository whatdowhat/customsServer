package com.keepgo.whatdo.entity.customs.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.Inbound;

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
public class InboundMasterRes {


	
private Long id;

@Column(name = "blNo")
private String blNo;

private String freight;
private int freightCode;
//담당자
private String manager;

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

//수출자 이름
private String export;
//수출자 이름
private String exportName;
//수출자 주소
private String exportAddress;

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

private Long exportId;

@Column(name = "orderNo")
private int orderNo;

@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
@Column(name = "workDate")
private Date workDate;



private Long inboundId;

//companyinfo
private String coNmEn;
private String consignee;
private String conInvoice;
private String coNum;


private Boolean isUsing;

@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
@Column(name = "createDt")
private Date createDt;

@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
@Column(name = "updateDt")
private Date updateDt;


private Long userId;

private Map<String,Object> files;

private Long aTypeCount;
private Long bTypeCount;
private Long cTypeCount;
private Long dTypeCount;
private Long eTypeCount;

private String aTypeNm;
private String bTypeNm;
private String cTypeNm;
private String dTypeNm;
private String eTypeNm;
//총 수량	
private Double totalItemCount;
//총 박스수
private Double totalBoxCount;
//총 무게
private Double totalWeight;
//총 CBM
private Double totalCbm;
//최종 총액
private Double finalTotalPrice;
//미리보기용 작업일자
private String forViewWorkDate;

private String currencyType;
private String packingType;
}
