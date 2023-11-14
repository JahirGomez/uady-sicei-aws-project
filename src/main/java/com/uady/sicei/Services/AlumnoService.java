package com.uady.sicei.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.uady.sicei.Model.Alumno;
import com.uady.sicei.Model.Request.PreAlumnoInfo;

@Service
public class AlumnoService {
    private List<Alumno> alumnos = new ArrayList<>();

    public AlumnoService() {
        this.alumnos = new ArrayList<>();
    }

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public Alumno getAlumnoById(int id){
        for (Alumno alumno : alumnos) {
            if (alumno.getId() == id) {
                return alumno;
            }
        }
        return null;
    }

    public Alumno createAlumno(PreAlumnoInfo alumnoAux){
        Alumno nuevoAlumno = new Alumno(alumnoAux.getId(), alumnoAux.getNombres(), alumnoAux.getApellidos(), alumnoAux.getMatricula(), alumnoAux.getPromedio());

        alumnos.add(nuevoAlumno);
        return nuevoAlumno;
    }

    public Alumno actualizar(int id, PreAlumnoInfo alumnoAux){
        Optional<Alumno> alumnoExistente = alumnos.stream().filter(a -> a.getId() == id).findFirst();
        alumnoExistente.ifPresent(value -> {
            value.setNombres(alumnoAux.getNombres());
            value.setApellidos(alumnoAux.getApellidos());
            value.setMatricula(alumnoAux.getMatricula());
            value.setPromedio(alumnoAux.getPromedio());
        });
        return alumnoExistente.orElse(null);
    }

    public Alumno deleteAlumno(int id){
        Alumno alumnoAEliminar = null;
        for (Alumno alumno : alumnos) {
            if (alumno.getId() == id) {
                alumnoAEliminar = alumno;
                break;
            }
        }

        if (alumnoAEliminar != null) {
            alumnos.remove(alumnoAEliminar);
            System.out.println("Alumno con ID " + id + " eliminado correctamente.");
        } else {
            System.out.println("No se encontró ningún alumno con el ID: " + id);
        }

        return alumnoAEliminar;
    }
}
