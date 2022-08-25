package com.keepgo.whatdo.entity.customs.response;

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
public class ManageRes {

	
	private Long id;
	private Long finalInboundId;
	private int orderNo;
	private int no;
	private String corpType;
	private String incomeDtStr;
	private String memo;
	private String regDtStr;
	private String masterBlNo;
	private String containerNo;
	private String gubun;
	
	private List<Long> ids = new ArrayList<>();
	private String loginId;
	private String userNm;
	
}
