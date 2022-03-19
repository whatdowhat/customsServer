package com.keepgo.whatdo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.Notice;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer>{


}
