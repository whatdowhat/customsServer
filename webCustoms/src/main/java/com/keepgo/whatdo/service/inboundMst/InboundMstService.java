package com.keepgo.whatdo.service.inboundMst;

import java.text.ParseException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.keepgo.whatdo.entity.customs.request.FinalInboundInboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.InboundMasterReq;
import com.keepgo.whatdo.entity.customs.response.FinalInboundInboundMasterRes;
import com.keepgo.whatdo.entity.customs.response.FinalInboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundMasterRes;

@Service
public interface InboundMstService {

	@Transactional
	List<?> getInboundMaster();
	
	@Transactional
	InboundMasterRes  getOne(Long id);
	
	@Transactional
	InboundMasterRes addInboundMaster(InboundMasterReq inboundMasterReq);
	@Transactional
	InboundMasterRes createInboundMaster(InboundMasterReq inboundMasterReq);

	@Transactional
	InboundMasterRes updateInboundMaster(InboundMasterReq inboundMasterReq) throws ParseException;

	@Transactional
	InboundMasterRes getInboundMaster(Long inboundMstId);
	
	@Transactional
	FinalInboundInboundMasterRes getMappingInfo(FinalInboundInboundMasterReq req);
	
}
