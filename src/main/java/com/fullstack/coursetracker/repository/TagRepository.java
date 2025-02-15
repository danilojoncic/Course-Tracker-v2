package com.fullstack.coursetracker.repository;

import com.fullstack.coursetracker.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Long> {
    Optional<Tag> findByTitle(String title);
}
