package com.example.backend_parser.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ban_list")
public class BanToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String exchange;
    private String token;
}
