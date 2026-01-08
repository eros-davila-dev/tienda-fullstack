package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data              // Crea Getters y Setters
@Builder           // Permite construir objetos fácil
@AllArgsConstructor
@NoArgsConstructor // <--- ¡ESTE ES EL QUE TE FALTA Y CAUSA EL ERROR 400!
public class AuthenticationRequest {

    private String username;
    private String password;
}