package com.keepgo.whatdo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.customs.Inbound;

@Repository
public interface InboundRepository extends JpaRepository<Inbound, Long>{

}
