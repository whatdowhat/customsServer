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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "base_info_web")
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BaseInfo {

	@Id
	@Column(name = "seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;
	
	@Column(name = "memberNm")
	private String memberNm;
	
	@Column(name = "memberPh")
	private String memberPh;
	
	@Column(name = "memberEmail")
	private String memberEmail;
	
	@Column(name = "memberTeam")
	private String memberTeam;
	
	@Column(name = "memberProject")
	private String memberProject;

	@Column(name = "memberLeaderYn")
	private String memberLeaderYn;
	
	@Column(name = "matchingNm")
	private String matchingNm;

	@Column(name = "attendYn")
	private String attendYn;
	
	@Column(name = "govPh")
	private String govPh;
	
	@Column(name = "govNm")
	private String govNm;
	
	@Column(name = "govPersonNm")
	private String govPersonNm;
	
	@Column(name = "govPersonPh")
	private String govPersonPh;
	
	@Column(name = "govPersonEmail")
	private String govPersonEmail;
	
	@Column(name = "linkUrl")
	private String linkUrl;

	@Column(name = "yyyyMMdd")
	private String yyyyMMdd;
	
	@Column(name = "startHhmm")
	private String startHhmm;
	
	@Column(name = "endHhmm")
	private String endHhmm;
	
	@Column(name = "regPh")
	private String regPh;
	
	@Column(name = "editPh")
	private String editPh;
	
	@Column(name = "regDt")
	private LocalDateTime regDt;
	
	@Column(name = "startDt")
	private LocalDateTime startDt;
	
	@Column(name = "endDt")
	private LocalDateTime endDt;
	
	@Column(name = "editDt")
	private LocalDateTime editDt;
	
	@Column(name = "confirm")
	private Boolean confirm;
	
	@Column(name = "stAttendYn")
	private String stAttendYn;

	@Column(name = "confirmFinal")
	private Boolean confirmFinal;
	
	
	@Transient
	private String lineNumber;
	
	@Transient
	private String startDtString;
	
	@Transient
	private String endDtString;	
	
	@Transient
	private String uploadAvailable;	
	
	@Transient
	private List<String> deleteSeq;	
	
	@Transient
	private String searchSt;	
	@Transient
	private String searchEd;	
	
	@Transient
	private String searchStEdYn;	
	
}
