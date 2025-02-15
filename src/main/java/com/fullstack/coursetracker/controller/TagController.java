package com.fullstack.coursetracker.controller;

import com.fullstack.coursetracker.dto.CreateTag;
import com.fullstack.coursetracker.dto.ServiceResponse;
import com.fullstack.coursetracker.service.TagsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tag")
public class TagController {
    private final TagsService tagsService;

    public TagController(TagsService tagsService) {
        this.tagsService = tagsService;
    }

    @GetMapping
    public ResponseEntity<?> getAllTags(){
        ServiceResponse sr = tagsService.getAllTags();
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneTag(@PathVariable Long id){
        ServiceResponse sr = tagsService.getOneTag(id);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }

    @GetMapping("/paginated")
    public ResponseEntity<?> getAllTagsPaginated(@RequestParam(defaultValue = "0") Integer page,
                                                 @RequestParam(defaultValue = "5") Integer size){
        ServiceResponse sr = tagsService.getAllTagsPaginated(page,size);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOneTag(@PathVariable Long id){
        ServiceResponse sr = tagsService.deleteTag(id);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> putOneTag(@PathVariable Long id, @RequestBody CreateTag dto){
        ServiceResponse sr = tagsService.editTag(id,dto);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }

    @PostMapping("/create")
    public ResponseEntity<?> postOneTag(@RequestBody CreateTag dto){
        ServiceResponse sr = tagsService.createTag(dto);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }
}
