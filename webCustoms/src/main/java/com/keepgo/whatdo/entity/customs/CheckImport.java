package com.keepgo.whatdo.entity.customs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.keepgo.whatdo.util.ExcelColumn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "web_check_import")
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@ToString
public class CheckImport {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JoinColumn(name = "inboundMasterId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private InboundMaster inboundMaster;

	@JoinColumn(name = "companyInfoId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private CompanyInfo companyInfo;
	
	@JoinColumn(name = "userId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private User user;
	
	@Column(name = "orderNo")
	private int orderNo;
	
	@Column(name = "no")
	private int no;
	
	@Column(name = "memo")
	private String memo;

	//등록일자
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
	@Column(name = "regDate")
	private Date regDate;
	

	
}