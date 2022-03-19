package com.keepgo.whatdo.entity.customs;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "web_inbound")
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Inbound {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "workDate")
	private Date workDate;

	@Column(name = "company")
	private String company;
	
	@Column(name = "marking")
	private String marking;
	
	@Column(name = "korNm")
	private String korNm;
	
	@Column(name = "itemCount")
	private String itemCount;
	
	@Column(name = "boxCount")
	private String boxCount;
	
	@Column(name = "weight")
	private String weight;
	
	@Column(name = "cbm")
	private String cbm;
	
	@Column(name = "reportPrice")
	private String reportPrice;
	
	@Column(name = "memo1")
	private String memo1;
	
	@Column(name = "memo2")
	private String memo2;
	
	@Column(name = "memo3")
	private String memo3;
	
	@Column(name = "itemNo")
	private String itemNo;
	
	@Column(name = "taxNo")
	private String taxNo;
	
	@Column(name = "coYn")
	private String coYn;
	
	@Column(name = "totalPrice")
	private String totalPrice;
	
	@Column(name = "engNm")
	private String engNm;
	
	@Column(name = "exporter")
	private String exporter;
	
	@Column(name = "businessman")
	private String businessman;
	
	@Column(name = "blNo")
	private String blNo;
	
	
	
}
