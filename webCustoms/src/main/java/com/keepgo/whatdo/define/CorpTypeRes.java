package com.keepgo.whatdo.define;

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
public class CorpTypeRes {

	private int id;
	private String code;
	private String name;
	private String showName;
	private String fullName;
	private String address;
	private int order;
	
}
