package com.fullstack.coursetracker.service;
import com.fullstack.coursetracker.dto.CreateLesson;
import com.fullstack.coursetracker.dto.CriteriaObject;
import com.fullstack.coursetracker.dto.Message;
import com.fullstack.coursetracker.dto.ServiceResponse;
import com.fullstack.coursetracker.model.*;
import com.fullstack.coursetracker.repository.*;
import com.fullstack.coursetracker.repository.specification.LessonSpecification;
import com.fullstack.coursetracker.service.abstraction.LessonAbs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LessonService implements LessonAbs {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final TagRepository tagRepository;
    private final TransactionRepository transactionRepository;

    public LessonService(LessonRepository lessonRepository, CourseRepository courseRepository, InstructorRepository instructorRepository, TagRepository tagRepository, TransactionRepository transactionRepository) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
        this.tagRepository = tagRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public ServiceResponse createLesson(CreateLesson dto) {
        if (lessonRepository.findByTitle(dto.title()).isPresent()) {
            return new ServiceResponse(401, new Message("Lesson already exists!"));
        }

        Course course = courseRepository.findByTitle(dto.courseName())
                .orElseGet(() -> {
                    Course newCourse = new Course(dto.courseName(), "Placeholder");
                    return courseRepository.save(newCourse);
                });

        Set<Instructor> instructors = dto.instructors().stream()
                .map(instructorName -> instructorRepository.findByName(instructorName)
                        .orElseGet(() -> {
                            Instructor newInstructor = new Instructor(instructorName);
                            return instructorRepository.save(newInstructor);
                        }))
                .collect(Collectors.toSet());

        Set<Tag> tags = dto.tags().stream()
                .map(tagTitle -> tagRepository.findByTitle(tagTitle)
                        .orElseGet(() -> {
                            Tag newTag = new Tag(tagTitle);
                            return tagRepository.save(newTag);
                        }))
                .collect(Collectors.toSet());

        Lesson lesson = new Lesson(dto.title(), dto.createdAt(),dto.link(), course, instructors, tags);
        lessonRepository.save(lesson);

        return new ServiceResponse(201, new Message("Lesson has been successfully created!"));
    }



    @Override
    public ServiceResponse getOneLesson(Long id) {
        return lessonRepository.findById(id)
                .map(lesson -> new ServiceResponse(200,lesson))
                .orElseGet(() -> {
                    return new ServiceResponse(404,new Message("Lesson does not exist!"));
                });
    }
    //because of vaadin
    @Transactional
    @Override
    public ServiceResponse getAllLessons() {
        return new ServiceResponse(200,lessonRepository.findAll());
    }

    @Override
    public ServiceResponse getAllLessonsPaginated(Integer page, Integer size) {
        Pageable pg = PageRequest.of(page,size);
        return new ServiceResponse(200,lessonRepository.findAll(pg));
    }

    @Override
    @Transactional
    public ServiceResponse editOneLesson(Long id, CreateLesson dto) {
        if (!lessonRepository.findByTitle(dto.title()).isEmpty()) {
            return new ServiceResponse(401, new Message("Lesson does not exist!"));
        }
        if(!lessonRepository.existsById(id))return new ServiceResponse(401, new Message("Lesson does not exist!"));
        Lesson lesson = lessonRepository.findById(id).get();
        //pretty much the same code as creation but instead of creating a new object instance we just use setters
        Course course = courseRepository.findByTitle(dto.courseName())
                .orElseGet(() -> {
                    Course newCourse = new Course(dto.courseName(), "Placeholder");
                    return courseRepository.save(newCourse);
                });

        Set<Instructor> instructors = dto.instructors().stream()
                .map(instructorName -> instructorRepository.findByName(instructorName)
                        .orElseGet(() -> {
                            Instructor newInstructor = new Instructor(instructorName);
                            return instructorRepository.save(newInstructor);
                        }))
                .collect(Collectors.toSet());

        Set<Tag> tags = dto.tags().stream()
                .map(tagTitle -> tagRepository.findByTitle(tagTitle)
                        .orElseGet(() -> {
                            Tag newTag = new Tag(tagTitle);
                            return tagRepository.save(newTag);
                        }))
                .collect(Collectors.toSet());

        lesson.setCourse(course);
        lesson.setLink(dto.link());
        lesson.setTitle(dto.title());
        lesson.setInstructors(instructors);
        lesson.setCreatedAt(dto.createdAt());
        lesson.setTags(tags);
        lessonRepository.save(lesson);

        return new ServiceResponse(201, new Message("Lesson has been successfully edited!"));
    }

    @Override
    @Transactional
    public ServiceResponse deleteOneLesson(Long id) {
        return lessonRepository.findById(id)
                .map(lesson-> {
                    List<Transaction> toBeDeleted = new ArrayList<>();
                    List<Transaction> trns = transactionRepository.findAll();
                    trns.forEach(transaction -> {
                        if(transaction.getLesson().equals(lesson)){
                            toBeDeleted.add(transaction);
                        }
                    });

                    transactionRepository.deleteAll(toBeDeleted);
                    lessonRepository.deleteById(id);

                    return new ServiceResponse(201,new Message("Lesson with id: " + id + " has been deleted!"));
                })
                .orElseGet(() -> new ServiceResponse(404,new Message("Lesson does not exist!")));
    }


    @Override
    @Transactional(readOnly = true)
    public ServiceResponse getLessonsPerCriteria(CriteriaObject criteria) {
        Pageable pg = PageRequest.of(criteria.page(), criteria.size(),
                criteria.order()? Sort.Direction.ASC : Sort.Direction.DESC,"title");
        Page<Lesson> lessons = lessonRepository.findAll(LessonSpecification.getLessonsPerCriteria(criteria),pg);
        return new ServiceResponse(200,lessons);
    }
}
