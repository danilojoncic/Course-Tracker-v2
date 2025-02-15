package com.fullstack.coursetracker.service.abstraction;

import com.fullstack.coursetracker.dto.CreateTransaction;
import com.fullstack.coursetracker.dto.CriteriaObject;
import com.fullstack.coursetracker.dto.ServiceResponse;
import com.fullstack.coursetracker.dto.TransactionCriteriaObject;

public interface TransactionAbs {
    ServiceResponse getOneTransaction(Long id);
    ServiceResponse getAllTransactions();
    ServiceResponse getAllTransactionsPaginated(Integer page, Integer size, Boolean ascendingOrder);
    ServiceResponse createTransaction(CreateTransaction dto);
    ServiceResponse editOneTransaction(Long id, CreateTransaction dto);
    ServiceResponse deleteTransaction(Long id);
    ServiceResponse getTotalSpendByStudent(String studentName);
    ServiceResponse getTotalRevenueByInstructor(String instructor);
    ServiceResponse getTotalRevenue();
    ServiceResponse getAllTransactionsBasedOnCriteria(TransactionCriteriaObject dto);
    ServiceResponse getRevenueBasedOnCriteria(TransactionCriteriaObject dto);

}
