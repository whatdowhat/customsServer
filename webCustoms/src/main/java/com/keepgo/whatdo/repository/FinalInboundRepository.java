package com.keepgo.whatdo.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.customs.FinalInbound;

@Repository
public interface FinalInboundRepository extends JpaRepository<FinalInbound, Long> {

	
	FinalInbound findByTitle(String title);
	List<FinalInbound> findByIncomeDt(String IncomeDt);
//	List<CommonMaster> findAll(Specification<CommonMaster> condition);
	List<FinalInbound> findAll(Specification<FinalInbound> condition);
	List<FinalInbound> findByIncomeDtBetween(String start, String end);
	List<FinalInbound> findByDepartDtStrBetween(String start, String end);
	
	List<FinalInbound> findByChinaSanggumYn(int ChinaSanggumYn);
	List<FinalInbound> findByDepartDelayYn(int departDelayYn);
	List<FinalInbound> findByGwanriYn(int gwanriYn);
}