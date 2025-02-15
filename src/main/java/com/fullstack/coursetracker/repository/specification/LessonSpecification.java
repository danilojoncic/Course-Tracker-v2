package com.fullstack.coursetracker.repository.specification;

import com.fullstack.coursetracker.dto.CriteriaObject;
import com.fullstack.coursetracker.model.Lesson;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

//some new things here
public class LessonSpecification {
    public static Specification<Lesson> getLessonsPerCriteria(CriteriaObject criteriaObject){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteriaObject.titleOfCourse() != null) {
                predicates.add(criteriaBuilder.equal(root.get("title"), criteriaObject.titleOfCourse()));
            }

            if (criteriaObject.instructorNames() != null && !criteriaObject.instructorNames().isEmpty()) {
                predicates.add(root.join("instructors").get("name").in(criteriaObject.instructorNames()));
            }

            if (criteriaObject.tags() != null && !criteriaObject.tags().isEmpty()) {
                predicates.add(root.join("tags").get("title").in(criteriaObject.tags()));
            }

            if (criteriaObject.dateFrom() != null && criteriaObject.dateTo() != null) {
                predicates.add(criteriaBuilder.between(root.get("createdAt"), criteriaObject.dateFrom(), criteriaObject.dateTo()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
