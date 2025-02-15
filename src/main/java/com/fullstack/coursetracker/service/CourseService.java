package com.fullstack.coursetracker.service;

import com.fullstack.coursetracker.dto.CreateCourse;
import com.fullstack.coursetracker.dto.Message;
import com.fullstack.coursetracker.dto.ServiceResponse;
import com.fullstack.coursetracker.model.Course;
import com.fullstack.coursetracker.model.Lesson;
import com.fullstack.coursetracker.model.Transaction;
import com.fullstack.coursetracker.repository.CourseRepository;
import com.fullstack.coursetracker.repository.LessonRepository;
import com.fullstack.coursetracker.repository.TransactionRepository;
import com.fullstack.coursetracker.service.abstraction.CourseAbs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService implements CourseAbs {
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final TransactionRepository transactionRepository;

    public CourseService(CourseRepository courseRepository, LessonRepository lessonRepository, TransactionRepository transactionRepository) {
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public ServiceResponse createCourse(CreateCourse dto) {
        if(courseRepository.existsByTitle(dto.title())){
            return new ServiceResponse(401,
                    new Message("Course already exists!"));
        }else{
            Course course = new Course(dto.title(), dto.description());
            courseRepository.save(course);
            return new ServiceResponse(200,
                    new Message("Course successfully created!"));
        }
    }

    @Override
    @Transactional
    public ServiceResponse deleteCourse(Long id) {
        if(courseRepository.existsById(id)){
            List<Lesson> toDelete = new ArrayList<>();
            List<Transaction> toDeleteTransactions = new ArrayList<>();
            List<Transaction> transactions = transactionRepository.findAll();
            transactions.forEach(transaction -> {
                if(transaction.getLesson().getCourse().getId().equals(id)){
                    toDeleteTransactions.add(transaction);
                    Lesson lesson = transaction.getLesson();
                    toDelete.add(lesson);
                }
            });
            transactionRepository.deleteAll(toDeleteTransactions);
            lessonRepository.deleteAll(toDelete);


            courseRepository.deleteById(id);
            return new ServiceResponse(201,
                    new Message("Course with id: " + id + " has been deleted!"));
        }else{
            return new ServiceResponse(401,
                    new Message("Course cannot be deleted, because it does not exist!"));
        }
    }

    @Override
    @Transactional
    public ServiceResponse editCourse(Long id, CreateCourse dto) {
        if(courseRepository.existsById(id)){
            Course course = courseRepository.findById(id).get();
            course.setDescription(dto.description());
            course.setTitle(dto.title());
            courseRepository.save(course);
            return new ServiceResponse(201,
                    new Message("Course with id: " + id + " has been edited!"));
        }else{
            return new ServiceResponse(401,
                    new Message("Course cannot be edited, because it does not exist!"));
        }
    }

    @Override
    public ServiceResponse getOneCourse(Long id) {
        return courseRepository.findById(id)
                .map(course -> new ServiceResponse(200, course))
                .orElseGet(() -> new ServiceResponse(404, new Message("Course with id: " + id + " does not exist!")));
    }


    @Override
    public ServiceResponse getAllCourses() {
        return new ServiceResponse(200,
                courseRepository.findAll());
    }

    @Override
    public ServiceResponse getCoursesPaginated(Integer page, Integer size) {
        Pageable pg = PageRequest.of(page,size);
        return new ServiceResponse(200,
                courseRepository.findAll(pg));
    }
}
