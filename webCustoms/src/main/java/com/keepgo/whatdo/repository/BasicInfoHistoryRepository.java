package com.keepgo.whatdo.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.BaseInfo;
import com.keepgo.whatdo.entity.BaseInfoHistory;
import com.keepgo.whatdo.entity.Member;
import com.keepgo.whatdo.entity.Notice;

@Repository
public interface BasicInfoHistoryRepository extends JpaRepository<BaseInfoHistory, Long>{


	List<BaseInfoHistory> findByBaseInfoSeq(Long baseInfoSeq,Sort sort);
	
}
