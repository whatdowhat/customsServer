package com.keepgo.whatdo.entity.customs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "web_common_master")
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class CommonMaster {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	
	@Column(name = "Nm",nullable = false)
	private String Nm;
	
	@Column(name = "isUsing",nullable = false,columnDefinition="tinyint(1) default 1")
	private Boolean isUsing;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Column(name = "createDt")
	private Date createDt;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Column(name = "updateDt")
	private Date updateDt;
	
	@Column(name = "user")
	private Long user;
	
//	@OneToMany(fetch = FetchType.EAGER)
	@OneToMany
	@JoinColumn(name = "commonMasterId")
	@JsonManagedReference  // 추가
	private List<Common> commons = new ArrayList<>();

	//검색 컬럼
	@Transient
	private String startDate;
	//검색 컬럼
	@Transient
	private String endDate;
	
	

	
}
