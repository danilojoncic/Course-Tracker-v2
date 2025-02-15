package com.fullstack.coursetracker.controller;

import com.fullstack.coursetracker.dto.CreateTransaction;
import com.fullstack.coursetracker.dto.ServiceResponse;
import com.fullstack.coursetracker.dto.TransactionCriteriaObject;
import com.fullstack.coursetracker.model.Format;
import com.fullstack.coursetracker.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> postTransaction(@RequestBody CreateTransaction dto){
        ServiceResponse sr = transactionService.createTransaction(dto);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }
    @PutMapping("edit/{id}")
    public ResponseEntity<?> putTransaction(@PathVariable Long id,@RequestBody CreateTransaction dto){
        ServiceResponse sr = transactionService.editOneTransaction(id,dto);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delteTransaction(@PathVariable Long id){
        ServiceResponse sr = transactionService.deleteTransaction(id);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }
    @GetMapping
    public ResponseEntity<?> getAllTransactions(){
        ServiceResponse sr = transactionService.getAllTransactions();
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOneTransaction(@PathVariable Long id){
        ServiceResponse sr = transactionService.getOneTransaction(id);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }
    @GetMapping("/paginated")
    public ResponseEntity<?> getAllTransactionsPaginated(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "true") Boolean ascending){
        ServiceResponse sr = transactionService.getAllTransactionsPaginated(page,size,ascending);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }
    @GetMapping("/student/{name}")
    public ResponseEntity<?> getTotalSpentByStudent(@PathVariable String name){
        ServiceResponse sr = transactionService.getTotalSpendByStudent(name);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }
    @GetMapping("/instructor/{name}")
    public ResponseEntity<?> getTotalRevenueByInstructor(@PathVariable String name){
        ServiceResponse sr = transactionService.getTotalRevenueByInstructor(name);
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }
    @GetMapping("/revenue/total")
    public ResponseEntity<?> getTotalRevenue(){
        ServiceResponse sr = transactionService.getTotalRevenue();
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }
    @GetMapping("/criteria")
    public ResponseEntity<?> getAllTransactionsBasedOnCriteria(
            @RequestParam(required = false) Integer cost,
            @RequestParam(required = false) LocalDate paidStart,
            @RequestParam(required = false) LocalDate paidEnd,
            @RequestParam(required = false) String format,
            @RequestParam(required = false) String student,
            @RequestParam(required = false) String lessonTitle,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String courseName,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "true") Boolean ascending
            ){
        ServiceResponse sr = transactionService.getAllTransactionsBasedOnCriteria(
                new TransactionCriteriaObject(
                        cost,
                        paidStart,
                        paidEnd,
                        Format.valueOf(format),
                        student,
                        lessonTitle,
                        tag,
                        courseName,
                        page,
                        size,
                        ascending
                )
        );
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }
    @GetMapping("/revenue/criteria")
    public ResponseEntity<?> getAllRevenueOnCriteria(
            @RequestParam(required = false) Integer cost,
            @RequestParam(required = false) LocalDate paidStart,
            @RequestParam(required = false) LocalDate paidEnd,
            @RequestParam(required = false) String format,
            @RequestParam(required = false) String student,
            @RequestParam(required = false) String lessonTitle,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String courseName,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "true") Boolean ascending
    ){
        ServiceResponse sr = transactionService.getRevenueBasedOnCriteria(
                new TransactionCriteriaObject(
                        cost,
                        paidStart,
                        paidEnd,
                        Format.valueOf(format),
                        student,
                        lessonTitle,
                        tag,
                        courseName,
                        page,
                        size,
                        ascending
                )
        );
        return ResponseEntity.status(sr.code()).body(sr.responseObject());
    }



}
