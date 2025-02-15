package com.fullstack.coursetracker.view.pages;

import com.fullstack.coursetracker.dto.CreateStudent;
import com.fullstack.coursetracker.dto.CreateTag;
import com.fullstack.coursetracker.model.Student;
import com.fullstack.coursetracker.model.Tag;
import com.fullstack.coursetracker.service.StudentService;
import com.fullstack.coursetracker.service.TagsService;
import com.fullstack.coursetracker.view.layouot.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "students",layout = MainLayout.class)
public class StudentView extends VerticalLayout {
    private final StudentService studentService;
    public StudentView(StudentService studentService) {
        this.studentService = studentService;

        setPadding(true);
        setSpacing(true);

        Grid<Student> grid = new Grid<>(Student.class);
        grid.setColumns("id", "name");

        grid.addComponentColumn(tag -> createActionsColumn(tag, grid)).setHeader("Actions");

        grid.addItemClickListener(event -> openEditModal(event.getItem(), grid));

        List<Student> students = (List<Student>) studentService.getAllStudents().responseObject();
        grid.setItems(students);

        Button createBtn = new Button("Create", event -> openCreateModal(grid));
        createBtn.getStyle()
                .set("background-color", "#4CAF50")
                .set("color", "#FFFFFF");

        add(grid, createBtn);

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    private HorizontalLayout createActionsColumn(Student student, Grid<Student> grid) {
        Button editButton = new Button("Edit", e -> openEditModal(student, grid));
        editButton.getStyle()
                .set("background-color", "#FFD700")
                .set("color", "#000000");

        Button deleteButton = new Button("Delete", e -> deleteTag(student, grid));
        deleteButton.getStyle()
                .set("background-color", "#FF0000")
                .set("color", "#FFFFFF");

        HorizontalLayout actionsLayout = new HorizontalLayout(editButton, deleteButton);
        actionsLayout.setSpacing(true);

        return actionsLayout;
    }

    private void openCreateModal(Grid<Student> grid) {
        Dialog dialog = new Dialog();
        TextField titleField = new TextField("Name");

        Button saveButton = new Button("Save", e -> {
            studentService.createStudent(new CreateStudent(titleField.getValue()));
            refresh(grid);
            dialog.close();
            Notification.show("Student created");
        });
        saveButton.getStyle()
                .set("background-color", "#4CAF50")
                .set("color", "#FFFFFF");

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        FormLayout form = new FormLayout(titleField);
        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);

        dialog.add(form, buttons);
        dialog.open();
    }

    private void openEditModal(Student student, Grid<Student> grid) {
        Dialog dialog = new Dialog();
        TextField titleField = new TextField("Name", student.getName());

        Button saveButton = new Button("Save", e -> {
            studentService.editStudent(student.getId(), new CreateStudent(titleField.getValue()));
            refresh(grid);
            dialog.close();
            Notification.show("Student updated");
        });
        saveButton.getStyle()
                .set("background-color", "#4CAF50")
                .set("color", "#FFFFFF");

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        FormLayout form = new FormLayout(titleField);
        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);

        dialog.add(form, buttons);
        dialog.open();
    }

    private void deleteTag(Student student, Grid<Student> grid) {
        studentService.deleteStudent(student.getId());
        refresh(grid);
        Notification.show("Tag deleted");
    }

    private void refresh(Grid<Student> grid) {
        grid.setItems((List<Student>) studentService.getAllStudents().responseObject());
    }
}
