package com.keepgo.whatdo.service.fileupload;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.keepgo.whatdo.entity.customs.request.FileUploadReq;
import com.keepgo.whatdo.entity.customs.request.UserReq;
import com.keepgo.whatdo.entity.customs.response.FileUploadRes;
import com.keepgo.whatdo.entity.customs.response.UserRes;

@Service
public interface FileUploadService {

	
	
	@Transactional
	List<?> getFileList();
	
	@Transactional
	FileUploadRes deleteFile(FileUploadReq fileUploadReq);

	
	@Transactional
	FileUploadRes uploadFile(MultipartFile file,FileUploadReq fileUploadReq) throws IOException; 
	
	@Transactional
//	void downloadFile(FileUploadReq fileUploadReq, HttpServletResponse response,HttpServletRequest request) throws Exception;
	ResponseEntity<Object> downloadFile(FileUploadReq fileUploadReq, HttpServletResponse response,HttpServletRequest request,String agent) throws Exception;
}