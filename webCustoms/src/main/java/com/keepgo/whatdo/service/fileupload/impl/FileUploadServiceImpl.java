package com.keepgo.whatdo.service.fileupload.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.FileUpload;
import com.keepgo.whatdo.entity.customs.InboundMaster;
import com.keepgo.whatdo.entity.customs.request.FileUploadReq;
import com.keepgo.whatdo.entity.customs.response.FileUploadRes;
import com.keepgo.whatdo.repository.CommonRepository;
import com.keepgo.whatdo.repository.FileUploadRepository;
import com.keepgo.whatdo.repository.InboundMasterRepository;
import com.keepgo.whatdo.repository.InboundRepository;
import com.keepgo.whatdo.service.fileupload.FileUploadService;
import com.keepgo.whatdo.service.util.UtilService;

@Component
public class FileUploadServiceImpl implements FileUploadService {

	
	@Autowired
	InboundRepository _inboundRepository;
	
	@Autowired
	InboundMasterRepository _inboundMasterRepository;
	
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
					.inboundMasterId(item.getInboundMaster().getId()).coNum(item.getInboundMaster().getCompanyInfo().getCoNum())
											
					.build();
					
			return dto;
		}).collect(Collectors.toList());

		return list;
	}
	
	@Override
	public FileUploadRes uploadFile(MultipartFile file,FileUploadReq fileUploadReq) throws IOException {
		
		
		InboundMaster inboundMaster = _inboundMasterRepository.findById(Long.valueOf(fileUploadReq.getPath1()))
				.orElse(InboundMaster.builder().build());
		Common common = _commonRepository.findByValue(fileUploadReq.getPath2());
		BufferedReader  bufferedReader  = new BufferedReader(new InputStreamReader(file.getInputStream()));
		String root = uploadRoot;
		
		
		StringBuilder strPath = new StringBuilder(uploadRoot);
		strPath.append(File.separatorChar+inboundMaster.getCompanyInfo().getCoNum());
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
		
		
		fileupload.setInboundMaster(inboundMaster);
		fileupload.setFileName1(fileName);
		fileupload.setFileuploadType(common);
		fileupload.setRoot(uploadRoot);
		fileupload.setCreateDt(new Date());
		fileupload.setUpdateDt(new Date());
		fileupload.setFileSize(filesize);
		fileupload.setPath1(inboundMaster.getCompanyInfo().getCoNum());
		fileupload.setPath2(fileUploadReq.getPath2());
		fileupload.setPath3(fileName);
		fileupload.setUploadType(fileUploadReq.getPath2());
		fileupload.setUploadTypeNm(common.getValue2());
		fileupload.setRoot(strPath.toString());
		
		fileupload.setIsUsing(true);
		_fileUploadRepository.save(fileupload);
		
		FileUploadRes fileUploadRes = new FileUploadRes();
		return fileUploadRes;
		
	}
	
	@Override
	public FileUploadRes deleteFile(FileUploadReq fileUploadReq) {
		
		StringBuilder strPath = new StringBuilder(uploadRoot);
		strPath.append(File.separatorChar+fileUploadReq.getPath1());
		strPath.append(File.separatorChar+fileUploadReq.getPath2());
		File FileList = new File(strPath.toString());

		//해당 폴더의 전체 파일리스트 조회
		String fileList[] = FileList.list();

		//전체파일
		for(int i=0; i<fileList.length; i++){
		  //파일명 조회
		  String FileName = fileList[i];

		 
		  if(FileName.equals(fileUploadReq.getFileName1())){
		    //존재하면 파일삭제
		    File deleteFile = new File(strPath.toString()+File.separatorChar+FileName);
		    deleteFile.delete();
		  }
		}
		
		
		
		FileUpload fileupload =  _fileUploadRepository.findById(fileUploadReq.getId())
				.orElse(FileUpload.builder().build());
				
		_fileUploadRepository.delete(fileupload);
		
		
		
			FileUploadRes fileUploadRes = new FileUploadRes();
			return fileUploadRes;
	}

	@Override
	public ResponseEntity<Object> downloadFile(FileUploadReq fileUploadReq, HttpServletResponse response,
			HttpServletRequest request,String agent) throws Exception {
		 String dFile = fileUploadReq.getFileName1(); //이름 받아오면 됨.
		  StringBuilder strPath = new StringBuilder(uploadRoot);
			strPath.append(File.separatorChar+fileUploadReq.getPath1());
			strPath.append(File.separatorChar+fileUploadReq.getPath2()); //고정 경로인경우 직접 입력, 아닐경우 DB에서 경로 받아오기
		  String path = strPath.toString()+File.separator+dFile;
		  
		  try {
				Path filePath = Paths.get(path);
				Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기
				
				File file = new File(path);
				
				String fileNm = file.getName();

	            HttpHeaders header = new HttpHeaders();
	            
	            //한글 인코딩 -> 클라이언트에서 디코딩으로 가져와서써야함
	            fileNm = URLEncoder.encode(fileNm, java.nio.charset.StandardCharsets.UTF_8.toString()).replace("+", "%20")
	            		.replace("*", "%2A")
	            		.replace("%7e","~");
	            header.add("custom-header",fileNm); //client에서 file이름을 받기위해서 custom-header로 넣어줘야함.		
		        response.setContentType("application/download;charset=utf-8");
	        
				
				return new ResponseEntity<Object>(resource, header, HttpStatus.OK);
			} catch(Exception e) {
				return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
			}
		
	}


	
}