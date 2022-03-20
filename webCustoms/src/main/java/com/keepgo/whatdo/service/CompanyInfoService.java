package com.keepgo.whatdo.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.keepgo.whatdo.entity.customs.response.CompanyInfoReq;

@Service
public interface CompanyInfoService {

	@Transactional
	List<?> getAll(CompanyInfoReq companyInfoReq); 
	@Transactional
	boolean removeCompanyInfo(CompanyInfoReq companyInfoReq);
	@Transactional
	boolean createCompanyInfo(CompanyInfoReq companyInfoReq);
}
