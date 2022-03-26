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
import com.keepgo.whatdo.entity.customs.response.CommonRes;

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
	
	
	@Query(
			value =
			""
			+"select * , "
			+" 0 as `commonMasterId` , 1 as `isUsing` , 1 `user`, now() `createDt`, now() `updateDt` "
			+" from (                                                                                   "  
			+"		select a.id as id ,a.value ,a.value2,a.nm, b.id as companyInfoExportId, b.companInfoyId , b.preperOrder "
			+"			,CASE WHEN b.companInfoyId IS NULL THEN false                   	                    "
			+"			ELSE true end as isChecked	from (                                                      "
			+"			select * from web_common                                                                "
			+"			where 1=1                                                                               "
			+"			and commonMasterId = 2                                                                  "
			+"		    )  as a left join                                                                       "
			+"		    (                                                                                       "
			+"		    select * from web_companyInfoExport                                                     "
			+"			where 1=1                                                                               "
			+"			and companInfoyId = :companInfoyId                                                      "
			+"		) as b                                                                                      "
			+"		on a.id = b.commonId                                                                        "
			+"		) as cc                                                                                     "
			+"		order by isChecked desc , preperOrder                                                       "
			,nativeQuery = true
			)
	List<Common> checkedCompanyinfoExport(@Param("companInfoyId")Long companyInfoId);
	
	@Query(
			value =
			""
			+"select * , "
			+" 0 as `commonMasterId` , 1 as `isUsing` , 1 `user`, now() `createDt`, now() `updateDt` "
			+" from (                                                                                   "
			+"		select a.id as id ,a.value ,a.value2,a.nm, b.id as companyInfoExportId, b.companInfoyId , b.preperOrder "
			+"			,CASE WHEN b.companInfoyId IS NULL THEN false                   	                    "
			+"			ELSE true end as isChecked	from (                                                      "
			+"			select * from web_common                                                                "
			+"			where 1=1                                                                               "
			+"			and commonMasterId = 1                                                                  "
			+"		    )  as a left join                                                                       "
			+"		    (                                                                                       "
			+"		    select * from web_companyInfoManage                                                     "
			+"			where 1=1                                                                               "
			+"			and companInfoyId = :companInfoyId                                                      "
			+"		) as b                                                                                      "
			+"		on a.id = b.commonId                                                                        "
			+"		) as cc                                                                                     "
			+"		order by isChecked desc , preperOrder                                                       "
			
			
			,nativeQuery = true
			)
//	List<Common> checkedCompanyinfoManage(@Param("companInfoyId")Long companyInfoId);
	List<Common> checkedCompanyinfoManage(@Param("companInfoyId")Long companyInfoId);
	
}
