package com.example.backend_parser.api.repositories;

import com.example.backend_parser.api.table_entities.BanToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BanTokenRepo extends JpaRepository<BanToken, Long>, JpaSpecificationExecutor<BanToken> {
    List<BanToken> findByToken(String token);
    BanToken findByTokenAndExchange(String token, String exchange);
    boolean existsByTokenAndExchange(String token, String exchange);
    List<BanToken> findByExchange(String exchange);
    boolean existsByToken(String token);
    boolean existsByExchange(String exchange);
}
