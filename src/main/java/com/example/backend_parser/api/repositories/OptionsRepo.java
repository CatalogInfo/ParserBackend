package com.example.backend_parser.api.repositories;

import com.example.backend_parser.api.table_entities.Options;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionsRepo extends JpaRepository<Options, Long>, JpaSpecificationExecutor<Options> {
    boolean existsByName(String name);
    List<Options> findByName(String name);


}