package com.keepgo.whatdo.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.customs.FinalInbound;

@Repository
public interface FinalInboundRepository extends JpaRepository<FinalInbound, Long> {

	
	FinalInbound findByCargoName(String cargoName);
	
//	List<CommonMaster> findAll(Specification<CommonMaster> condition);
	List<FinalInbound> findAll(Specification<FinalInbound> condition);
}