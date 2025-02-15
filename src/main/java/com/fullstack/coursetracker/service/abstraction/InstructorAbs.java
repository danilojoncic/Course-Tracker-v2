package com.fullstack.coursetracker.service.abstraction;

import com.fullstack.coursetracker.dto.CreateInstructor;
import com.fullstack.coursetracker.dto.ServiceResponse;

import java.util.Date;

public interface InstructorAbs {
    ServiceResponse createInstructor(CreateInstructor dto);
    ServiceResponse deleteInstructor(Long id);
    ServiceResponse editInstructor(Long id, CreateInstructor dto);
    ServiceResponse getOneInstructor(Long id);
    ServiceResponse getAllInstructors();
    ServiceResponse getAllInstructorsPaginated(Integer page, Integer size);
}
