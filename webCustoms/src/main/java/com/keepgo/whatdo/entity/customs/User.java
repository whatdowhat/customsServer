package com.keepgo.whatdo.entity.customs;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.keepgo.whatdo.util.ExcelColumn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "web_user")
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

	@Id
	@Column(name = "id")
	@ExcelColumn(headerName="id",order = 1)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ExcelColumn(headerName="이름",order = 2)
	@Column(name = "name")
	private String name;
	
	@ExcelColumn(headerName="전화번호",order = 3)
	@Column(name = "phoneNo")
	private String phoneNo;
	
	@ExcelColumn(headerName="비밀번호",order = 4)
	@Column(name = "password")
	private String password;
	
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
	@Column(name = "regDt")
	private Date regDt;
	
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
	@Column(name = "updateDt")
	private Date updateDt;
	
	@Column(name = "updateId")
	private String updateId;
	
	@ExcelColumn(headerName="로그인아이디",order = 5)
	@Column(name = "loginId")
	private String loginId;

	
	
}
