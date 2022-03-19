package com.keepgo.whatdo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.mes.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>{
}
