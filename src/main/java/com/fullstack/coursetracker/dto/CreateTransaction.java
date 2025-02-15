package com.fullstack.coursetracker.dto;

import com.fullstack.coursetracker.model.Format;
import com.fullstack.coursetracker.model.Lesson;

import java.time.LocalDate;

public record CreateTransaction(Integer cost,
                                LocalDate paidAt,
                                Format format,
                                String studentName,
                                String lessonTitle) {
}
