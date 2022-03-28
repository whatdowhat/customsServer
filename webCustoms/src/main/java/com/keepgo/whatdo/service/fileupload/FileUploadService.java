package com.keepgo.whatdo.service.fileupload;

import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.keepgo.whatdo.entity.customs.request.FileUploadReq;
import com.keepgo.whatdo.entity.customs.response.FileUploadRes;

@Service
public interface FileUploadService {

	
	
	@Transactional
	List<?> getFileList();
	
//	@Transactional
//	List<?> getFileList(FileUploadReq fileUploadReq); 
	
	@Transactional
	FileUploadRes uploadFile(MultipartFile file,FileUploadReq fileUploadReq) throws IOException; 
	
//	@Transactional
//	FileUploadRes uploadFile(MultipartFile file, FileUploadReq fileUploadReq) throws IOException; 
}
