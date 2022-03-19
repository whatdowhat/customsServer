package com.keepgo.whatdo.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.keepgo.whatdo.util.ExcelColumn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "member_web")
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Member {

	@Id
	@Column(name = "seq")
	@ExcelColumn(headerName="seq",order = 1)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@ExcelColumn(headerName="memberId",order = 3)
	@Column(name = "memberId")
	private String memberId;
	
	@ExcelColumn(headerName="memberNm",order = 2)
	@Column(name = "memberNm")
	private String memberNm;
	
	@ExcelColumn(headerName="memberPh",order = 4)
	@Column(name = "memberPh")
	private String memberPh;
	
	@ExcelColumn(headerName="한글",order = 5)
	@Column(name = "memberEmail")
	private String memberEmail;
	
	@Column(name = "memberPassword")
	private String memberPassword;
	
	@Column(name = "memberFirstLogin")
	private boolean memberFirstLogin;

	@Column(name = "regMember")
	private String regMember;
	
	@Column(name = "memberAuth")
	private String memberAuth;
	
	@Column(name = "regDt")
	@ExcelColumn(headerName="등록일",order = 2)
	private LocalDateTime regDt;
	
	@Column(name = "editDt")
	private LocalDateTime editDt;
	
	@Transient
	private String password1;
	@Transient
	private String password2;
	
	@Transient
	private List<String> resetSeq;	

	@Transient
	private String memberProject;
	@Transient
	private String memberTeam;
	
	
	
}
