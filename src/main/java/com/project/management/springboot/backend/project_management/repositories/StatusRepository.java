package com.project.management.springboot.backend.project_management.repositories;

import org.springframework.data.repository.CrudRepository;

import com.project.management.springboot.backend.project_management.entities.Status;

public interface StatusRepository extends CrudRepository<Status, Long>{
    
}
