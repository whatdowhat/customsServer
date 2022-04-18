package com.keepgo.whatdo.controller.excel;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.keepgo.whatdo.define.DocumentType;
import com.keepgo.whatdo.entity.customs.request.FinalInboundInboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.InboundMasterReq;
import com.keepgo.whatdo.entity.customs.response.ExcelFTARes;
import com.keepgo.whatdo.entity.customs.response.ExcelInpackRes;
import com.keepgo.whatdo.entity.customs.response.ExcelInpackSubRes;
import com.keepgo.whatdo.entity.customs.response.ExcelRCEPRes;
import com.keepgo.whatdo.entity.customs.response.ExcelYATAIRes;
import com.keepgo.whatdo.repository.CommonMasterRepository;
import com.keepgo.whatdo.service.common.CommonService;
import com.keepgo.whatdo.service.excel.ExcelService;

@RestController
public class ExcelController {

	static final Logger log = LoggerFactory.getLogger(ExcelController.class);

	@Autowired
	CommonService _commonService;

	@Autowired
	CommonMasterRepository _commonMasterRepository;

	@Autowired
	ExcelService _excelService;

	@Autowired
	ResourceLoader resourceLoader;

	@RequestMapping(value = "/excel/document/ftaData", method = { RequestMethod.POST })
	public ExcelFTARes ExcelFTARes(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {

//		String path = DocumentType.getList().stream().filter(t->t.getId() == 1).findFirst().get().getName();
////		Path filePath = Paths.get(path);
//		
//		Resource resource = resourceLoader.getResource(path); 
////				new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기
//
//		System.out.println(resource.exists());
//		System.out.println(resource.getFilename());
//		
//		File file = new File(resource.getURI());
////		
//		System.out.println(file.isFile());
//		System.out.println(path);

//		return null;
		return _excelService.ftaData(req);
	}

	@RequestMapping(value = "/excel/document/fta", method = { RequestMethod.POST })
	public void commonMaster(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {

		ExcelFTARes s = _excelService.ftaData(req);
		_excelService.fta(s, response);
	}
	
	@RequestMapping(value = "/excel/document/rcepData", method = { RequestMethod.POST })
	public ExcelRCEPRes excelRCEPRes(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {


		return _excelService.rcepData(req);
	}
	
	@RequestMapping(value = "/excel/document/rcep", method = { RequestMethod.POST })
	public void rcep(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {

		ExcelRCEPRes s =_excelService.rcepData(req);
		_excelService.rcep(s, response);
	}
	
	@RequestMapping(value = "/excel/document/yataiData", method = { RequestMethod.POST })
	public ExcelYATAIRes excelYATAIRes(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {


		return _excelService.yataiData(req);
	}
	
	@RequestMapping(value = "/excel/document/yatai", method = { RequestMethod.POST })
	public void yatai(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {

		ExcelYATAIRes s =_excelService.yataiData(req);
		_excelService.yatai(s, response);
	}
	
	@RequestMapping(value = "/excel/document/inpack", method = { RequestMethod.POST })
	public void inpack(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {

//		ExcelYATAIRes s =_excelService.yataiData(req);
		ExcelInpackRes s = new ExcelInpackRes();
		s.setFileNm("test");
		s.setData01("d1");
		s.setData03("d3");
		s.setData04("d4");
		s.setData05("d5");
		s.setData06("d6");
		s.setData07("d7");
		s.setData08("d8");
		s.setData09("d9");
		
		ExcelInpackSubRes sub = new ExcelInpackSubRes();
		sub.setHsCode("hs code1");
		sub.setEngNm("enNm1");
		sub.setItemCount(40d);
		sub.setItemPrice(3d);
		
		List<ExcelInpackSubRes> subList = new ArrayList<>();
		subList.add(sub);
		s.setSubItem(subList);
		
		_excelService.inpack(s, response);
	}
	

}
