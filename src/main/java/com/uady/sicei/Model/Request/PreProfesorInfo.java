package com.uady.sicei.Model.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PreProfesorInfo {
    @Positive(message = "El campo 'id' debe ser un número entero mayor a 0")
    private int id;

    @Positive(message = "El campo 'numeroEmpleado' debe ser un número entero mayor a 0")
    private int numeroEmpleado;

    @NotNull
    @NotBlank
    private String nombres;

    @NotNull
    @NotBlank
    private String apellidos;

    @Positive(message = "El campo 'horasClase' debe ser un número entero positivo")
    private  int horasClase;
}
