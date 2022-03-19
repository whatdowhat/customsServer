package com.keepgo.whatdo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.keepgo.whatdo.entity.BaseInfo;

@Mapper
public interface BaseInfoMapper {
	
	int deleteBeforeInsert(List<String>  list);
	int deleteBeforeInsertNew(String first,String end);
	
	List<BaseInfo> selectAllProjectAndTeam();
	List<BaseInfo> selectTREE(BaseInfo baseInfo);
	List<BaseInfo> selectProjectTREE(BaseInfo baseInfo);
	List<BaseInfo> selectNldProjectAndTeam(BaseInfo baseInfo);
	List<BaseInfo> selectNldTREE(BaseInfo baseInfo);
	List<BaseInfo> selectLdProjectAndTeam(BaseInfo baseInfo);
	List<BaseInfo> selectLdTREE(BaseInfo baseInfo);
	List<BaseInfo> selectLdTREEAfterEndDt(BaseInfo baseInfo);
	List<BaseInfo> selectGovProjectAndTeam(BaseInfo baseInfo);
	List<BaseInfo> selectGovTREE(BaseInfo baseInfo);
	List<BaseInfo> selectSendEmailTarget();
	
	//viewDataFinal
	List<BaseInfo> selectAllProjectAndTeamFinal();
	List<BaseInfo> selectTREEFinal(BaseInfo baseInfo);
	List<BaseInfo> selectProjectTREEFinal(BaseInfo baseInfo);
}
