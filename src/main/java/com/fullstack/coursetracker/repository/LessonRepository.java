package com.fullstack.coursetracker.repository;

import com.fullstack.coursetracker.dto.CriteriaObject;
import com.fullstack.coursetracker.model.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson,Long>, JpaSpecificationExecutor<Lesson> {
    //needs special annotation for it to work
    Optional<Lesson> findByTitle(String title);



}
