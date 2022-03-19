package com.keepgo.whatdo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.customs.CompanyInfoExport;

@Repository
public interface CompanyInfoExportRepository extends JpaRepository<CompanyInfoExport, Long> {

	//public List<CompanyInfo> findByCoNm(String coNm);
	
}