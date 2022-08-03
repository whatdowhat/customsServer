package com.keepgo.whatdo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.customs.CheckImport;
import com.keepgo.whatdo.entity.customs.ChinaSanggum;
import com.keepgo.whatdo.entity.customs.DepartDelay;
import com.keepgo.whatdo.entity.customs.Manage;

@Repository
public interface DepartDelayRepository extends JpaRepository<DepartDelay, Long> {

//	CheckImport findByInboundMasterIdAndCompanyInfoId(Long inboundMasterId, Long companyInfoId);
//	List<CheckImport> findByCompanyInfoId(Long companyInfoId);
	DepartDelay findByFinalInboundId(Long finalInboundId);
	
	
	
	
}