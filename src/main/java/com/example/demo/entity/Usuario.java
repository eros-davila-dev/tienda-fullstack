package com.example.demo.entity;

import jakarta.persistence.*; // Importante: Spring Boot 3 usa 'jakarta', no 'javax'
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data // Lombok genera Getters, Setters y toString
@NoArgsConstructor // Lombok genera constructor vacío
@AllArgsConstructor // Lombok genera constructor con todo
@Table(name = "usuarios") // Asegura que coincida con tu tabla en MySQL
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 'unique = true' para que no haya dos usuarios con el mismo nombre
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
}