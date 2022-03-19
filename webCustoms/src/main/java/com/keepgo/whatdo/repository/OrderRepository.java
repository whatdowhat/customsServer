package com.keepgo.whatdo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keepgo.whatdo.entity.mes.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

}
