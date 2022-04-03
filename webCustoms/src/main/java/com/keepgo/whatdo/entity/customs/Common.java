package com.keepgo.whatdo.entity.customs;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "web_common")
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Common {

	@Id
	@Column(name = "id")
	@ExcelColumn(headerName="CommonId",order = 1)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "commonMasterId")
	@ExcelColumn(headerName="commonMasterId",order = 2)
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private CommonMaster commonMaster;
	
	
	@ExcelColumn(headerName="코드이름",order = 3)
	@Column(name = "nm",nullable = false)
	private String nm;
	
	@ExcelColumn(headerName="코드값",order = 4)
	@Column(name = "value",nullable = false)
	private String value;
	
	
	@Column(name = "value2")
	private String value2;
	
	@ExcelColumn(headerName="사용여부",order = 5)
	@Column(name = "isUsing",nullable = false,columnDefinition="tinyint(1) default 1")
	private Boolean isUsing;
	
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
	@Column(name = "createDt")
	private Date createDt;
	
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
	@Column(name = "updateDt")
	private Date updateDt;
	
	@ExcelColumn(headerName="사용자",order = 6)
	@Column(name = "user")
	private Long user;
	
	@Transient
	private Long companInfoyId;
	
	@Transient
	private Long companyInfoExportId;
	
	@Transient
	private int preperOrder;
	
	@Transient
	private Boolean isChecked;
}
