package com.keepgo.whatdo.viewEntity;

import java.util.Date;

import com.keepgo.whatdo.entity.NodeMember;

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
public class ViewCommon {

	private Long id;

	private Long masterId;
	
	private String code;
	private String codeNm;
	private Boolean isUsing;
	private Date createDt;
	private Date updateDt;
	private Long user;
	private NodeMember nodeMember;
	
}
