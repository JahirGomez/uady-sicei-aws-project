package com.uady.sicei.Model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Profesor {
    private int id;
    private int numeroEmpleado;
    private String nombres;
    private String apellidos;
    private int horasClase;
}
