package com.fullstack.coursetracker.repository;

import com.fullstack.coursetracker.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction,Long> , JpaSpecificationExecutor<Transaction> {
    @Query("SELECT SUM(t.cost) FROM Transaction t WHERE t.student.name = :studentName")
    Integer getTotalSpendByStudent(@Param("studentName") String studentName);

    @Query("""
    SELECT SUM(t.cost / SIZE(l.instructors))
    FROM Transaction t
    JOIN t.lesson l
    JOIN l.instructors i
    WHERE i.name = :instructorName
    """)
    Integer getTotalRevenueByInstructor(@Param("instructorName") String instructorName);

    @Query("SELECT SUM(t.cost) FROM Transaction t")
    Integer getTotalRevenue();
}
