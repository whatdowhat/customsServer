package com.keepgo.whatdo.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CompanyInfo;

@Repository
public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, Long> {

	List<CompanyInfo> findAll(Specification<CompanyInfo> condition);
	//public List<CompanyInfo> findByCoNm(String coNm);
	
}