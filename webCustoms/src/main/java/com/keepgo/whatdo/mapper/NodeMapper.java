package com.keepgo.whatdo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NodeMapper {
	public List<?> selectAll();
}
