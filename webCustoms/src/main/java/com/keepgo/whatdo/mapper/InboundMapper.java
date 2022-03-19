package com.keepgo.whatdo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.keepgo.whatdo.entity.customs.Inbound;

@Mapper
public interface InboundMapper {
	public List<Inbound> selectAll();
}
