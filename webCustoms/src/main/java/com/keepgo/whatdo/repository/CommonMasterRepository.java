package com.keepgo.whatdo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CommonMaster;

@Repository
public interface CommonMasterRepository extends JpaRepository<CommonMaster, Long>{

	Page<CommonMaster> findAll(Specification<CommonMaster> condition,Pageable pageable);
	List<CommonMaster> findAll(Specification<CommonMaster> condition);
	CommonMaster findByName(String nm);
	
	@Query("select m from CommonMaster m ")
	List<CommonMaster> findAll2();
	
	@Query("select m.commons from CommonMaster m ")
	List<Common> findAll3();
			
	
}
