package com.fullstack.coursetracker.dto;

import java.time.LocalDate;
import java.util.List;

public record CriteriaObject(String titleOfCourse,
                             List<String> instructorNames,
                             List<String> tags,
                             LocalDate dateFrom,
                             LocalDate dateTo,
                             Integer page,
                             Integer size,
                             boolean order) {
}
