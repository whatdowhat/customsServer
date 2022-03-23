package com.keepgo.whatdo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CommonMaster;

@Repository
public interface CommonRepository extends JpaRepository<Common, Long>{

	Page<Common> findAll(Specification<Common> condition,Pageable pageable);
	List<Common> findAll(Specification<Common> condition);
	public Common findByNm(String nm);
	
	@Query("select m from Common m ")
	List<Common> findAll();
	List<Common> findByCommonMaster(CommonMaster commonMaster);
	@Query(
			value =
			""
			+"		SELECT count(*)  FROM web_common "
			+"		WHERE 1=1                       "
			+"		AND commonMasterId = :commonMasterId          "
			+"		AND `value` = :value            "
			,nativeQuery = true
			)
	int findMasterIdValue(@Param("commonMasterId")Long commonMasterId,@Param("value") String value);
	
	@Query(
			value =
			""
			+"		SELECT *  FROM web_common "
			+"		WHERE 1=1                       "
			+"		AND commonMasterId = :commonMasterId          "
			+"		AND `value` = :value            "
			,nativeQuery = true
			)
	Common getComonMasterValue(@Param("commonMasterId")Long commonMasterId,@Param("value") String value);
}
