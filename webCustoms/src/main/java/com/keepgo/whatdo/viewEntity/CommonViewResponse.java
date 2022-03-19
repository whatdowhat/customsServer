package com.keepgo.whatdo.viewEntity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class CommonViewResponse {

	private Long id;
	private Long commonMasterId;
	private String nm;
	private Boolean isUsing;
	private Long user;
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date createDt;
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date updateDt;
	
	
}

