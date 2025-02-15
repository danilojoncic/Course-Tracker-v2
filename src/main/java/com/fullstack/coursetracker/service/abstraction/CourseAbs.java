package com.fullstack.coursetracker.service.abstraction;

import com.fullstack.coursetracker.dto.CreateCourse;
import com.fullstack.coursetracker.dto.ServiceResponse;

import java.net.CacheResponse;

public interface CourseAbs {
    ServiceResponse createCourse(CreateCourse dto);
    ServiceResponse deleteCourse(Long id);
    ServiceResponse editCourse(Long id, CreateCourse dto);
    ServiceResponse getOneCourse(Long id);
    ServiceResponse getAllCourses();
    ServiceResponse getCoursesPaginated(Integer page, Integer size);
}
