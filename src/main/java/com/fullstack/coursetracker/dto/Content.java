package com.fullstack.coursetracker.dto;

import java.time.LocalDate;
import java.util.List;

public record Content(LocalDate paidAt,
                      String lessonName,
                      String courseName,
                      String studentName,
                      Integer amountPaid,
                      List<String> instructorNames,
                      List<String> tags) {
}
