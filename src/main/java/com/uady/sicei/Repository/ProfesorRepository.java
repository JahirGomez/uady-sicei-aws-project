package com.uady.sicei.Repository;

import org.springframework.stereotype.Repository;

import com.uady.sicei.Model.Request.PreProfesorInfo;

import org.springframework.data.repository.CrudRepository;


@Repository
public interface ProfesorRepository extends CrudRepository<PreProfesorInfo, Integer> {
    
}
