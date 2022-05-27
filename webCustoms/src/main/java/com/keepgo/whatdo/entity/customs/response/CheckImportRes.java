package com.keepgo.whatdo.entity.customs.response;

import java.util.Date;

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
public class CheckImportRes {

	
	private Long id;
	private Long inboundMasterId;
	private Long companyInfoId;
	private int orderNo;
	private int no;
	private String companyNm;
	private String blNo;
	private String memo;
	private String regDtStr;
	private String userNm;
	private String incomeDtStr;
	
	
	
	
}
