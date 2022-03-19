package com.keepgo.whatdo.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.BaseInfo;
import com.keepgo.whatdo.entity.Member;
import com.keepgo.whatdo.entity.Notice;

@Repository
public interface BasicInfoRepository extends JpaRepository<BaseInfo, Long>{

	List<BaseInfo> findAll();
	BaseInfo findBySeq(Long seq);

}
