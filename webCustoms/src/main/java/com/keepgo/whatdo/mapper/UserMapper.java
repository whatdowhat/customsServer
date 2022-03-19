package com.keepgo.whatdo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.keepgo.whatdo.entity.customs.Inbound;
import com.keepgo.whatdo.entity.customs.User;

@Mapper
public interface UserMapper {
	public List<User> selectAll();
	List<User> selectBySeq(Long id);
	List<User> selectByloginId(String loginId);
}
