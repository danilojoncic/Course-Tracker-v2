package com.fullstack.coursetracker.controller;

import com.fullstack.coursetracker.dto.CreateLesson;
import com.fullstack.coursetracker.dto.CriteriaObject;
import com.fullstack.coursetracker.dto.ServiceResponse;
import com.fullstack.coursetracker.service.LessonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lesson")
public class LessonController {
    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping("/format")
    public ResponseEntity<?> getAllFormats(){
        return ResponseEntity.status(200).body(List.of("ONLINE","LIVE","RECORDING"));
    }


    @GetMapping
    public ResponseEntity<?> getAllLessons(){
        ServiceResponse sr = lessonService.getAllLessons();
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOneLesson(@PathVariable Long id){
        ServiceResponse sr = lessonService.getOneLesson(id);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }
    @GetMapping("/paginated")
    public ResponseEntity<?> getAllLessonsPaginated(@RequestParam(defaultValue = "0") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer size){
        ServiceResponse sr = lessonService.getAllLessonsPaginated(page,size);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }
    @GetMapping("/criteria")
    public ResponseEntity<?> getAllLessonsBasedOnCriteria(@RequestParam(required = false) String titleOfCourse,
                                                          @RequestParam(required = false) List<String> instructorNames,
                                                          @RequestParam(required = false) List<String> tags,
                                                          @RequestParam(required = false) LocalDate dateFrom,
                                                          @RequestParam(required = false) LocalDate dateTo,
                                                          @RequestParam(defaultValue = "true") Boolean ascendingOrder,
                                                          @RequestParam(defaultValue = "0") Integer page,
                                                          @RequestParam(defaultValue = "20") Integer size){
        ServiceResponse sr = lessonService.getLessonsPerCriteria(
                new CriteriaObject(
                        titleOfCourse,
                        instructorNames,
                        tags,
                        dateFrom,
                        dateTo,
                        page,
                        size,
                        ascendingOrder));
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }
    @PostMapping("/create")
    public ResponseEntity<?> postLesson(@RequestBody CreateLesson dto){
        ServiceResponse sr = lessonService.createLesson(dto);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> putLesson(@PathVariable Long id, @RequestBody CreateLesson dto){
        ServiceResponse sr = lessonService.editOneLesson(id,dto);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLesson(@PathVariable Long id){
        ServiceResponse sr = lessonService.deleteOneLesson(id);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }



}
