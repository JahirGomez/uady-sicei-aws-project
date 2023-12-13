package com.uady.sicei.Model.Request;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Entity
@NoArgsConstructor
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

    public PreAlumnoInfo(int id, @NotNull @NotBlank String nombres, @NotNull @NotBlank String apellidos,
            @Pattern(regexp = "^[a-zA-Z]+\\d+$", message = "La matrícula debe comenzar con letras seguidas de al menos un número") String matricula,
            @DecimalMin(value = "0.0", inclusive = true, message = "El campo 'promedio' debe ser un número decimal mayor o igual a 0") @DecimalMax(value = "100.0", inclusive = true, message = "El campo 'promedio' debe ser un número decimal menor o igual a 100") double promedio) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.matricula = matricula;
        this.promedio = promedio;
    }

    public PreAlumnoInfo(int id, @NotNull @NotBlank String nombres, @NotNull @NotBlank String apellidos,
            @Pattern(regexp = "^[a-zA-Z]+\\d+$", message = "La matrícula debe comenzar con letras seguidas de al menos un número") String matricula,
            @DecimalMin(value = "0.0", inclusive = true, message = "El campo 'promedio' debe ser un número decimal mayor o igual a 0") @DecimalMax(value = "100.0", inclusive = true, message = "El campo 'promedio' debe ser un número decimal menor o igual a 100") double promedio,
            String fotoPerfilUrl) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.matricula = matricula;
        this.promedio = promedio;
        this.fotoPerfilUrl = fotoPerfilUrl;
    }

    
    

}
