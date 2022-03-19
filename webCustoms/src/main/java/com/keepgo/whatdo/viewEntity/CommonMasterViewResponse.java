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
public class CommonMasterViewResponse {

	private Long id;
	private String name;
	private Boolean isUsing;
	private Long user;
	private Date createDt;
	private Date updateDt;
	
	
}

