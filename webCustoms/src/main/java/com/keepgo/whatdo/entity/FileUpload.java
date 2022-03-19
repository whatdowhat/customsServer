package com.keepgo.whatdo.entity;

import java.time.LocalDateTime;

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
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fileupload")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileUpload {

	@Id
	@Column(name = "Seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Seq;
	
	@Column(name = "fileSaveName")
	private String fileSaveName;
	
	@Column(name = "fileSize")
	private Long fileSize;
	
	@Column(name = "fileOriginalName")
	private String fileOriginalName;
	
	@Column(name = "uploadUser")
	private String uploadUser;
	
	@Column(name = "publicType")
	private String publicType;
	
	@Column(name = "regDt")
	private LocalDateTime Regdt;
	
	@Column(name="fileData")
	private byte[] fileData;
	
	@Transient
	boolean uploadYn;
	@Transient
	String errorMessage;
	
}
