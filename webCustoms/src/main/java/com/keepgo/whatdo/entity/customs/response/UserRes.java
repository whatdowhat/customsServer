package com.keepgo.whatdo.entity.customs.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CompanyInfo;
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
public class UserRes {
	private Long id;
	private String name;
	private String phoneNo;
	private String password;
	private String loginId;
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date updateDt;
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date regDt;
	
	private String updateId;
	

	private List<Long> ids = new ArrayList<>();
	
	
	List<UserRes> userReqData;
	
	
}
