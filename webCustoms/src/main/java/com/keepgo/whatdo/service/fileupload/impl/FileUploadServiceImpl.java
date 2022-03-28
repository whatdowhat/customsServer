package com.keepgo.whatdo.service.fileupload.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.keepgo.whatdo.ApplicationConfig;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CommonMaster;
import com.keepgo.whatdo.entity.customs.FileUpload;
import com.keepgo.whatdo.entity.customs.Inbound;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.request.FileUploadReq;
import com.keepgo.whatdo.entity.customs.response.FileUploadRes;
import com.keepgo.whatdo.entity.customs.response.UserRes;
import com.keepgo.whatdo.repository.CommonRepository;
import com.keepgo.whatdo.repository.FileUploadRepository;
import com.keepgo.whatdo.repository.InboundRepository;
import com.keepgo.whatdo.service.fileupload.FileUploadService;
import com.keepgo.whatdo.service.util.UtilService;

import lombok.RequiredArgsConstructor;

@Component
public class FileUploadServiceImpl implements FileUploadService {

	
	@Autowired
	InboundRepository _inboundRepository;
	
	@Autowired
	FileUploadRepository _fileUploadRepository;

	@Autowired
	UtilService _UtilService;
	
	@Autowired
	CommonRepository _commonRepository;
	
	@Value("${fileupload.root}")
	String uploadRoot;
	
	@Override
	public List<?> getFileList() {


		List<?> list = _fileUploadRepository.findAll().stream()
				.sorted(Comparator.comparing(FileUpload::getUpdateDt).reversed())
				.map(item -> {

			FileUploadRes dto = FileUploadRes.builder()

					
					.id(item.getId()).path1(item.getPath1()).path2(item.getPath2()).path3(item.getPath3())
					.fileName1(item.getFileName1()).fileName2(item.getFileNam2()).fileSize(item.getFileSize())
					.root(item.getRoot()).uploadType(item.getUploadType()).uploadTypeNm(item.getUploadTypeNm())
//					.inboundId(item.getInbound().getId()).coNum(item.getInbound().getCompanyInfo().getCoNum())
											
					.build();
					
			return dto;
		}).collect(Collectors.toList());

		return list;
	}
	
	@Override
	public FileUploadRes uploadFile(MultipartFile file,FileUploadReq fileUploadReq) throws IOException {
		
		
		
		BufferedReader  bufferedReader  = new BufferedReader(new InputStreamReader(file.getInputStream()));
		String root = uploadRoot;
		
		
		StringBuilder strPath = new StringBuilder(uploadRoot);
		strPath.append(File.separatorChar+fileUploadReq.getPath1());
		strPath.append(File.separatorChar+fileUploadReq.getPath2());
		
		//stringBuilder.append(File.separatorChar+fileUploadReq.getPath3());
		
		//c:/Customs/212351251/A/filename.xml
		

//		String path = root+"a"; //폴더 경로
		File Folder = new File(strPath.toString());

		// 해당 디렉토리가 없을경우 디렉토리를 생성
		if (!Folder.exists()) {
			try{
			    Folder.mkdirs(); //폴더 생성
//			    System.out.println("폴더가 생성되었습니다.");
		        } 
		        catch(Exception e){
			    e.getStackTrace();
			}        
	         }else {
//			System.out.println("이미 폴더가 생성되어 있습니다.");
		}
		
		String fileName= file.getOriginalFilename();
		int filesize=(int)file.getSize();
		File saveFile = new File(strPath.toString(), file.getOriginalFilename());
		file.transferTo(saveFile);
		
		

		
		FileUpload fileupload = new FileUpload();
		
		
		
		fileupload.setRoot(uploadRoot);
		fileupload.setCreateDt(new Date());
		fileupload.setUpdateDt(new Date());
		fileupload.setFileSize(filesize);
		fileupload.setPath1(fileUploadReq.getPath1());
		fileupload.setPath2(fileUploadReq.getPath2());
		fileupload.setPath3(fileName);
		
		fileupload.setIsUsing(true);
		_fileUploadRepository.save(fileupload);
		
		FileUploadRes fileUploadRes = new FileUploadRes();
		return fileUploadRes;
		
	}
	
//	@Override
//	public FileUploadRes uploadFile(MultipartFile file, FileUploadReq fileUploadReq) throws IOException {
//		
//		
//		BufferedReader  bufferedReader  = new BufferedReader(new InputStreamReader(file.getInputStream()));
//		String root = "c:/Customs/";
//		String fileName= file.getName();
//		int filesize=(int)file.getSize();
//		File saveFile = new File(root, file.getOriginalFilename());
//		file.transferTo(saveFile);
//		Inbound inbound = _inboundRepository.findById(fileUploadReq.getInboundId())
//				.orElse(Inbound.builder().build());
//		Common common = _commonRepository.findByValue(fileUploadReq.getUploadType());
//		
//		
//		FileUpload fileupload = new FileUpload();
//		
//		
//		fileupload.setInbound(inbound);
//		fileupload.setPath1(inbound.getCompanyInfo().getCoNum());
//		fileupload.setPath2(fileUploadReq.getUploadTypeNm());
//		fileupload.setUploadType(fileUploadReq.getUploadType());
//		fileupload.setUploadTypeNm(common.getValue2());
//		fileupload.setRoot(root);
//		fileupload.setCreateDt(new Date());
//		fileupload.setUpdateDt(new Date());
//		fileupload.setFileSize(filesize);
//		fileupload.setPath3(fileName);
//		fileupload.setFileuploadType(common);
//		fileupload.setIsUsing(true);
//		_fileUploadRepository.save(fileupload);
//		
//		FileUploadRes fileUploadRes = new FileUploadRes();
//		return fileUploadRes;
//		
//	}

	
}