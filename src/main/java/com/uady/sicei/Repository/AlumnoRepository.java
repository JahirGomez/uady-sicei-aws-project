package com.uady.sicei.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uady.sicei.Model.Request.PreAlumnoInfo;

@Repository
public interface AlumnoRepository extends CrudRepository<PreAlumnoInfo, Integer> {
    
}
