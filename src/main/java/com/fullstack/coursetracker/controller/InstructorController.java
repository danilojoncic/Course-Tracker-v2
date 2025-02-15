package com.fullstack.coursetracker.controller;

import com.fullstack.coursetracker.dto.CreateInstructor;
import com.fullstack.coursetracker.dto.ServiceResponse;
import com.fullstack.coursetracker.service.InstructorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/instructor")
public class InstructorController {
    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping
    public ResponseEntity<?> getAllInstructors(){
        ServiceResponse sr = instructorService.getAllInstructors();
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }

    @GetMapping("/paginated")
    public ResponseEntity<?> getAllInstructorsPaginated(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "2") Integer size){
        ServiceResponse sr = instructorService.getAllInstructorsPaginated(page,size);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneInstructor(@PathVariable Long id){
        ServiceResponse sr = instructorService.getOneInstructor(id);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOneInstructor(@PathVariable Long id){
        ServiceResponse sr = instructorService.deleteInstructor(id);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> putOneInstructor(@PathVariable Long id, @RequestBody CreateInstructor dto){
        ServiceResponse sr = instructorService.editInstructor(id,dto);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }

    @PostMapping("/create")
    public ResponseEntity<?> postOneInstructor(@RequestBody CreateInstructor dto){
        ServiceResponse sr = instructorService.createInstructor(dto);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }
}
