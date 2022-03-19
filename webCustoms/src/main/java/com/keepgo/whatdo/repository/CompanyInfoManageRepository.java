package com.keepgo.whatdo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.customs.CompanyInfoManage;

@Repository
public interface CompanyInfoManageRepository extends JpaRepository<CompanyInfoManage, Long> {

	//public List<CompanyInfo> findByCoNm(String coNm);
	
}