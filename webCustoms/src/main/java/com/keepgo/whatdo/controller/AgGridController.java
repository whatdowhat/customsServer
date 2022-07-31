package com.keepgo.whatdo.controller;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.keepgo.whatdo.entity.BaseInfo;
import com.keepgo.whatdo.entity.ReactVO;
import com.keepgo.whatdo.repository.NoticeRepository;

@RestController
@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true" ) // 컨트롤러에서 설정
public class AgGridController {
	
	static final Logger log = LoggerFactory.getLogger(AgGridController.class);
	
	@Autowired
	NoticeRepository _notice;
    
    
    

    @RequestMapping(value = "/getData",method = { RequestMethod.POST})
	@ResponseBody
    public BaseInfo formexample(@RequestBody BaseInfo data,HttpServletRequest req) throws Exception, NumberFormatException {
    	
//    	System.out.println(">>>>>>>>>>>>"+data);

		return data;
	}
    
}
