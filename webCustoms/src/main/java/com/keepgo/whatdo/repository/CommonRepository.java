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
public interface CommonRepository extends JpaRepository<Common, Long>{

	Page<Common> findAll(Specification<Common> condition,Pageable pageable);
	List<Common> findAll(Specification<Common> condition);
	public Common findByNm(String nm);
	
	@Query("select m from Common m ")
	List<Common> findAll();
}
