package com.keepgo.whatdo.service.inbound;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.keepgo.whatdo.entity.customs.Inbound;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.request.InboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.entity.customs.request.UserReq;
import com.keepgo.whatdo.entity.customs.response.CommonRes;
import com.keepgo.whatdo.entity.customs.response.InboundMasterRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundViewRes;

@Service
public interface InboundService {

	@Transactional
	public List<InboundRes> getList(InboundReq inboundReq);
	
	@Transactional
	List<?> getInboundMaster();
	
	@Transactional
	InboundMasterRes addInboundMaster(InboundMasterReq inboundMasterReq);
	
	@Transactional
	InboundMasterRes addInboundMasterCompany(InboundMasterReq inboundMasterReq);
	
	@Transactional
	InboundMasterRes addInboundMasterExport(InboundMasterReq inboundMasterReq);
	
	@Transactional
	InboundMasterRes updateInboundMaster(InboundMasterReq inboundMasterReq);
	
	@Transactional
	List<?> excelRead(MultipartFile file,InboundReq inboundReq) throws IOException;
	
	@Transactional
	InboundRes excelCommitInboundData(InboundReq inboundReq);
	
	
	@Transactional
	InboundRes inboundCommit(InboundReq inboundReq);
	
	@Transactional
	List<InboundRes> getInboundByInboundMasterId(InboundReq inboundReq);
	
	@Transactional
	List<InboundRes> getInboundByMasterId(Long id);
	
	@Transactional
	InboundRes deleteInbound(InboundReq inboundReq);
	@Transactional
	InboundViewRes changeInbound(List<InboundRes> list);
}
