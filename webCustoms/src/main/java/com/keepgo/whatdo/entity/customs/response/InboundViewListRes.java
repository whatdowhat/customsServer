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
public class InboundViewListRes {

	
		
	private String itemCountSumFinal;
	private Double itemCountSumFinalD;
	private String boxCountSumFinal;
	private Double boxCountSumFinalD;
	private String cbmSumFinal;
	private Double cbmSumFinalD;
	private String weightSumFinal;
	private Double weightSumFinalD;
	private String packingType;
	
	
	private List<InboundViewRes> inbounds = new ArrayList();
	
	private List<InboundRes> inboundsForPreview = new ArrayList();
	
}
