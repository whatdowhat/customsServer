package com.keepgo.whatdo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.customs.CompanyInfoExport;

@Repository
public interface CompanyInfoExportRepository extends JpaRepository<CompanyInfoExport, Long> {

	//public List<CompanyInfo> findByCoNm(String coNm);
	//삭제쿼리 Can not issue data manipulation statements with executeQuery() 방지 아래2개
	//@Transactional, @Modifying
	@Transactional
	@Modifying
	@Query(
			value =
			""
			+"		DELETE FROM web_companyInfoExport "
			+"		WHERE 1=1                       "
			+"		AND companInfoyId = :companInfoyId          "
			
			,nativeQuery = true
			)
	int deleteCompanyinfoExport(@Param("companInfoyId")Long companInfoyId);
	
	 
	
}