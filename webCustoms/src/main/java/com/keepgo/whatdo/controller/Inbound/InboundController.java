package com.keepgo.whatdo.controller.Inbound;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.request.FileUploadReq;
import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.entity.customs.request.UserReq;
import com.keepgo.whatdo.entity.customs.response.CommonRes;
import com.keepgo.whatdo.entity.customs.response.FileUploadRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.service.fileupload.FileUploadService;
import com.keepgo.whatdo.service.inbound.InboundService;

@RestController
public class InboundController {

	
	static final Logger log = LoggerFactory.getLogger(InboundController.class);
	
	@Autowired
	InboundService _InboundService;
	
	@Autowired
	FileUploadService _fileUploadService;

	@RequestMapping(value = "/test/inbound", method = { RequestMethod.POST })
	public List<InboundRes> shopper(HttpServletRequest httpServletRequest,InboundReq inboundReq){
	
//		return null;
		return _InboundService.getList(inboundReq);
	}
	
	@RequestMapping(value = "/test/getFile", method = {RequestMethod.POST })
	public List<?> getuser(HttpServletRequest httpServletRequest ){
		

		return  _fileUploadService.getFileList();
	}
	
	@RequestMapping(value = "/test/uploadFile", method = { RequestMethod.POST })
	@ResponseBody
//	public FileUploadRes excelUpload(MultipartFile file, @RequestParam String param1,@RequestParam String param2 , HttpServletRequest req)
	public FileUploadRes excelUpload(MultipartFile file,@RequestParam String fileUploadReq, HttpServletRequest req)	throws Exception, NumberFormatException {
		
		Gson gson = new Gson();
		FileUploadReq frq = gson.fromJson(fileUploadReq, FileUploadReq.class);
		
		System.out.println("here!");
		System.out.println(file);
		
		FileUploadRes result = _fileUploadService.uploadFile(file,frq);
		return result;
		
	}
	
}
