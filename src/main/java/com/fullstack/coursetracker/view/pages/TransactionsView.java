package com.fullstack.coursetracker.view.pages;

import com.fullstack.coursetracker.dto.CreateTransaction;
import com.fullstack.coursetracker.dto.ServiceResponse;
import com.fullstack.coursetracker.model.*;
import com.fullstack.coursetracker.service.LessonService;
import com.fullstack.coursetracker.service.StudentService;
import com.fullstack.coursetracker.service.TransactionService;
import com.fullstack.coursetracker.view.layouot.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.List;

@Route(value = "transactions", layout = MainLayout.class)
public class TransactionsView extends VerticalLayout {
    private final TransactionService transactionService;
    private final LessonService lessonService;
    private final StudentService studentService;
    private final Grid<Transaction> grid = new Grid<>(Transaction.class);

    public TransactionsView(TransactionService transactionService, LessonService lessonService, StudentService studentService) {
        this.transactionService = transactionService;
        this.lessonService = lessonService;
        this.studentService = studentService;

        setPadding(true);
        setSpacing(true);

        grid.setColumns("id", "cost", "paidAt");
        grid.addColumn(transaction -> transaction.getStudent().getName()).setHeader("Student");
        grid.addColumn(transaction -> transaction.getLesson().getTitle()).setHeader("Lesson");
        grid.addColumn(transaction -> transaction.getFormat().toString()).setHeader("Format");
        grid.addComponentColumn(transaction -> createActionsColumn(transaction)).setHeader("Actions");

        refreshGrid();

        Button createBtn = new Button("Create Transaction", event -> openCreateModal());
        createBtn.getStyle().set("background-color", "#4CAF50").set("color", "#FFFFFF");

        add(grid, createBtn);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    private HorizontalLayout createActionsColumn(Transaction transaction) {
        Button editButton = new Button("Edit", e -> openEditModal(transaction));
        editButton.getStyle().set("background-color", "#FFD700").set("color", "#000000");

        Button deleteButton = new Button("Delete", e -> deleteTransaction(transaction));
        deleteButton.getStyle().set("background-color", "#FF0000").set("color", "#FFFFFF");

        return new HorizontalLayout(editButton, deleteButton);
    }

    private void openCreateModal() {
        Dialog dialog = new Dialog();
        TextField studentNameField = new TextField("Student Name");
        ComboBox<String> lessonTitleField = new ComboBox<>("Lesson Title");
        ComboBox<Format> formatField = new ComboBox<>("Format");
        NumberField costField = new NumberField("Cost");
        DatePicker paidAtField = new DatePicker("Paid At");

        List<String> lessonTitles = ((List<Lesson>) lessonService.getAllLessons().responseObject())
                .stream()
                .map(Lesson::getTitle)
                .toList();
        lessonTitleField.setItems(lessonTitles);

        formatField.setItems(Format.values());
        Button saveButton = new Button("Save", e -> {
            Integer cost = costField.getValue() != null ? costField.getValue().intValue() : 0;
            CreateTransaction createTransaction = new CreateTransaction(
                    cost,
                    paidAtField.getValue(),
                    formatField.getValue(),
                    studentNameField.getValue().equals("")? "Placeholder" : studentNameField.getValue(),
                    lessonTitleField.getValue()
            );
            ServiceResponse response = transactionService.createTransaction(createTransaction);
            if (response.code() == 201) {
                refreshGrid();
                dialog.close();
                Notification.show("Transaction created successfully!");
            } else {
                Notification.show("Error: " + response.responseObject());
            }
        });
        saveButton.getStyle().set("background-color", "#4CAF50").set("color", "#FFFFFF");

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        FormLayout form = new FormLayout(studentNameField, lessonTitleField, formatField, costField, paidAtField);
        dialog.add(form, new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }

    private void openEditModal(Transaction transaction) {
        Dialog dialog = new Dialog();
        TextField studentNameField = new TextField("Student Name", transaction.getStudent().getName());
        ComboBox<String> lessonTitleField = new ComboBox<>("Lesson Title");
        ComboBox<Format> formatField = new ComboBox<>("Format", transaction.getFormat());
        NumberField costField = new NumberField("Cost", String.valueOf(transaction.getCost()));
        DatePicker paidAtField = new DatePicker("Paid At", transaction.getPaidAt());

        List<String> lessonTitles = ((List<Lesson>) lessonService.getAllLessons().responseObject())
                .stream()
                .map(Lesson::getTitle)
                .toList();
        lessonTitleField.setItems(lessonTitles);
        lessonTitleField.setValue(transaction.getLesson().getTitle());

        formatField.setItems(Format.values());
        Button saveButton = new Button("Save", e -> {
            Integer cost = costField.getValue() != null ? costField.getValue().intValue() : 0;
            CreateTransaction createTransaction = new CreateTransaction(
                    cost,
                    paidAtField.getValue(),
                    formatField.getValue(),
                    studentNameField.getValue().equals("")? transaction.getStudent().getName() :  studentNameField.getValue(),
                    lessonTitleField.getValue()
            );
            ServiceResponse response = transactionService.editOneTransaction(transaction.getId(), createTransaction);
            if (response.code() == 200) {
                refreshGrid();
                dialog.close();
                Notification.show("Transaction updated successfully!");
            } else {
                Notification.show("Error: " + response.responseObject());
            }
        });
        saveButton.getStyle().set("background-color", "#4CAF50").set("color", "#FFFFFF");

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        FormLayout form = new FormLayout(studentNameField, lessonTitleField, formatField, costField, paidAtField);
        dialog.add(form, new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }

    private void deleteTransaction(Transaction transaction) {
        ServiceResponse response = transactionService.deleteTransaction(transaction.getId());
        if (response.code() == 201) {
            refreshGrid();
            Notification.show("Transaction deleted successfully!");
        } else {
            Notification.show("Error: " + response.responseObject());
        }
    }

    private void refreshGrid() {
        List<Transaction> transactions = (List<Transaction>) transactionService.getAllTransactions().responseObject();
        grid.setItems(transactions);
    }
}