package com.fullstack.coursetracker.repository.specification;

import com.fullstack.coursetracker.dto.TransactionCriteriaObject;
import com.fullstack.coursetracker.model.Transaction;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification {
    public static Specification<Transaction> getTransactionsBasedOnCriteria(TransactionCriteriaObject criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.cost() != null) {
                predicates.add(criteriaBuilder.equal(root.get("cost"), criteria.cost()));
            }

            if (criteria.paidStart() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("paidAt"), criteria.paidStart()));
            }
            if (criteria.paidEnd() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("paidAt"), criteria.paidEnd()));
            }

            if (criteria.student() != null && !criteria.student().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("student").get("name")),
                        "%" + criteria.student().toLowerCase() + "%"
                ));
            }

            if (criteria.lessonTitle() != null && !criteria.lessonTitle().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("lesson").get("title")),
                        "%" + criteria.lessonTitle().toLowerCase() + "%"
                ));
            }

            if (criteria.courseName() != null && !criteria.courseName().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("lesson").get("course").get("name")),
                        "%" + criteria.courseName().toLowerCase() + "%"
                ));
            }

            if (criteria.tag() != null && !criteria.tag().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.join("lesson").join("tags").get("name")),
                        "%" + criteria.tag().toLowerCase() + "%"
                ));
            }

            if (criteria.format() != null) {
                predicates.add(criteriaBuilder.equal(root.get("lesson").get("format"), criteria.format()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
