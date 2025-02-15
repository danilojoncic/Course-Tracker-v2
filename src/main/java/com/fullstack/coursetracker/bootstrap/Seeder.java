package com.fullstack.coursetracker.bootstrap;

import com.fullstack.coursetracker.dto.*;
import com.fullstack.coursetracker.model.Course;
import com.fullstack.coursetracker.model.Format;
import com.fullstack.coursetracker.model.Tag;
import com.fullstack.coursetracker.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class Seeder implements CommandLineRunner {

    private final CourseService courseService;
    private final StudentService studentService;
    private final TagsService tagsService;
    private final InstructorService instructorService;
    private final LessonService lessonService;
    private final TransactionService transactionService;


    public Seeder(CourseService courseService, StudentService studentService, TagsService tagsService, InstructorService instructorService, LessonService lessonService, TransactionService transactionService) {
        this.courseService = courseService;
        this.studentService = studentService;
        this.tagsService = tagsService;
        this.instructorService = instructorService;
        this.lessonService = lessonService;
        this.transactionService = transactionService;
    }

    @Override
    public void run(String... args) throws Exception {

        create();


    }

    private void create() {
        //input as you like

    }
}
