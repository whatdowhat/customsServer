package com.keepgo.whatdo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.customs.CompanyInfo;

@Repository
public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, Long> {

	//public List<CompanyInfo> findByCoNm(String coNm);
	
}