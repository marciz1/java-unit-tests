package com.matchtrade.javaunittests.domain.domain;

import com.matchtrade.javaunittests.domain.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {}
