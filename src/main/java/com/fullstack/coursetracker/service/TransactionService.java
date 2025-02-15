package com.fullstack.coursetracker.service;

import com.fullstack.coursetracker.dto.CreateTransaction;
import com.fullstack.coursetracker.dto.Message;
import com.fullstack.coursetracker.dto.ServiceResponse;
import com.fullstack.coursetracker.dto.TransactionCriteriaObject;
import com.fullstack.coursetracker.model.Format;
import com.fullstack.coursetracker.model.Lesson;
import com.fullstack.coursetracker.model.Student;
import com.fullstack.coursetracker.model.Transaction;
import com.fullstack.coursetracker.repository.*;
import com.fullstack.coursetracker.repository.specification.TransactionSpecification;
import com.fullstack.coursetracker.service.abstraction.TransactionAbs;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService implements TransactionAbs {
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;

    private final TransactionRepository transactionRepository;

    private final EntityManager entityManager;

    public TransactionService(CourseRepository courseRepository, InstructorRepository instructorRepository, LessonRepository lessonRepository, StudentRepository studentRepository, TransactionRepository transactionRepository, EntityManager entityManager) {
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
        this.lessonRepository = lessonRepository;
        this.studentRepository = studentRepository;
        this.transactionRepository = transactionRepository;
        this.entityManager = entityManager;
    }

    @Override
    public ServiceResponse getOneTransaction(Long id) {
        return transactionRepository.findById(id)
                .map(transaction -> new ServiceResponse(200,transaction))
                .orElseGet(() -> new ServiceResponse(404,new Message("Transaction does not exist!")));
    }

    @Override
    public ServiceResponse getAllTransactions() {
        return new ServiceResponse(200,transactionRepository.findAll());
    }

    @Override
    public ServiceResponse getAllTransactionsPaginated(Integer page, Integer size, Boolean ascendingOrder) {
        Pageable pg = PageRequest.of(page,size,ascendingOrder? Sort.Direction.ASC : Sort.Direction.DESC,"paidAt");
        return new ServiceResponse(200,transactionRepository.findAll(pg));
    }

    @Override
    @Transactional
    public ServiceResponse createTransaction(CreateTransaction dto) {
        Student student = studentRepository.findByName(dto.studentName())
                .orElseGet(() -> {
                    Student newStudent = new Student(dto.studentName());
                    return studentRepository.save(newStudent);
                });

        Lesson lesson = lessonRepository.findByTitle(dto.lessonTitle())
                .orElse(null);

        if (lesson == null) {
            return new ServiceResponse(401, new Message("Lesson does not exist!"));
        }

        Transaction transaction = new Transaction();
        transaction.setStudent(student);
        transaction.setFormat(dto.format());
        transaction.setLesson(lesson);
        transaction.setCost(dto.cost());
        transaction.setPaidAt(dto.paidAt());

        transactionRepository.save(transaction);
        return new ServiceResponse(201, new Message("Transaction created successfully!"));
    }


    @Override
    @Transactional
    public ServiceResponse editOneTransaction(Long id, CreateTransaction dto) {
        Transaction transaction = transactionRepository.findById(id)
                .orElse(null);

        if (transaction == null) {
            return new ServiceResponse(404, new Message("Transaction does not exist!"));
        }

        Student student = studentRepository.findByName(dto.studentName())
                .orElseGet(() -> {
                    Student newStudent = new Student(dto.studentName());
                    return studentRepository.save(newStudent);
                });

        Lesson lesson = lessonRepository.findByTitle(dto.lessonTitle()).orElse(null);
        if (lesson == null) {
            return new ServiceResponse(401, new Message("Lesson does not exist!"));
        }

        transaction.setStudent(student);
        transaction.setFormat(dto.format());
        transaction.setLesson(lesson);
        transaction.setCost(dto.cost());
        transaction.setPaidAt(dto.paidAt());
        transactionRepository.save(transaction);
        return new ServiceResponse(200, new Message("Transaction updated successfully!"));
    }


    @Override
    @Transactional
    public ServiceResponse deleteTransaction(Long id) {
        return transactionRepository.findById(id)
                .map(transaction -> {
                    transactionRepository.deleteById(id);
                    return new ServiceResponse(201,new Message("Transaction has been deleted!"));
                })
                .orElseGet(() -> new ServiceResponse(404,new Message("Transaction does not exist!")));
    }

    @Override
    public ServiceResponse getTotalSpendByStudent(String studentName) {
        return studentRepository.findByName(studentName)
                .map(student -> {
                    Integer totalSpent = transactionRepository.getTotalSpendByStudent(studentName);
                    return new ServiceResponse(200, totalSpent != null ? totalSpent : 0);
                })
                .orElseGet(() -> new ServiceResponse(404, new Message("Student does not exist!")));
    }

    @Override
    public ServiceResponse getTotalRevenueByInstructor(String instructor) {
        return instructorRepository.findByName(instructor)
                .map(instructor1 -> {
                    Integer revenueByInstructor = transactionRepository.getTotalRevenueByInstructor(instructor);
                    return new ServiceResponse(200,revenueByInstructor != null ? revenueByInstructor : 0);
                })
                .orElseGet(() -> new ServiceResponse(404,new Message("Instructor does not exist!")));
    }

    @Override
    public ServiceResponse getTotalRevenue() {
        return new ServiceResponse(200,transactionRepository.getTotalRevenue());
    }

    //this needs to be tested! ASAP
    @Override
    @Transactional(readOnly = true)
    public ServiceResponse getAllTransactionsBasedOnCriteria(TransactionCriteriaObject criteria) {
        Specification<Transaction> specs = TransactionSpecification.getTransactionsBasedOnCriteria(criteria);
        Pageable pg = PageRequest.of(criteria.page(),criteria.size(),criteria.ascending()? Sort.by("paidAt").ascending() : Sort.by("paidAt").descending());
        return new ServiceResponse(200,transactionRepository.findAll(specs,pg));
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResponse getRevenueBasedOnCriteria(TransactionCriteriaObject criteria) {
        Specification<Transaction> specs = TransactionSpecification.getTransactionsBasedOnCriteria(criteria);

        //things are a bit hazy for the following few lines
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
        Root<Transaction> root = query.from(Transaction.class);

        //somehow works, need to study the docs and figure out why this works (DeepSeek suggestion)
        query.select(cb.sum(root.get("cost"))).where(specs.toPredicate(root,query,cb));

        Integer totalRevenue = entityManager.createQuery(query).getSingleResult();


        return new ServiceResponse(200,totalRevenue);
    }
}
