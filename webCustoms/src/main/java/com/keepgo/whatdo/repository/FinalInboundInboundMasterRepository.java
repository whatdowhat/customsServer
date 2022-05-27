package com.keepgo.whatdo.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CompanyInfo;
import com.keepgo.whatdo.entity.customs.FinalInbound;
import com.keepgo.whatdo.entity.customs.FinalInboundInboundMaster;

@Repository
public interface FinalInboundInboundMasterRepository extends JpaRepository<FinalInboundInboundMaster, Long> {

	FinalInboundInboundMaster findByFinalInboundIdAndInboundMasterId(Long finalInboundId, Long inboundMasterId);
	List<FinalInboundInboundMaster> findByFinalInboundId(Long finalInboundId);
	FinalInboundInboundMaster findByInboundMasterId(Long inboundMasterId);
	
	
	@Transactional
	@Modifying
	@Query(
			value =
			""
			+"		DELETE FROM web_final_inbound_inboundmst "
			+"		WHERE 1=1                       "
			+"		AND finalInboundId = :finalInboundId          "
			+"		AND inboundMasterId = :inboundMasterId          "
			,nativeQuery = true
			)
	int deleteFinalInboundInboundMaster(@Param("finalInboundId")Long finalInboundId,@Param("inboundMasterId")Long inboundMasterId);
	
	
}