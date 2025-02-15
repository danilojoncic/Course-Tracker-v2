package com.fullstack.coursetracker.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    private String description;

    @OneToMany(mappedBy = "course",cascade = {CascadeType.ALL},orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Lesson> lessons = new ArrayList<>();


    public Course(String title, String description) {
        this.title = title;
        this.description = description;
    }

}
