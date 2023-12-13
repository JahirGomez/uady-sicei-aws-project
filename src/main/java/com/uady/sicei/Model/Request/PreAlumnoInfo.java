package com.uady.sicei.Model.Request;

import org.springframework.data.annotation.Id;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class PreAlumnoInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @NotBlank
    private String nombres;
    @NotNull
    @NotBlank
    private String apellidos;

    @Pattern(regexp = "^[a-zA-Z]+\\d+$", message = "La matrícula debe comenzar con letras seguidas de al menos un número")
    private String matricula;
    @DecimalMin(value = "0.0", inclusive = true, message = "El campo 'promedio' debe ser un número decimal mayor o igual a 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "El campo 'promedio' debe ser un número decimal menor o igual a 100")
    private double promedio;
    
    private String fotoPerfilUrl;
    private String password;

}
