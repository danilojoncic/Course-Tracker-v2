package com.fullstack.coursetracker.dto;

import com.fullstack.coursetracker.model.Format;

import java.time.LocalDate;

public record TransactionCriteriaObject(Integer cost,
                                        LocalDate paidStart,
                                        LocalDate paidEnd,
                                        Format format,
                                        String student,
                                        String lessonTitle,
                                        String tag,
                                        String courseName,
                                        Integer page,
                                        Integer size,
                                        Boolean ascending) {
}
