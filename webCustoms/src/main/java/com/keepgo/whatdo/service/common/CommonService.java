package com.keepgo.whatdo.service.common;

import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.keepgo.whatdo.entity.customs.request.CommonMasterReq;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.request.CompanyInfoReq;
import com.keepgo.whatdo.entity.customs.response.CommonRes;

@Service
public interface CommonService {

	@Transactional
	List<?> getMasterAll(CommonMasterReq commonMasterReq); 
	@Transactional
	List<?> getCommonAll(CommonReq commonReq); 
	
	@Transactional
	List<?> getCommonByMaster(CommonReq commonReq); 
	
	@Transactional
	CommonRes deleteCommonData(CommonReq commonReq);
	
	@Transactional
	CommonRes addCommonData(CommonReq commonReq);
	
	@Transactional
	CommonRes updateCommonData(CommonReq commonReq);
	
	
	@Transactional
	List<?> excelUpload(MultipartFile file,CommonReq commonReq) throws IOException; 
	
}
