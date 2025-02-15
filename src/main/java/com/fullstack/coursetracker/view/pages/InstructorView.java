package com.fullstack.coursetracker.view.pages;

import com.fullstack.coursetracker.dto.CreateInstructor;
import com.fullstack.coursetracker.dto.CreateTag;
import com.fullstack.coursetracker.model.Instructor;
import com.fullstack.coursetracker.model.Tag;
import com.fullstack.coursetracker.service.InstructorService;
import com.fullstack.coursetracker.service.TagsService;
import com.fullstack.coursetracker.view.layouot.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "instructors",layout = MainLayout.class)
public class InstructorView extends VerticalLayout {
    private final InstructorService instructorService;

    public InstructorView(InstructorService instructorService) {
        this.instructorService = instructorService;

        setPadding(true);
        setSpacing(true);

        Grid<Instructor> grid = new Grid<>(Instructor.class);
        grid.setColumns("id", "name");

        grid.addComponentColumn(tag -> createActionsColumn(tag, grid)).setHeader("Actions");

        grid.addItemClickListener(event -> openEditModal(event.getItem(), grid));

        List<Instructor> instructors = (List<Instructor>) instructorService.getAllInstructors().responseObject();
        grid.setItems(instructors);

        Button createBtn = new Button("Create", event -> openCreateModal(grid));
        createBtn.getStyle()
                .set("background-color", "#4CAF50")
                .set("color", "#FFFFFF");

        add(grid, createBtn);

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    private HorizontalLayout createActionsColumn(Instructor instructor, Grid<Instructor> grid) {
        Button editButton = new Button("Edit", e -> openEditModal(instructor, grid));
        editButton.getStyle()
                .set("background-color", "#FFD700")
                .set("color", "#000000");

        Button deleteButton = new Button("Delete", e -> deleteTag(instructor, grid));
        deleteButton.getStyle()
                .set("background-color", "#FF0000")
                .set("color", "#FFFFFF");

        HorizontalLayout actionsLayout = new HorizontalLayout(editButton, deleteButton);
        actionsLayout.setSpacing(true);

        return actionsLayout;
    }

    private void openCreateModal(Grid<Instructor> grid) {
        Dialog dialog = new Dialog();
        TextField titleField = new TextField("Name");

        Button saveButton = new Button("Save", e -> {
            instructorService.createInstructor(new CreateInstructor(titleField.getValue()));
            refresh(grid);
            dialog.close();
            Notification.show("Tag created");
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

    private void openEditModal(Instructor instructor, Grid<Instructor> grid) {
        Dialog dialog = new Dialog();
        TextField titleField = new TextField("Name", instructor.getName());

        Button saveButton = new Button("Save", e -> {
            instructorService.editInstructor(instructor.getId(),new CreateInstructor(titleField.getValue()));
            refresh(grid);
            dialog.close();
            Notification.show("Tag updated");
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

    private void deleteTag(Instructor instructor, Grid<Instructor> grid) {
        instructorService.deleteInstructor(instructor.getId());
        refresh(grid);
        Notification.show("Tag deleted");
    }

    private void refresh(Grid<Instructor> grid) {
        grid.setItems((List<Instructor>) instructorService.getAllInstructors().responseObject());
    }
}
