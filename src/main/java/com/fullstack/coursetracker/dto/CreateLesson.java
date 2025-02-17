package com.fullstack.coursetracker.dto;

import java.time.LocalDate;
import java.util.List;

public record CreateLesson(String title,
                           String courseName,
                           String link,
                           List<String> tags,
                           List<String> instructors,
                           LocalDate createdAt) {
}
