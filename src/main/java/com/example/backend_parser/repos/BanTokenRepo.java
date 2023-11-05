package com.example.backend_parser.repos;

import com.example.backend_parser.entities.BanToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BanTokenRepo extends JpaRepository<BanToken, Long>, JpaSpecificationExecutor<BanToken> {
    BanToken findByToken(String token);

    BanToken findByTokenAndExchange(String token, String exchange);
    BanToken findByExchange(String exchange);
    boolean existsByToken(String token);
    boolean existsByExchange(String exchange);
}
