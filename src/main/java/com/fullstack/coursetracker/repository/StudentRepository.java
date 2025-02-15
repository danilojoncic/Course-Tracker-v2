package com.fullstack.coursetracker.repository;

import com.fullstack.coursetracker.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {
    Optional<Student> findByName(String name);
}
