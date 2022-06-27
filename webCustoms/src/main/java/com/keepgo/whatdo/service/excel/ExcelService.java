package com.keepgo.whatdo.service.excel;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.keepgo.whatdo.entity.customs.FinalInboundInboundMaster;
import com.keepgo.whatdo.entity.customs.request.FinalInboundInboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.FinalInboundReq;
import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.entity.customs.response.ExcelCLPRes;
import com.keepgo.whatdo.entity.customs.response.ExcelCORes;
import com.keepgo.whatdo.entity.customs.response.ExcelContainerRes;
import com.keepgo.whatdo.entity.customs.response.ExcelFTARes;
import com.keepgo.whatdo.entity.customs.response.ExcelInboundRes;
import com.keepgo.whatdo.entity.customs.response.ExcelInpackRes;
import com.keepgo.whatdo.entity.customs.response.ExcelRCEPRes;
import com.keepgo.whatdo.entity.customs.response.ExcelYATAIRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundViewRes;

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
	
	@Transactional
	ExcelInpackRes inpackData(FinalInboundInboundMasterReq req) throws Exception;
	
	@Transactional
	boolean inpack(ExcelInpackRes excelInpackRes,HttpServletResponse response) throws Exception;
	
	@Transactional
	List<ExcelContainerRes> containerData(FinalInboundReq req) throws Exception;
	
	@Transactional
	boolean container(List<ExcelContainerRes> list,HttpServletResponse response) throws Exception;
	
	@Transactional
	List<InboundRes> inboundData(InboundReq inboundReq) throws Exception;
	@Transactional
	boolean inbound(List<InboundRes> list, HttpServletResponse response) throws Exception;
	
	@Transactional
	List<ExcelCLPRes> clpData(FinalInboundReq req) throws Exception;
	
	@Transactional
	boolean clp(List<ExcelCLPRes> list, HttpServletResponse response) throws Exception;
	
	@Transactional
	List<ExcelInpackRes> userInpackData(FinalInboundInboundMasterReq req) throws Exception;
	
	@Transactional
	List<ExcelCORes> cOData(FinalInboundInboundMasterReq req) throws Exception;
}
