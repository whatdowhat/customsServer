package com.keepgo.whatdo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.customs.CompanyInfo;
import com.keepgo.whatdo.entity.customs.Inbound;

@Repository
public interface InboundRepository extends JpaRepository<Inbound, Long>{
	List<Inbound> findByInboundMasterId(Long inboundMasterId);
}
