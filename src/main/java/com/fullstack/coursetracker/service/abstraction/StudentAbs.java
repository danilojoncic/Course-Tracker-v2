package com.fullstack.coursetracker.service.abstraction;

import com.fullstack.coursetracker.dto.CreateStudent;
import com.fullstack.coursetracker.dto.ServiceResponse;

public interface StudentAbs {
    ServiceResponse createStudent(CreateStudent dto);
    ServiceResponse deleteStudent(Long id);
    ServiceResponse editStudent(Long id, CreateStudent dto);
    ServiceResponse getAllStudents();
    ServiceResponse getOneStudent(Long id);
    ServiceResponse getAllStudentsPaginated(Integer page, Integer size);

}
