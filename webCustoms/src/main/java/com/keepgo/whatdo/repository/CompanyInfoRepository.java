package com.keepgo.whatdo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CompanyInfo;

@Repository
public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, Long> {

	List<CompanyInfo> findAll(Specification<CompanyInfo> condition);
	//public List<CompanyInfo> findByCoNm(String coNm);
	
	CompanyInfo findByCoNum(String coNum);
	
	CompanyInfo findByCoNm(String coNm);
	
//	CompanyInfo findByCompanyInfo(CompanyInfo companyInfo);
	
	@Transactional
	@Modifying
	@Query(
			value =
			""
			+"	delete from web_companyinfo	"
			+"	where 1=1                   "
			+"	and corpType = :corpType         "
			,nativeQuery = true
			)
	int deleteByCorpType(@Param("corpType")int corpType);
	
//	@Transactional
//	@Modifying
	@Query(
			value =
			""
			+" select * from web_companyinfo      "
			+" where 1=1                          "
			+" and coNum = :coNum           "
			+" and corpType = :corpType   "               
			,nativeQuery = true               
			)
	CompanyInfo findByCoNumCorpType(@Param("coNum")String coNum ,@Param("corpType")int corpType);
	
}