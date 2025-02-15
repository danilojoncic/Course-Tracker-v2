package com.fullstack.coursetracker.repository;

import com.fullstack.coursetracker.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course,Long> {
    Optional<Course> findByTitle(String title);
    boolean existsByTitle(String title);
}
