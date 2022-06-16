package com.keepgo.whatdo.controller.mkExcel;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.service.inbound.InboundService;
import com.keepgo.whatdo.service.util.UtilService;

@RestController
public class MkExcelController {

	static final Logger log = LoggerFactory.getLogger(MkExcelController.class);


	@Autowired
	UtilService _utilService;
	
	@Autowired
	InboundService _InboundService;

	@RequestMapping(value = "/front/mkexcel/inboundByInboundMasterId", method = {RequestMethod.POST })
	public List<InboundRes> inboundByInboundMasterId(HttpServletRequest httpServletRequest,@RequestBody InboundReq inboundReq) throws Exception{

		List<InboundRes> list = _InboundService.getInboundByInboundMasterId(inboundReq);
		//출력모드
		List<InboundRes> result = _utilService.changeExcelFormatNew(list);
		return  result;
	}

}
