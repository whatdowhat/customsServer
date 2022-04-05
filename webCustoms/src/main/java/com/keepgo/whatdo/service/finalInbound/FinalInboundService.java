package com.keepgo.whatdo.service.finalInbound;

import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.keepgo.whatdo.entity.customs.CompanyInfo;
import com.keepgo.whatdo.entity.customs.FinalInbound;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.request.CompanyInfoReq;
import com.keepgo.whatdo.entity.customs.request.FinalInboundReq;
import com.keepgo.whatdo.entity.customs.response.CommonRes;
import com.keepgo.whatdo.entity.customs.response.CompanyInfoRes;
import com.keepgo.whatdo.entity.customs.response.FinalInboundRes;

@Service
public interface FinalInboundService {

	@Transactional
	List<?> getAll(FinalInboundReq finalInboundReq); 

	@Transactional
	FinalInboundRes  getOne(Long id); 
	
	
	@Transactional
	boolean createFinalInbound(FinalInboundReq finalInboundReq);
	@Transactional
	boolean updateFinalInbound(FinalInboundReq finalInboundReq);
	
	@Transactional
	boolean deleteFinalInboundMasterItems(FinalInboundReq finalInboundReq);
	
	@Transactional
	boolean addFinalInboundMasterItems(FinalInboundReq finalInboundReq);
	
}
