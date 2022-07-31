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
import com.keepgo.whatdo.entity.customs.response.FinalInboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundViewListRes;
import com.keepgo.whatdo.entity.customs.response.InboundViewRes;
import com.keepgo.whatdo.entity.customs.response.UnbiRes;

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
	boolean inbounds(InboundViewListRes listRes, HttpServletResponse response) throws Exception;
	
	@Transactional
	List<ExcelCLPRes> clpData(FinalInboundReq req) throws Exception;
	
	@Transactional
	boolean clp(List<ExcelCLPRes> list, HttpServletResponse response) throws Exception;
	
	@Transactional
	List<ExcelInpackRes> userInpackData(FinalInboundInboundMasterReq req) throws Exception;
	
	@Transactional
	List<ExcelCORes> cOData(FinalInboundInboundMasterReq req) throws Exception;
	
	@Transactional
	InboundViewListRes previewData(InboundReq inboundReq) throws Exception;
	
	@Transactional
	boolean preview(InboundViewListRes inboundViewListRes,List<UnbiRes> unbiList,FinalInboundRes container,HttpServletResponse response) throws Exception;
	@Transactional
	boolean previewContainer(InboundViewListRes inboundViewListRes,List<UnbiRes> unbiList,FinalInboundRes container,HttpServletResponse response) throws Exception;
	
	@Transactional
	List<ExcelInpackRes> listInpackData(FinalInboundInboundMasterReq req) throws Exception;
	@Transactional
	boolean listInpack(List<ExcelInpackRes> excelInpackRes,HttpServletResponse response) throws Exception;
	@Transactional
	List<ExcelFTARes> listFtaData(FinalInboundInboundMasterReq req) throws Exception;
	@Transactional
	boolean listFta(List<ExcelFTARes> excelFTARes,HttpServletResponse response) throws Exception;
	@Transactional
	List<ExcelRCEPRes> listRcepData(FinalInboundInboundMasterReq req) throws Exception;
	@Transactional
	boolean listRcep(List<ExcelRCEPRes> excelRCEPRes,HttpServletResponse response) throws Exception;
	@Transactional
	List<ExcelYATAIRes> listYataiData(FinalInboundInboundMasterReq req) throws Exception;
	@Transactional
	boolean listYatai(List<ExcelYATAIRes> excelYATAIRes,HttpServletResponse response) throws Exception;
}
