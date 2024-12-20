package com.keepgo.whatdo.entity.customs.request;

import java.util.Date;
import java.util.List;

import com.keepgo.whatdo.entity.NodeMember;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.User;
import com.keepgo.whatdo.util.ExcelColumn;

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
	@ExcelColumn(headerName="CommonId",order = 1)
	private Long id;
	
	
	private Long commonMasterId;
	
	private Long companInfoyId;
	
	@ExcelColumn(headerName="코드이름",order = 2)
	private String name;
	@ExcelColumn(headerName="쉬퍼코드",order = 3)
	private String value3;
	@ExcelColumn(headerName="쉬퍼이름",order = 4)
	private String value;
	
	@ExcelColumn(headerName="쉬퍼주소",order = 5)
	private String value2;
	
	
	private Boolean isUsing;
	
	
	private Long user;
	private int preperOrder;
//	List<Common> commonData;
	List<CommonReq> commonReqData;
	
	private boolean isChecked;
	
	
	
}
