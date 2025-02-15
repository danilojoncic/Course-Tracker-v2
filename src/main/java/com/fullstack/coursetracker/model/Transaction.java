package com.fullstack.coursetracker.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int cost;
    private LocalDate paidAt;

    @Enumerated(EnumType.STRING)
    private Format format;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonBackReference
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    public Transaction(int cost, LocalDate paidAt, Format format, Student student, Lesson lesson) {
        this.cost = cost;
        this.paidAt = paidAt;
        this.format = format;
        this.student = student;
        this.lesson = lesson;
    }
}
