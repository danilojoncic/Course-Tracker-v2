package com.fullstack.coursetracker.service;
import com.fullstack.coursetracker.dto.CreateInstructor;
import com.fullstack.coursetracker.dto.Message;
import com.fullstack.coursetracker.dto.ServiceResponse;
import com.fullstack.coursetracker.model.Instructor;
import com.fullstack.coursetracker.model.Lesson;
import com.fullstack.coursetracker.model.Transaction;
import com.fullstack.coursetracker.repository.InstructorRepository;
import com.fullstack.coursetracker.repository.LessonRepository;
import com.fullstack.coursetracker.repository.TransactionRepository;
import com.fullstack.coursetracker.service.abstraction.InstructorAbs;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class InstructorService implements InstructorAbs {
    private final InstructorRepository instructorRepository;
    private final LessonRepository lessonRepository;
    private final TransactionRepository transactionRepository;

    public InstructorService(InstructorRepository instructorRepository, LessonRepository lessonRepository, TransactionRepository transactionRepository) {
        this.instructorRepository = instructorRepository;
        this.lessonRepository = lessonRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public ServiceResponse createInstructor(CreateInstructor dto) {
        if(instructorRepository.existsByName(dto.name())){
            return new ServiceResponse(401,
                    new Message("Instructor already exists!"));
        }else{
            Instructor instructor = new Instructor(dto.name());
            instructorRepository.save(instructor);
            return new ServiceResponse(200,
                    new Message("Instructor successfully created!"));
        }
    }

    @Override
    @Transactional
    public ServiceResponse deleteInstructor(Long id) {
        if(instructorRepository.existsById(id)){
            List<Transaction> toBeDeleted = new ArrayList<>();
            List<Lesson> toBeDeletedLessons = new ArrayList<>();
            List<Transaction> trns = transactionRepository.findAll();
            trns.forEach(transaction -> {
                Instructor instructor = instructorRepository.findById(id).get();
                if(transaction.getLesson().getInstructors().contains(instructor)){
                    toBeDeleted.add(transaction);
                    toBeDeletedLessons.add(transaction.getLesson());
                }
            });

            transactionRepository.deleteAll(toBeDeleted);
            lessonRepository.deleteAll(toBeDeletedLessons);
            instructorRepository.deleteById(id);

            return new ServiceResponse(201,
                    new Message("Instructor with id: " + id + " has been deleted!"));
        }else{
            return new ServiceResponse(401,
                    new Message("Instructor cannot be deleted, because he/she does not exist!"));
        }
    }

    @Override
    @Transactional
    public ServiceResponse editInstructor(Long id, CreateInstructor dto) {
        return instructorRepository.findById(id)
                .map(instructor -> {
                    instructor.setName(dto.name());
                    instructorRepository.save(instructor);
                    return new ServiceResponse(201, "Instructor with id: " + id + " has been edited!");
                })
                .orElseGet(() -> new ServiceResponse(404, "Instructor with id: " + id + " does not exist!"));
    }

    @Override
    public ServiceResponse getOneInstructor(Long id) {
        return instructorRepository.findById(id)
                .map(instructor -> new ServiceResponse(200,instructor))
                .orElseGet(()-> new ServiceResponse(404, new Message("Instructor with id: " + id + " does not exist!")));
    }

    @Override
    public ServiceResponse getAllInstructors() {
        return new ServiceResponse(200,
                instructorRepository.findAll());
    }

    @Override
    public ServiceResponse getAllInstructorsPaginated(Integer page, Integer size) {
        Pageable pg = PageRequest.of(page,size);
        return new ServiceResponse(200,
                instructorRepository.findAll(pg));
    }
}
