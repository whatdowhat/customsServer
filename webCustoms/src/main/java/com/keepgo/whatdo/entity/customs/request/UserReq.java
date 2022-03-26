package com.keepgo.whatdo.entity.customs.request;

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
public class UserReq {
	@ExcelColumn(headerName="id",order = 1)
	private Long id;
	@ExcelColumn(headerName="이름",order = 2)
	private String name;
	@ExcelColumn(headerName="전화번호",order = 3)
	private String phoneNo;
	@ExcelColumn(headerName="비밀번호",order = 4)
	private String password;
	@ExcelColumn(headerName="로그인아이디",order = 5)
	private String loginId;
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date updateDt;
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date regDt;
	
	private String updateId;
	

	private List<Long> ids = new ArrayList<>();
	
	
	List<UserReq> userReqData;
	
	
}
