package com.uady.sicei.Controller;

import java.util.List;
import java.util.Optional;

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
import com.uady.sicei.Model.Profesor;
import com.uady.sicei.Model.Request.PreProfesorInfo;
import com.uady.sicei.Services.ProfesorService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/profesores")
@CrossOrigin(origins = {"*"})
public class ProfesorController {
    private final ProfesorService profesorService;

    public ProfesorController(ProfesorService profesorService){
        this.profesorService = profesorService;
    }

    @GetMapping("")
    @Operation(summary = "Obtener todos los profesores")
    public ResponseEntity<List<PreProfesorInfo>> getAllProfesores() {
        List<PreProfesorInfo> profesores = this.profesorService.getProfesores();
        return new ResponseEntity<>(profesores, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener profesor por ID")
    public ResponseEntity<PreProfesorInfo> getProfesorById(@PathVariable int id){
        Optional<PreProfesorInfo> newProfesor = profesorService.getProfesorById(id);
        if (newProfesor.isPresent()) {
            PreProfesorInfo profesor = newProfesor.get();
            return new ResponseEntity<>(profesor, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @Operation(summary = "Crear nuevo profesor")
    public ResponseEntity<PreProfesorInfo> createProfesor(@Valid @RequestBody PreProfesorInfo info){
        PreProfesorInfo newProfesor = this.profesorService.createProfesor(info);
        return new ResponseEntity<>(newProfesor,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar profesor por ID")
    public ResponseEntity<PreProfesorInfo> editProfesorByid(@Valid @RequestBody PreProfesorInfo info, @PathVariable int id){
        PreProfesorInfo newProfesor = profesorService.actualizar(id, info);
        if (newProfesor != null) {
            return new ResponseEntity<>(newProfesor, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "eliminar profesor")
    public ResponseEntity<Profesor> delete(@PathVariable int id){
        boolean deleted = profesorService.deleteProfesor(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
