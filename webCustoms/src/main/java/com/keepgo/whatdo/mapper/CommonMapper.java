package com.keepgo.whatdo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.viewEntity.CommonViewResponse;

@Mapper
public interface CommonMapper {
	public List<CommonViewResponse> selectAll();
	List<Common> selectBySeq(Long id);
	List<Common> selectByNm(String nm);
	List<?> checkedCompanyinfoManage(Long companInfoyId);
	List<?> checkedCompanyinfoExport(Long companInfoyId);
}
