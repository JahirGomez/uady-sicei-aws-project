package com.uady.sicei.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import com.uady.sicei.Model.Alumno;
import com.uady.sicei.Model.Request.PreAlumnoInfo;
import com.uady.sicei.Services.AlumnoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/alumnos")
@CrossOrigin(origins = {"*"})
public class AlumnoController {
    private final AlumnoService alumnoService;

    public AlumnoController(AlumnoService alumnoService){
        this.alumnoService = alumnoService;
    }

    @GetMapping("")
    @Operation(summary = "Obtener todos los estudiantes")
    public ResponseEntity<List<Alumno>> getAllAlumnos() {
        List<Alumno> alumnos = this.alumnoService.getAlumnos();
        return new ResponseEntity<>(alumnos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener estudiante por id")
    public ResponseEntity<Alumno> getAlumnoById(@PathVariable int id){
        Alumno newAlumno = this.alumnoService.getAlumnoById(id);
        if(newAlumno == null){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(newAlumno,HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo estudiante")
    public ResponseEntity<Alumno> createAlumno(@Valid @RequestBody PreAlumnoInfo info){
        Alumno newAlumno = this.alumnoService.createAlumno(info);
        return new ResponseEntity<>(newAlumno, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar estudiante")
    public ResponseEntity<Alumno> editAlumnoByid(@Valid @RequestBody PreAlumnoInfo info, @PathVariable int id){
        Alumno newAlumno = this.alumnoService.actualizar(id,info);
        return new ResponseEntity<>(newAlumno,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "eliminar estudiante")
    public ResponseEntity<Alumno> delete(@PathVariable int id){
        Alumno alumnoAEliminar = this.alumnoService.deleteAlumno(id);

        if (alumnoAEliminar != null) {
            return new ResponseEntity<>(alumnoAEliminar, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
