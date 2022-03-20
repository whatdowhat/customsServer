package com.keepgo.whatdo.controller.Common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.keepgo.whatdo.entity.customs.request.CommonMasterReq;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.service.common.CommonService;

@RestController
public class CommonController {

	static final Logger log = LoggerFactory.getLogger(CommonController.class);
	
	@Autowired
	CommonService _commonService;
	
	@RequestMapping(value = "/common/commonMaster", method = {RequestMethod.POST })
	public List<?> commonMaster(HttpServletRequest httpServletRequest,@RequestBody CommonMasterReq commonMasterReq) throws Exception {

		List<?> list = _commonService.getMasterAll(commonMasterReq);
		return  list;
	}
	
	@RequestMapping(value = "/common/common", method = {RequestMethod.POST })
	public List<?> common(HttpServletRequest httpServletRequest,@RequestBody CommonReq commonReq) throws Exception {

		List<?> list = _commonService.getCommonAll(commonReq);
		return  list;
	}
	@RequestMapping(value = "/common/commonByMaster", method = {RequestMethod.POST })
	public List<?> commonByMaster(HttpServletRequest httpServletRequest,@RequestBody CommonReq commonReq) throws Exception {

		List<?> list = _commonService.getCommonByMaster(commonReq);
		return  list;
	}
	
}
