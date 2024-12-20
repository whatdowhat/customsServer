package com.keepgo.whatdo.define;

import java.util.Date;

import com.keepgo.whatdo.viewEntity.CommonMasterViewResponse;

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
public class BackgroundColorTypeRes {

	private int id;
	private String name;
	private String code;
	private String showName;
	private int r;
	private int g;
	private int b;
	private int sortOrder;
	
}
