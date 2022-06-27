package com.keepgo.whatdo.entity.customs.request;

import java.util.ArrayList;
import java.util.List;

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
public class FinalInboundInboundMasterReq {

	private Long id;
	private Long finalInboundId;
	private Long inboundMasterId;
	private List<Long> ids = new ArrayList<>();
	private int preperOrder;
	private String incomeDt;
	private String manager;
}
