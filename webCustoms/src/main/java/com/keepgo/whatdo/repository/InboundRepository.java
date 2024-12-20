package com.keepgo.whatdo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.customs.CompanyInfo;
import com.keepgo.whatdo.entity.customs.FinalInbound;
import com.keepgo.whatdo.entity.customs.Inbound;

@Repository
public interface InboundRepository extends JpaRepository<Inbound, Long>{
	List<Inbound> findByInboundMasterId(Long inboundMasterId);
	List<Inbound> findByKorNmContainingIgnoreCase(String korNm);
	List<Inbound> findByEngNmContainingIgnoreCase(String engNm);
	List<Inbound> findByMemo1ContainingIgnoreCaseOrMemo2ContainingIgnoreCase(String memo1, String memo2);
}
