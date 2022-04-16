package com.keepgo.whatdo.service.company;

import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.keepgo.whatdo.entity.customs.CompanyInfo;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.request.CompanyInfoReq;
import com.keepgo.whatdo.entity.customs.response.CommonRes;
import com.keepgo.whatdo.entity.customs.response.CompanyInfoRes;

@Service
public interface CompanyInfoService {

	@Transactional
	List<?> getAll(CompanyInfoReq companyInfoReq); 

	@Transactional
	CompanyInfoRes getCompanyInfoExports(CompanyInfoReq companyInfoReq); 
	
	@Transactional
	boolean removeCompanyInfo(CompanyInfoReq companyInfoReq);
	@Transactional
	boolean createCompanyInfo(CompanyInfoReq companyInfoReq);
	@Transactional
	CompanyInfoRes deleteCompanyInfo(CompanyInfoReq companyInfoReq);
	
	@Transactional
	CompanyInfoRes addCompanyInfo(CompanyInfoReq companyInfoReq);
	
	@Transactional
	CompanyInfoRes updateCompanyInfo(CompanyInfoReq companyInfoReq);
	
	@Transactional
	CompanyInfoRes addCompanyInfoExports(CompanyInfoReq companyInfoReq);
	
	@Transactional
	CompanyInfoRes addCompanyInfoManages(CompanyInfoReq companyInfoReq);
	
	@Transactional
	List<?> excelUpload(MultipartFile file,CompanyInfoReq companyInfoReq) throws IOException; 
}
