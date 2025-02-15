package com.fullstack.coursetracker.service;

import com.fullstack.coursetracker.model.Transaction;
import com.fullstack.coursetracker.service.abstraction.DownloadAbs;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.yaml.snakeyaml.tokens.Token.ID.Tag;

@Service
public class DownloadService implements DownloadAbs {
    private final TransactionService transactionService;

    public DownloadService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Transactional
    @Override
    public byte[] downlaodCsv() {
        List<Transaction> transactions = (List<Transaction>) transactionService.getAllTransactions().responseObject();

        String[] headers = {"ID","Paid At", "Cost", "Format", "Student", "Lesson", "Course", "Tags", "Instructors"};

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(byteArrayOutputStream);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers))) {

            for (Transaction transaction : transactions) {
                StringBuilder builderTags = new StringBuilder();
                transaction.getLesson().getTags().forEach(tag -> builderTags.append(tag.getTitle() + ", "));
                StringBuilder builderInstructor = new StringBuilder();
                transaction.getLesson().getInstructors().forEach(
                        instructor -> builderInstructor.append(instructor.getName() + ", ")
                );

                csvPrinter.printRecord(
                        transaction.getId(),
                        transaction.getPaidAt(),
                        transaction.getCost(),
                        transaction.getFormat().toString(),
                        transaction.getStudent().getName(),
                        transaction.getLesson().getTitle(),
                        transaction.getLesson().getCourse().getTitle(),
                        builderTags.toString(),
                        builderInstructor.toString()
                );
            }

            writer.flush();
            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}