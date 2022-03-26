package com.keepgo.whatdo.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.customs.CompanyInfoExport;
import com.keepgo.whatdo.entity.customs.CompanyInfoManage;

@Repository
public interface CompanyInfoManageRepository extends JpaRepository<CompanyInfoManage, Long> {

	//삭제쿼리 Can not issue data manipulation statements with executeQuery() 방지 아래2개
	//@Transactional, @Modifying
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(
			value =
			""
			+"		DELETE FROM web_companyInfoManage "
			+"		WHERE 1=1                       "
			+"		AND companInfoyId = :companInfoyId          "
			
			,nativeQuery = true
			)
	int deleteCompanyinfoManage(@Param("companInfoyId")Long companInfoyId);
	
	
}