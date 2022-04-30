package com.keepgo.whatdo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.customs.CompanyInfo;
import com.keepgo.whatdo.entity.customs.Inbound;
import com.keepgo.whatdo.entity.customs.Unbi;

@Repository
public interface UnbiRepository extends JpaRepository<Unbi, Long>{
	List<Unbi> findByFinalInboundId(Long finalInboundId);
}
