package com.keepgo.whatdo.controller.Unbi;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.Inbound;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.request.FileUploadReq;
import com.keepgo.whatdo.entity.customs.request.InboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.entity.customs.request.UnbiReq;
import com.keepgo.whatdo.entity.customs.request.UserReq;
import com.keepgo.whatdo.entity.customs.response.CommonRes;
import com.keepgo.whatdo.entity.customs.response.FileUploadRes;
import com.keepgo.whatdo.entity.customs.response.InboundMasterRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundViewListRes;
import com.keepgo.whatdo.entity.customs.response.InboundViewRes;
import com.keepgo.whatdo.entity.customs.response.UnbiRes;
import com.keepgo.whatdo.entity.customs.response.UserRes;
import com.keepgo.whatdo.service.fileupload.FileUploadService;
import com.keepgo.whatdo.service.inbound.InboundService;
import com.keepgo.whatdo.service.unbi.UnbiService;
import com.keepgo.whatdo.service.util.UtilService;

@RestController
public class UnbiController {

	
	static final Logger log = LoggerFactory.getLogger(UnbiController.class);
	
	@Autowired
	InboundService _InboundService;
	
	@Autowired
	FileUploadService _fileUploadService;
	
	@Autowired
	UtilService _utilService;
	
	@Autowired
	UnbiService _unbiservice;

	
	@RequestMapping(value = "/front/unbiByFinalInboundId", method = {RequestMethod.POST })
	public List<UnbiRes> unbiByFinalInboundId(HttpServletRequest httpServletRequest,@RequestBody UnbiReq unbiReq) throws Exception{

		List<UnbiRes> list = _unbiservice.getUnbiByMasterId(unbiReq);		
		
		return  list;
	}
	
	@RequestMapping(value = "/front/unbiDataCommit", method = {RequestMethod.POST })

	public  boolean unbiDataCommit(@RequestBody UnbiReq unbiReq) throws IOException, InterruptedException {
		
		
		return  _unbiservice.commitUnbiData(unbiReq);

	}
	
	
	
	
	
}
