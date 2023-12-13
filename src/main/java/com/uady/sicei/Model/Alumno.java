package com.uady.sicei.Model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Alumno {
    private int id;
    private String nombres;
    private String apellidos;
    private String matricula;
    private double promedio;
    private String fotoPerfilUrl;
    private String password;
}
