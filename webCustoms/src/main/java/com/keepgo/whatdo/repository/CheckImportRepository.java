package com.keepgo.whatdo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.customs.CheckImport;

@Repository
public interface CheckImportRepository extends JpaRepository<CheckImport, Long> {

	CheckImport findByInboundMasterIdAndCompanyInfoId(Long inboundMasterId, Long companyInfoId);
	List<CheckImport> findByCompanyInfoId(Long companyInfoId);
	CheckImport findByInboundMasterId(Long inboundMasterId);
	
	
	
	
}