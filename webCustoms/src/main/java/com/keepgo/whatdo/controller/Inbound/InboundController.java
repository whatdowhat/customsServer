package com.keepgo.whatdo.controller.Inbound;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.service.inbound.InboundService;

@RestController
public class InboundController {

	
	static final Logger log = LoggerFactory.getLogger(InboundController.class);
	
	@Autowired
	InboundService _InboundService;

	@RequestMapping(value = "/test/inbound", method = { RequestMethod.POST })
	public List<InboundRes> shopper(HttpServletRequest httpServletRequest,InboundReq inboundReq){
	
//		return null;
		return _InboundService.getList(inboundReq);
	}
	
	
}
