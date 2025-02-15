package com.fullstack.coursetracker.controller;
import com.fullstack.coursetracker.dto.CreateCourse;
import com.fullstack.coursetracker.dto.ServiceResponse;
import com.fullstack.coursetracker.service.CourseService;
import com.fullstack.coursetracker.service.LessonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
public class CourseController {
    private final CourseService courseService;


    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<?> getAllCourses(){
        ServiceResponse sr = courseService.getAllCourses();
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }
    @GetMapping("/paginated")
    public ResponseEntity<?> getAllCoursesPaginated(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size
            ){
        ServiceResponse sr = courseService.getCoursesPaginated(page,size);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneCourse(@PathVariable Long id){
        ServiceResponse sr = courseService.getOneCourse(id);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOneCourse(@PathVariable Long id){
        ServiceResponse sr = courseService.deleteCourse(id);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> putOneCourse(@PathVariable Long id,
                                          @RequestBody CreateCourse dto){
        ServiceResponse sr = courseService.editCourse(id,dto);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }

    @PostMapping("/create")
    public ResponseEntity<?> postOneCourse(@RequestBody CreateCourse dto){
        ServiceResponse sr = courseService.createCourse(dto);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }




}
