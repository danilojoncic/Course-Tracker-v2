package com.fullstack.coursetracker.repository;

import com.fullstack.coursetracker.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor,Long> {
    boolean existsByName(String name);
    Optional<Instructor> findByName(String name);
}
