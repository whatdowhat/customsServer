package com.keepgo.whatdo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.MailTO;

@Repository
public interface MailTORepository extends JpaRepository<MailTO, Long>{

}
