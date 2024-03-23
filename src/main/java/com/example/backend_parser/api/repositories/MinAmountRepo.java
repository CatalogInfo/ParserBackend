package com.example.backend_parser.api.repositories;

import com.example.backend_parser.api.table_entities.MinAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface MinAmountRepo extends JpaRepository<MinAmount, Long>, JpaSpecificationExecutor<MinAmount> {
}