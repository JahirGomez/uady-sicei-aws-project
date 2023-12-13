package com.uady.sicei.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<List<PreAlumnoInfo>> getAllAlumnos() {
        List<PreAlumnoInfo> alumnos = this.alumnoService.getAlumnos();
        return new ResponseEntity<>(alumnos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener estudiante por id")
    public ResponseEntity<PreAlumnoInfo> getAlumnoById(@PathVariable int id){
        Optional<PreAlumnoInfo> alumnoOptional = alumnoService.getAlumnoById(id);
        if (alumnoOptional.isPresent()) {
            PreAlumnoInfo alumno = alumnoOptional.get();
            return new ResponseEntity<>(alumno, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo estudiante")
    public ResponseEntity<PreAlumnoInfo> createAlumno(@Valid @RequestBody PreAlumnoInfo info){
        PreAlumnoInfo newAlumno = this.alumnoService.createAlumno(info);
        return new ResponseEntity<>(newAlumno, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar estudiante")
    public ResponseEntity<PreAlumnoInfo> editAlumnoByid(@Valid @RequestBody PreAlumnoInfo info, @PathVariable int id){
        PreAlumnoInfo updatedAlumno = this.alumnoService.actualizar(id, info);
        if (updatedAlumno != null) {
            return new ResponseEntity<>(updatedAlumno, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "eliminar estudiante")
    public ResponseEntity<PreAlumnoInfo> delete(@PathVariable int id){
        boolean alumnoAEliminar = this.alumnoService.deleteAlumno(id);
        if (alumnoAEliminar) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/fotoPerfil")
    @Operation(summary = "agregar foto estudiante")
    public ResponseEntity<Map<String, String>> uploadPhotoProfile(@PathVariable int id, @RequestParam("foto") MultipartFile file) {
        String fotoPerfilUrl = alumnoService.uploadPhotoProfile(id, file);
        Map<String, String> response = new HashMap<>();
        response.put("fotoPerfilUrl", fotoPerfilUrl);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PostMapping(value = "/{id}/email", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "enviar notificacion")
    public ResponseEntity<String> sendNotification(@PathVariable int id) {
        this.alumnoService.sendNotification(id);
        return ResponseEntity.ok("Notificación enviada");
    }

    @PostMapping("/{id}/session/login")
    @Operation(summary = "iniciar sesion")
    public ResponseEntity<Map<String, String>> login(@PathVariable int id, @RequestBody Alumno alumno) {
        String session = alumnoService.login(id, alumno.getPassword());
        Map<String, String> response = new HashMap<>();
        response.put("sessionString", session);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }


    @PostMapping("/{id}/session/verify")
    @Operation(summary = "verificar sesion")
    public ResponseEntity<Map<String, Boolean>> verifySession(@PathVariable int id, @RequestBody Map<String, String> requestBody) {
        String sessionString = requestBody.get("sessionString");
        Map<String, Boolean> response = new HashMap<>();

        if (sessionString != null) {
            boolean valid = alumnoService.verifySession(id, sessionString);
            response.put("isValid", valid);

            if (valid) {
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(response);
            }
        } else {
            response.put("isValid", false);
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(response);
        }
    }

    
    @PostMapping("/{id}/session/logout")
    @Operation(summary = "cerrar sesion")
    public ResponseEntity<Map<String, String>> logout(@PathVariable int id, @RequestBody Map<String, String> requestBody) {
        String sessionString = requestBody.get("sessionString");
        Map<String, String> response = new HashMap<>();

        if (sessionString != null) {
            alumnoService.logout(id, sessionString);
            response.put("message", "Sesión cerrada con éxito");
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        } else {
            response.put("error", "No se proporcionó una sesión");
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(response);
        }
    }
}
