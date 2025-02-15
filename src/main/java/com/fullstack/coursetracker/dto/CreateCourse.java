package com.fullstack.coursetracker.dto;

import lombok.NonNull;

public record CreateCourse(@NonNull String title,
                           @NonNull String description) {
}
