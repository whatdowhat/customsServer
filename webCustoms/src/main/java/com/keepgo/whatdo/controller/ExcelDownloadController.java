package com.keepgo.whatdo.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.google.gson.Gson;
import com.keepgo.whatdo.repository.MemberRepository;
import com.keepgo.whatdo.util.CustomExcel;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true", exposedHeaders = "custom-header" ) // 컨트롤러에서 설정
public class ExcelDownloadController {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ExcelDownloadController.class);
	
	@Autowired
	MemberRepository _member;
	
	
	//excel download controller 추가.
	@RequestMapping(value = "/user/wait/complete/orderdetail/excel.do", method = { RequestMethod.POST ,RequestMethod.GET})
	public View userwaitcompleteorderdetailexcel(ModelAndView modelAndView, HttpServletRequest req, HttpServletResponse res,
			HttpSession session,
			Model model) throws  UnsupportedEncodingException {
		
		
		Gson gson = new Gson();
		//화면에서 받은 파라미터
//		Map<String,Object> condition =  gson.fromJson(fromJSP, Map.class);
		List<?> list = _member.findAll();
		
		LOGGER.error("<<ExcelDownloadController");
		LOGGER.error("{0}",list);
		LOGGER.error("<<ExcelDownloadController");
		String filename =  "exceldownload_test";
        model.addAttribute("targetList", list);
        model.addAttribute("sheetName", "first");
        model.addAttribute("workBookName", filename);
        return new CustomExcel();
	}

}
