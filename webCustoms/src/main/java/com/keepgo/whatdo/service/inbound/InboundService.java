package com.keepgo.whatdo.service.inbound;

import java.util.List;

import javax.persistence.Transient;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.entity.customs.response.InboundRes;

@Service
public interface InboundService {

	@Transactional
	public List<InboundRes> getList(InboundReq inboundReq);
}
