package com.example.backend_parser.repos;

import com.example.backend_parser.entities.BanToken;
import com.example.backend_parser.entities.MinAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MinAmountRepo extends JpaRepository<MinAmount, Long>, JpaSpecificationExecutor<MinAmount> {
}