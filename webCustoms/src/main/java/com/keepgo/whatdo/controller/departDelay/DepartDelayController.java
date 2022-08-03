package com.keepgo.whatdo.controller.departDelay;

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
import com.keepgo.whatdo.controller.checkImport.CheckImportController;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.Inbound;
import com.keepgo.whatdo.entity.customs.request.CheckImportReq;
import com.keepgo.whatdo.entity.customs.request.ChinaSanggumReq;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.request.DepartDelayReq;
import com.keepgo.whatdo.entity.customs.request.FileUploadReq;
import com.keepgo.whatdo.entity.customs.request.FinalInboundReq;
import com.keepgo.whatdo.entity.customs.request.InboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.entity.customs.request.ManageReq;
import com.keepgo.whatdo.entity.customs.request.UnbiReq;
import com.keepgo.whatdo.entity.customs.request.UserReq;
import com.keepgo.whatdo.entity.customs.response.CheckImportRes;
import com.keepgo.whatdo.entity.customs.response.ChinaSanggumRes;
import com.keepgo.whatdo.entity.customs.response.CommonRes;
import com.keepgo.whatdo.entity.customs.response.DepartDelayRes;
import com.keepgo.whatdo.entity.customs.response.FileUploadRes;
import com.keepgo.whatdo.entity.customs.response.InboundMasterRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundViewListRes;
import com.keepgo.whatdo.entity.customs.response.InboundViewRes;
import com.keepgo.whatdo.entity.customs.response.ManageRes;
import com.keepgo.whatdo.entity.customs.response.UnbiRes;
import com.keepgo.whatdo.entity.customs.response.UserRes;
import com.keepgo.whatdo.service.chinaSanggum.ChinaSanggumService;
import com.keepgo.whatdo.service.ckImport.CheckImportService;
import com.keepgo.whatdo.service.departdelay.DepartDelayService;
import com.keepgo.whatdo.service.fileupload.FileUploadService;
import com.keepgo.whatdo.service.inbound.InboundService;
import com.keepgo.whatdo.service.manage.ManageService;
import com.keepgo.whatdo.service.unbi.UnbiService;
import com.keepgo.whatdo.service.util.UtilService;

@RestController
public class DepartDelayController{
	
	static final Logger log = LoggerFactory.getLogger(CheckImportController.class);
	@Autowired
	ChinaSanggumService _chinaSanggumService;
	@Autowired
	DepartDelayService _departDelayService;
	
	@RequestMapping(value = "/front/getDepartDelay", method = {RequestMethod.POST })
	public List<DepartDelayRes> getDepartDelay(HttpServletRequest httpServletRequest,@RequestBody DepartDelayReq departDelayReq) throws Exception{

		List<DepartDelayRes> list = _departDelayService.getDepartDelay(departDelayReq);		
		
		return  list;
	}
	
	@RequestMapping(value = "/front/commitDepartDelay", method = { RequestMethod.POST })
	public boolean commitDepartDelay(HttpServletRequest httpServletRequest,
			@RequestBody DepartDelayReq departDelayReq) {

		return _departDelayService.commitDepartDelay(departDelayReq);

	}
	

}