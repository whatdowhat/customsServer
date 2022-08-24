package com.keepgo.whatdo.entity.customs.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.keepgo.whatdo.entity.customs.CompanyInfoExport;
import com.keepgo.whatdo.entity.customs.CompanyInfoManage;
import com.keepgo.whatdo.entity.customs.User;

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
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class FinalInboundViewRes {

	private Long id;

	
	private String itemCountSumFinal;
	private String boxCountSumFinal;
	private String cbmSumFinal;
	private String weightSumFinal;
	
	private List<InboundViewRes> inbounds = new ArrayList();
	
	
	private String masterBlNo;
	private String departDtStr;
	private String cargoName;
	private String incomePort;
	private Long incomePortId;
	private String departPort;
	private Long departPortId;
	private String hangName;
	private String hangCha;
	private String containerNo;
	private String silNo;
	
}
