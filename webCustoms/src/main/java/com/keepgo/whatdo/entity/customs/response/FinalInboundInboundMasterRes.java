package com.keepgo.whatdo.entity.customs.response;

import java.util.Date;

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
public class FinalInboundInboundMasterRes {

	private Long id;
	private Long finalInboundId;
	private Long inboundMasterId;
	private int preperOrder;
	
	
}

