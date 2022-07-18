package com.keepgo.whatdo.service.finalInbound;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.keepgo.whatdo.entity.customs.request.FinalInboundReq;
import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.entity.customs.response.FinalInboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;

@Service
public interface FinalInboundService {

	@Transactional
	List<?> getAll(FinalInboundReq finalInboundReq); 

	@Transactional
	List<FinalInboundRes> getAllCondition(FinalInboundReq finalInboundReq); 
	@Transactional
	List<FinalInboundRes> getFIList(FinalInboundReq finalInboundReq); 
	
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
	
	@Transactional
	boolean excelRead(MultipartFile file,InboundReq inboundReq,String id, String loginId) throws IOException;
	
	@Transactional
	boolean deleteFinalInbound(FinalInboundReq finalInboundReq);
}
