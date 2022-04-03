package com.keepgo.whatdo.entity.customs.response;

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
public class FileUploadRes {
	private Long id;
	private String path1;
	private String path2;
	private String path3;
	private String fileName1;
	private String fileName2;
	private int fileSize;
	private String root;
	private Long inboundMasterId;
	private String coNum;
	private int fileCount;
	private int fileType;
	private String fileTypeNm;
	
	
	
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
	private Date updateDt;
	@JsonFormat(pattern="yyyy-MM-dd" ,timezone = "Asia/Seoul")
	private Date createDt;
	
	
	

	private List<Long> ids = new ArrayList<>();
	
	
	List<FileUploadRes> FileUploadReqData;
	
	
}
