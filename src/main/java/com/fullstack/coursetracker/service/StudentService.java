package com.fullstack.coursetracker.service;

import com.fullstack.coursetracker.dto.CreateStudent;
import com.fullstack.coursetracker.dto.ServiceResponse;
import com.fullstack.coursetracker.dto.Message;
import com.fullstack.coursetracker.model.Student;
import com.fullstack.coursetracker.model.Transaction;
import com.fullstack.coursetracker.repository.LessonRepository;
import com.fullstack.coursetracker.repository.StudentRepository;
import com.fullstack.coursetracker.repository.TransactionRepository;
import com.fullstack.coursetracker.service.abstraction.StudentAbs;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService implements StudentAbs {

    private final StudentRepository studentRepository;
    private final TransactionRepository transactionRepository;

    public StudentService(StudentRepository studentRepository, TransactionRepository transactionRepository) {
        this.studentRepository = studentRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public ServiceResponse createStudent(CreateStudent dto) {
        return studentRepository.findByName(dto.name())
                .map(student -> new ServiceResponse(401, new Message("Student already exists!")))
                .orElseGet(() -> {
                    Student student = new Student(dto.name());
                    studentRepository.save(student);
                    return new ServiceResponse(201, new Message("Student has been successfully created!"));
                });
    }

    @Override
    @Transactional
    public ServiceResponse deleteStudent(Long id) {
        return studentRepository.findById(id)
                .map(student -> {
                    List<Transaction> toBeDeleted = new ArrayList<>();
                    //with custom sql query can be quicker but lets keep it all in the service layer for now
                    List<Transaction> trnas = transactionRepository.findAll();
                    trnas.forEach(transaction -> {
                        if(transaction.getStudent().equals(student)){
                            toBeDeleted.add(transaction);
                        }
                    });

                    transactionRepository.deleteAll(toBeDeleted);
                    studentRepository.deleteById(id);

                    return new ServiceResponse(200, new Message("Student with id: " + id + " has been deleted!"));
                })
                .orElseGet(() -> new ServiceResponse(404, new Message("Student with id: " + id + " does not exist!")));
    }

    @Override
    @Transactional
    public ServiceResponse editStudent(Long id, CreateStudent dto) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.setName(dto.name());
                    studentRepository.save(student);
                    return new ServiceResponse(200, new Message("Student with id: " + id + " has been edited!"));
                })
                .orElseGet(() -> new ServiceResponse(404, new Message("Student with id: " + id + " does not exist!")));
    }

    @Override
    public ServiceResponse getAllStudents() {
        return new ServiceResponse(200, studentRepository.findAll());
    }

    @Override
    public ServiceResponse getOneStudent(Long id) {
        return studentRepository.findById(id)
                .map(student -> new ServiceResponse(200, student))
                .orElseGet(() -> new ServiceResponse(404, new Message("Student with id: " + id + " does not exist!")));
    }

    @Override
    public ServiceResponse getAllStudentsPaginated(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ServiceResponse(200, studentRepository.findAll(pageable));
    }
}
