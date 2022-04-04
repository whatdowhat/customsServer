package com.keepgo.whatdo.controller.FinalInbound;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.keepgo.whatdo.entity.customs.request.CompanyInfoReq;
import com.keepgo.whatdo.entity.customs.request.FinalInboundReq;
import com.keepgo.whatdo.entity.customs.request.InboundMasterReq;
import com.keepgo.whatdo.entity.customs.response.InboundMasterRes;
import com.keepgo.whatdo.mapper.UserMapper;
import com.keepgo.whatdo.repository.CompanyInfoExportRepository;
import com.keepgo.whatdo.repository.CompanyInfoManageRepository;
import com.keepgo.whatdo.repository.CompanyInfoRepository;
import com.keepgo.whatdo.repository.UserRepository;
import com.keepgo.whatdo.service.company.CompanyInfoService;
import com.keepgo.whatdo.service.fileupload.FileUploadService;
import com.keepgo.whatdo.service.finalInbound.FinalInboundService;
import com.keepgo.whatdo.service.inboundMst.InboundMstService;

@RestController
//@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true" ) // 컨트롤러에서 설정
public class FinalInboundController {

	static final Logger log = LoggerFactory.getLogger(FinalInboundController.class);

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
	
	@Autowired
	InboundMstService _inboundMstService;
	
	@Autowired
	FinalInboundService _finalInboundService;
	
	@RequestMapping(value = "/test/finalInboundCreate", method = {RequestMethod.POST })
	public boolean finalInboundCreate(HttpServletRequest httpServletRequest,@RequestBody FinalInboundReq finalInboundReq){
		
		return  _finalInboundService.createFinalInbound(finalInboundReq);
		
	}
	
	@RequestMapping(value = "/test/finalInboundUpdate", method = {RequestMethod.POST })
	public boolean finalInboundUpdate(HttpServletRequest httpServletRequest,@RequestBody FinalInboundReq finalInboundReq){
		
		return  _finalInboundService.updateFinalInbound(finalInboundReq);
		
	}
//	@RequestMapping(value = "/test/update/inboundMst", method = {RequestMethod.POST })
//	public boolean updateInboundMaster(HttpServletRequest httpServletRequest, @RequestBody InboundMasterReq inboundMasterReq){
//		_inboundMstService.updateInboundMaster(inboundMasterReq);
//		return  true;
//	}
//	@RequestMapping(value = "/test/get/allInboundMaster", method = {RequestMethod.POST })
//	public List<?> getInboundMaster(HttpServletRequest httpServletRequest){
//		List<?> l = _inboundMstService.getInboundMaster();
//		return  l;
//	}
//	@RequestMapping(value = "/test/get/inboundMaster", method = {RequestMethod.POST })
//	public InboundMasterRes getInboundMaster(HttpServletRequest httpServletRequest,@RequestBody InboundMasterReq inboundMasterReq){
//		 
//		return  _inboundMstService.getInboundMaster(inboundMasterReq.getId());
//	}
	
}