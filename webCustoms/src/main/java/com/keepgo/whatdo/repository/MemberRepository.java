package com.keepgo.whatdo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{

	Member findByMemberId(String memberId); //핸드폰 번호
	Member findBySeq(Long seq);

}
