package com.keepgo.whatdo.service.excel;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.keepgo.whatdo.entity.customs.request.FinalInboundInboundMasterReq;
import com.keepgo.whatdo.entity.customs.response.ExcelFTARes;
import com.keepgo.whatdo.entity.customs.response.ExcelRCEPRes;
import com.keepgo.whatdo.entity.customs.response.ExcelYATAIRes;

@Service
public interface ExcelService {

	
	@Transactional
	ExcelFTARes ftaData(FinalInboundInboundMasterReq req) throws Exception;
	
	@Transactional
	boolean fta(ExcelFTARes excelFTARes,HttpServletResponse response) throws Exception;
	
	@Transactional
	ExcelRCEPRes rcepData(FinalInboundInboundMasterReq req) throws Exception;
	
	@Transactional
	boolean rcep(ExcelRCEPRes excelRCEPRes,HttpServletResponse response) throws Exception;
	
	@Transactional
	ExcelYATAIRes yataiData(FinalInboundInboundMasterReq req) throws Exception;
	
	@Transactional
	boolean yatai(ExcelYATAIRes excelYATAIRes,HttpServletResponse response) throws Exception;
}