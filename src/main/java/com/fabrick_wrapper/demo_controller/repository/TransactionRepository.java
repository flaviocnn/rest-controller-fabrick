package com.fabrick_wrapper.demo_controller.repository;

import com.fabrick_wrapper.demo_controller.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
