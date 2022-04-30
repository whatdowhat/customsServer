package com.keepgo.whatdo.controller.FinalInbound;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.keepgo.whatdo.entity.customs.request.CompanyInfoReq;
import com.keepgo.whatdo.entity.customs.request.FinalInboundReq;
import com.keepgo.whatdo.entity.customs.request.InboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.entity.customs.response.CompanyInfoRes;
import com.keepgo.whatdo.entity.customs.response.FinalInboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundMasterRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.mapper.UserMapper;
import com.keepgo.whatdo.repository.CompanyInfoExportRepository;
import com.keepgo.whatdo.repository.CompanyInfoManageRepository;
import com.keepgo.whatdo.repository.CompanyInfoRepository;
import com.keepgo.whatdo.repository.UserRepository;
import com.keepgo.whatdo.service.company.CompanyInfoService;
import com.keepgo.whatdo.service.fileupload.FileUploadService;
import com.keepgo.whatdo.service.finalInbound.FinalInboundService;
import com.keepgo.whatdo.service.inbound.InboundService;
import com.keepgo.whatdo.service.inboundMst.InboundMstService;
import com.keepgo.whatdo.service.util.UtilService;

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
	@Autowired
	InboundService _InboundService;
	@Autowired
	UtilService _utilService;
	
	@RequestMapping(value = "/test/finalInboundCreate", method = {RequestMethod.POST })
	public boolean finalInboundCreate(HttpServletRequest httpServletRequest,@RequestBody FinalInboundReq finalInboundReq){
		
		return  _finalInboundService.createFinalInbound(finalInboundReq);
		
	}
	
	@RequestMapping(value = "/test/finalInboundAdd", method = {RequestMethod.POST })
	public FinalInboundRes finalInboundAdd(HttpServletRequest httpServletRequest,@RequestBody FinalInboundReq finalInboundReq)throws ParseException{
		
		return  _finalInboundService.addFinalInbound(finalInboundReq);
		
	}
	@RequestMapping(value = "/test/deleteFinalInbound", method = {RequestMethod.POST })

	public  boolean deleteInbound(@RequestBody FinalInboundReq finalInboundReq) throws IOException, InterruptedException {
		
		
		return  _finalInboundService.deleteFinalInbound(finalInboundReq);

	}
	
	@RequestMapping(value = "/test/finalInboundUpdate", method = {RequestMethod.POST })
	public boolean finalInboundUpdate(HttpServletRequest httpServletRequest,@RequestBody FinalInboundReq finalInboundReq){
		
		return  _finalInboundService.updateFinalInbound(finalInboundReq);
		
	}
	@RequestMapping(value = "/test/finalInbound", method = {RequestMethod.POST })
	public FinalInboundRes finalInbound(HttpServletRequest httpServletRequest,@RequestBody FinalInboundReq finalInboundReq){
		
		return  _finalInboundService.getOne(finalInboundReq.getId());
		
	}
	
	@RequestMapping(value = "/test/deleteFinalInboundMasterItems", method = {RequestMethod.POST })
	public boolean deleteFinalInboundMasterItems(HttpServletRequest httpServletRequest,@RequestBody FinalInboundReq finalInboundReq){
		
		return  _finalInboundService.deleteFinalInboundMasterItems(finalInboundReq);
		
	}
	
	@RequestMapping(value = "/test/addFinalInboundMasterItems", method = {RequestMethod.POST })

	public  boolean addFinalInboundMasterItems(@RequestBody FinalInboundReq finalInboundReq) throws IOException, InterruptedException {

	
		
		return _finalInboundService.addFinalInboundMasterItems(finalInboundReq);

	}
	
	@RequestMapping(value = "/test/addDataFinalInboundMasterItems", method = {RequestMethod.POST })

	public  boolean addDataFinalInboundMasterItems(@RequestBody FinalInboundReq finalInboundReq) throws IOException, InterruptedException {

	
		
		return _finalInboundService.addDataFinalInboundMasterItems(finalInboundReq);

	}
	
	@RequestMapping(value = "/test/getAllCondition", method = {RequestMethod.POST })
	public List<?> finalInboundAll(HttpServletRequest httpServletRequest,@RequestBody FinalInboundReq finalInboundReq){
		
		return  _finalInboundService.getAllCondition(finalInboundReq);

		
	}
	
	@RequestMapping(value = "/test/FinalInboundExcelRead", method = { RequestMethod.POST })
	@ResponseBody
	public boolean excelRead(MultipartFile file, String test, HttpServletRequest req)
			throws Exception, NumberFormatException {

		String extension = FilenameUtils.getExtension(file.getOriginalFilename());

		if (!extension.equals("xlsx") && !extension.equals("xls")) {
			throw new IOException("엑셀파일만 업로드 해주세요.");
		}


		 

		//출력모드
//			List<InboundRes> result = _utilService.changeExcelFormatNew(list);
		return _finalInboundService.excelRead(file, null, test);
	}

	
}