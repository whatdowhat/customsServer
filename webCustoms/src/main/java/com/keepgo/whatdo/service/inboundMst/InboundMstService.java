package com.keepgo.whatdo.service.inboundMst;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.keepgo.whatdo.entity.customs.request.InboundMasterReq;
import com.keepgo.whatdo.entity.customs.response.InboundMasterRes;

@Service
public interface InboundMstService {

	@Transactional
	List<?> getInboundMaster();

	@Transactional
	InboundMasterRes addInboundMaster(InboundMasterReq inboundMasterReq);
	@Transactional
	InboundMasterRes createInboundMaster(InboundMasterReq inboundMasterReq);

	@Transactional
	InboundMasterRes updateInboundMaster(InboundMasterReq inboundMasterReq);

	@Transactional
	InboundMasterRes getInboundMaster(Long inboundMstId);
	
}
