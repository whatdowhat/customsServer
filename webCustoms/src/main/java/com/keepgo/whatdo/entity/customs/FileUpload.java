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
import javax.persistence.OneToOne;
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
@Table(name = "web_fileupload")
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileUpload {

	@Id
	@Column(name = "id")
	@ExcelColumn(headerName="fileupload",order = 1)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "path1")
	private String path1;

	@Column(name = "path2")
	private String path2;
	
	@Column(name = "path3")
	private String path3;

	@Column(name = "fileName1")
	private String fileName1;
	
	@Column(name = "fileName2")
	private String fileNam2;

	@Column(name = "fileSize")
	private int fileSize;
	
	
	@JoinColumn(name = "inboundId")
	@ExcelColumn(headerName="inboundId",order = 2)
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private Inbound inbound;
	
	@JoinColumn(name = "fileuploadTypeId")
	@ManyToOne(fetch = FetchType.LAZY)
	private Common fileuploadType;
	
	//공통 컬럼.
	@ExcelColumn(headerName="사용여부",order = 4)
	@Column(name = "isUsing",nullable = false,columnDefinition="tinyint(1) default 1")
	private Boolean isUsing;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	@Column(name = "createDt")
	private Date createDt;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	@Column(name = "updateDt")
	private Date updateDt;
	
	@ExcelColumn(headerName="사용자",order = 5)
	@OneToOne
	@JoinColumn(name = "id")
	private User user;
//	//공통 컬럼.
}
