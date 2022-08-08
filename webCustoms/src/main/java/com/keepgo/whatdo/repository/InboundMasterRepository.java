package com.keepgo.whatdo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.customs.CompanyInfo;
import com.keepgo.whatdo.entity.customs.FinalInbound;
import com.keepgo.whatdo.entity.customs.InboundMaster;

@Repository
public interface InboundMasterRepository extends JpaRepository<InboundMaster, Long>{
	InboundMaster findByBlNo(String blNo);
	List<InboundMaster> findByBlNoContainingIgnoreCase(String blNo);
	List<InboundMaster> findByCompanyInfo(CompanyInfo companyInfo);
}
