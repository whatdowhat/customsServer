package com.keepgo.whatdo.service.common;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.keepgo.whatdo.entity.customs.request.CommonMasterReq;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.request.CompanyInfoReq;

@Service
public interface CommonService {

	@Transactional
	List<?> getMasterAll(CommonMasterReq commonMasterReq); 
	@Transactional
	List<?> getCommonAll(CommonReq commonReq); 
	
	@Transactional
	List<?> getCommonByMaster(CommonReq commonReq); 
}
