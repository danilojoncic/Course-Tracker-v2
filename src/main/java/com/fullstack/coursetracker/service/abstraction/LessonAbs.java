package com.fullstack.coursetracker.service.abstraction;

import com.fullstack.coursetracker.dto.CreateLesson;
import com.fullstack.coursetracker.dto.CriteriaObject;
import com.fullstack.coursetracker.dto.ServiceResponse;

import java.util.List;

public interface LessonAbs {
    ServiceResponse createLesson(CreateLesson dto);
    ServiceResponse getOneLesson(Long id);
    ServiceResponse getAllLessons();
    ServiceResponse getAllLessonsPaginated(Integer page, Integer size);
    ServiceResponse editOneLesson(Long id, CreateLesson dto);
    ServiceResponse deleteOneLesson(Long id);
    ServiceResponse getLessonsPerCriteria(CriteriaObject criteria);

}
