package com.keepgo.whatdo.service.finalInbound;

import java.text.ParseException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.keepgo.whatdo.entity.customs.request.FinalInboundReq;
import com.keepgo.whatdo.entity.customs.response.FinalInboundRes;

@Service
public interface FinalInboundService {

	@Transactional
	List<?> getAll(FinalInboundReq finalInboundReq); 

	@Transactional
	List<FinalInboundRes> getAllCondition(FinalInboundReq finalInboundReq); 
	
	@Transactional
	FinalInboundRes  getOne(Long id); 
	
	
	@Transactional
	boolean createFinalInbound(FinalInboundReq finalInboundReq);
	
	@Transactional
	FinalInboundRes addFinalInbound(FinalInboundReq finalInboundReq)throws ParseException;
	
	@Transactional
	boolean updateFinalInbound(FinalInboundReq finalInboundReq);
	
	@Transactional
	boolean deleteFinalInboundMasterItems(FinalInboundReq finalInboundReq);
	
	@Transactional
	boolean addFinalInboundMasterItems(FinalInboundReq finalInboundReq);
	
	@Transactional
	boolean addDataFinalInboundMasterItems(FinalInboundReq finalInboundReq);
	
}
