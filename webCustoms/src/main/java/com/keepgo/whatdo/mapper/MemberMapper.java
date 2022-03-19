package com.keepgo.whatdo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.keepgo.whatdo.entity.BaseInfo;
import com.keepgo.whatdo.entity.Member;
import com.keepgo.whatdo.entity.PageVO;

@Mapper
public interface MemberMapper {
	
	Member selectAlreadyMember(String phone, String name);
	List<Member> selectAllMember();
	List<Member> selectBySeq(String seq);
	List<Member> selectPage(PageVO pageVO);
	List<Member> selectPageSearch(PageVO pageVO);
	int selectPageSearchCount(PageVO pageVO);
	int selectPageCount();
	
}
