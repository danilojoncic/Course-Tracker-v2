package com.fullstack.coursetracker.controller;
import com.fullstack.coursetracker.dto.CreateStudent;
import com.fullstack.coursetracker.dto.ServiceResponse;
import com.fullstack.coursetracker.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<?> getAllStudents(){
        ServiceResponse sr = studentService.getAllStudents();
        return ResponseEntity.status(sr.code()).body(sr.responseObject());

    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOneStudent(@PathVariable Long id){
        ServiceResponse sr = studentService.getOneStudent(id);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }
    @GetMapping("/paginated")
    public ResponseEntity<?> getAllStudentsPaginated(@RequestParam(defaultValue = "0") Integer page,
                                                     @RequestParam(defaultValue = "10") Integer size){
        ServiceResponse sr = studentService.getAllStudentsPaginated(page,size);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());

    }
    @PostMapping("/create")
    public ResponseEntity<?> postOneStudent(@RequestBody CreateStudent dto){
        ServiceResponse sr = studentService.createStudent(dto);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> putOneStudnet(@PathVariable Long id, @RequestBody CreateStudent dto){
        ServiceResponse sr = studentService.editStudent(id,dto);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOneStudnet(@PathVariable Long id){
        ServiceResponse sr = studentService.deleteStudent(id);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }
}
