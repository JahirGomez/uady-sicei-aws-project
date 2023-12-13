package com.uady.sicei.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.uady.sicei.Model.Profesor;
import com.uady.sicei.Model.Request.PreProfesorInfo;
import com.uady.sicei.Repository.ProfesorRepository;

@Service
public class ProfesorService {

   @Autowired
   private ProfesorRepository profesorRepository;


    public List<PreProfesorInfo> getProfesores(){
        return (List<PreProfesorInfo>) profesorRepository.findAll();
    }

    public Optional<PreProfesorInfo> getProfesorById(int id){
        return profesorRepository.findById(id);
    }

    public PreProfesorInfo createProfesor(PreProfesorInfo profesorAux){
        if (profesorAux.getNombres() == null || profesorAux.getApellidos() == null || profesorAux.getNumeroEmpleado() == 0 || profesorAux.getHorasClase() == 0) {
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los datos de profesor son inválidos");
      }
      return profesorRepository.save(profesorAux);
    }

    public PreProfesorInfo actualizar(int id, PreProfesorInfo profesorAux){
        if (profesorAux.getNombres() == null || profesorAux.getApellidos() == null || profesorAux.getNumeroEmpleado() == 0 || profesorAux.getHorasClase() == 0) {
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los datos de profesor son inválidos");
        }
         Optional<PreProfesorInfo> profesorExistente = profesorRepository.findById(id);
         if (profesorExistente.isPresent()) {
            PreProfesorInfo profesorUpdated = profesorExistente.get();
            profesorUpdated.setNombres(profesorAux.getNombres());
            profesorUpdated.setApellidos(profesorAux.getApellidos());
            profesorUpdated.setHorasClase(profesorAux.getHorasClase());
            profesorUpdated.setNumeroEmpleado(profesorAux.getNumeroEmpleado());
            return profesorRepository.save(profesorUpdated);
         } 
        return profesorExistente.orElse(null);
    }

    public boolean deleteProfesor(int id){
        Optional<PreProfesorInfo> profesorAEliminar = profesorRepository.findById(id);
      if (profesorAEliminar.isPresent()) {
         PreProfesorInfo profesor = profesorAEliminar.get();
         profesorRepository.delete(profesor);
         return true;
      } else {
         return false;
      }
    }
}
