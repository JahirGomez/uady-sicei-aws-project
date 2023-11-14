package com.uady.sicei.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.uady.sicei.Model.Profesor;
import com.uady.sicei.Model.Request.PreProfesorInfo;

@Service
public class ProfesorService {
    private List<Profesor> profesores = new ArrayList<>();

    public ProfesorService(){
        this.profesores = new ArrayList<>();
    }

    public List<Profesor> getProfesores(){
        return profesores;
    }

    public Profesor getProfesorById(int id){
        for (Profesor profesor : profesores) {
            if (profesor.getId() == id) {
                return profesor;
            }
        }
        return null;
    }

    public Profesor createProfesor(PreProfesorInfo profesorAux){
        Profesor nuevoProfesor = new Profesor(profesorAux.getId(), profesorAux.getNumeroEmpleado(), profesorAux.getNombres(), profesorAux.getApellidos(), profesorAux.getHorasClase());

        profesores.add(nuevoProfesor);
        return nuevoProfesor;
    }

    public Profesor actualizar(int id, PreProfesorInfo profesorAux){
        Optional<Profesor> profesorExistente = profesores.stream().filter(a -> a.getId() == id).findFirst();
        profesorExistente.ifPresent(value -> {
            value.setNumeroEmpleado(profesorAux.getNumeroEmpleado());
            value.setNombres(profesorAux.getNombres());
            value.setApellidos(profesorAux.getApellidos());
            value.setHorasClase(profesorAux.getHorasClase());
        });
        return profesorExistente.orElse(null);
    }

    public Profesor deleteProfesor(int id){
        Profesor profesorAEliminar = null;
        for (Profesor profesor : profesores) {
            if (profesor.getId() == id) {
                profesorAEliminar = profesor;
                break;
            }
        }

        if (profesorAEliminar != null) {
            profesores.remove(profesorAEliminar);
        }
        return profesorAEliminar;
    }
}
