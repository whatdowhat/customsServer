package com.keepgo.whatdo.entity.customs.request;

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
public class CommonReq {

	private Long id;
	private Long commonMasterId;
	private String name;
	private Boolean isUsing;
	private Long user;
	
	
}
