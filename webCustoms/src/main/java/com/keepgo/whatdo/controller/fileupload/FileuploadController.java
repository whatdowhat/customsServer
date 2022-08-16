package com.keepgo.whatdo.controller.fileupload;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.google.gson.Gson;
import com.keepgo.whatdo.controller.CompanySpecification;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CompanyInfo;
import com.keepgo.whatdo.entity.customs.CompanyInfoExport;
import com.keepgo.whatdo.entity.customs.CompanyInfoManage;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.request.CommonReqForExcelDownload;
import com.keepgo.whatdo.entity.customs.request.CompanyInfoReq;
import com.keepgo.whatdo.entity.customs.request.FileUploadReq;
import com.keepgo.whatdo.entity.customs.response.CommonRes;
import com.keepgo.whatdo.entity.customs.response.CompanyInfoRes;
import com.keepgo.whatdo.entity.customs.response.FileUploadRes;
import com.keepgo.whatdo.mapper.UserMapper;
import com.keepgo.whatdo.repository.CompanyInfoExportRepository;
import com.keepgo.whatdo.repository.CompanyInfoManageRepository;
import com.keepgo.whatdo.repository.CompanyInfoRepository;
import com.keepgo.whatdo.repository.UserRepository;
import com.keepgo.whatdo.service.company.CompanyInfoService;
import com.keepgo.whatdo.service.fileupload.FileUploadService;
import com.keepgo.whatdo.util.CustomExcel;

@RestController
//@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true" ) // 컨트롤러에서 설정
public class FileuploadController {

	static final Logger log = LoggerFactory.getLogger(FileuploadController.class);

	@Autowired
	CompanyInfoRepository _companyInfoRepository;

	@Autowired
	CompanyInfoExportRepository _companyInfoExportRepository;
	
	@Autowired
	CompanyInfoManageRepository _companyInfoManageRepository;
	
	@Autowired
	UserMapper _userMapper;

	@Autowired
	UserRepository _userRepository;

	@Autowired
	CompanyInfoService _companyInfoService;
	
	@Autowired
	FileUploadService _fileUploadService;
	
	@RequestMapping(value = "/front/getFile2", method = {RequestMethod.POST })
	public List<?> getFile2(HttpServletRequest httpServletRequest, @RequestBody FileUploadReq fileUploadReq ){
		
		//masterid,common type
		return  _fileUploadService.getFileList2(fileUploadReq);
	}
	
	@RequestMapping(value = "/front/getFile3", method = {RequestMethod.POST })
	public List<?> getFile3(HttpServletRequest httpServletRequest, @RequestBody FileUploadReq fileUploadReq ){
		
		//masterid,common type
		return  _fileUploadService.getFileList3(fileUploadReq);
	}
	
	@RequestMapping(value = "/front/uploadFile2", method = {RequestMethod.POST })
	public boolean uploadFile2(MultipartFile file,@RequestParam String fileUploadReq, HttpServletRequest req)	throws Exception, NumberFormatException {
		Gson gson = new Gson();
		FileUploadReq frq = gson.fromJson(fileUploadReq, FileUploadReq.class);
		_fileUploadService.uploadFile2(file,frq);
		return true;
	}
	@RequestMapping(value = "/front/uploadFileContainer", method = {RequestMethod.POST })
	public boolean uploadFileContainer(MultipartFile file,@RequestParam String fileUploadReq, HttpServletRequest req)	throws Exception, NumberFormatException {
		Gson gson = new Gson();
		FileUploadReq frq = gson.fromJson(fileUploadReq, FileUploadReq.class);
		_fileUploadService.uploadFileContainer(file,frq);
		return true;
	}
	@RequestMapping(value = "/common/uploadFile3", method = {RequestMethod.POST })
	public boolean uploadFile3(MultipartFile file,@RequestParam String fileUploadReq, HttpServletRequest req)	throws Exception, NumberFormatException {
		Gson gson = new Gson();
		FileUploadReq frq = gson.fromJson(fileUploadReq, FileUploadReq.class);
		_fileUploadService.uploadFile3(file,frq);
		return true;
	}
	
	@RequestMapping(value = "/common/uploadFile4", method = {RequestMethod.POST })
	public boolean uploadFile4(MultipartFile file,@RequestParam String fileUploadReq, HttpServletRequest req)	throws Exception, NumberFormatException {
		Gson gson = new Gson();
		FileUploadReq frq = gson.fromJson(fileUploadReq, FileUploadReq.class);
		_fileUploadService.uploadFile4(file,frq);
		return true;
	}
	

	
}