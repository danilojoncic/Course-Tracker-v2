package com.fullstack.coursetracker.view.pages;

import com.fullstack.coursetracker.dto.CreateCourse;
import com.fullstack.coursetracker.dto.CreateTag;
import com.fullstack.coursetracker.model.Course;
import com.fullstack.coursetracker.model.Tag;
import com.fullstack.coursetracker.service.CourseService;
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

@Route(value = "courses",layout = MainLayout.class)
public class CourseView extends VerticalLayout {
    private final CourseService courseService;

    public CourseView(CourseService courseService) {
        this.courseService = courseService;

        setPadding(true);
        setSpacing(true);

        Grid<Course> grid = new Grid<>(Course.class);
        grid.setColumns("id", "title","description");

        grid.addComponentColumn(course -> createActionsColumn(course, grid)).setHeader("Actions");

        grid.addItemClickListener(event -> openEditModal(event.getItem(), grid));

        List<Course> courses = (List<Course>) courseService.getAllCourses().responseObject();
        grid.setItems(courses);

        Button createBtn = new Button("Create", event -> openCreateModal(grid));
        createBtn.getStyle()
                .set("background-color", "#4CAF50")
                .set("color", "#FFFFFF");

        add(grid, createBtn);

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    private HorizontalLayout createActionsColumn(Course tag, Grid<Course> grid) {
        Button editButton = new Button("Edit", e -> openEditModal(tag, grid));
        editButton.getStyle()
                .set("background-color", "#FFD700")
                .set("color", "#000000");

        Button deleteButton = new Button("Delete", e -> deleteTag(tag, grid));
        deleteButton.getStyle()
                .set("background-color", "#FF0000")
                .set("color", "#FFFFFF");

        HorizontalLayout actionsLayout = new HorizontalLayout(editButton, deleteButton);
        actionsLayout.setSpacing(true);

        return actionsLayout;
    }

    private void openCreateModal(Grid<Course> grid) {
        Dialog dialog = new Dialog();
        TextField titleField = new TextField("Title");
        TextField descriptionField = new TextField("Description");

        Button saveButton = new Button("Save", e -> {
            courseService.createCourse(new CreateCourse(titleField.getValue(),descriptionField.getValue()));
            refresh(grid);
            dialog.close();
            Notification.show("Course created");
        });
        saveButton.getStyle()
                .set("background-color", "#4CAF50")
                .set("color", "#FFFFFF");

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        FormLayout form = new FormLayout(titleField,descriptionField);
        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);

        dialog.add(form, buttons);
        dialog.open();
    }

    private void openEditModal(Course course, Grid<Course> grid) {
        Dialog dialog = new Dialog();
        TextField titleField = new TextField("Title",course.getTitle() );
        TextField decriptionField = new TextField("Description", course.getDescription());

        Button saveButton = new Button("Save", e -> {
            courseService.editCourse(course.getId(), new CreateCourse(titleField.getValue(),decriptionField.getValue()));
            refresh(grid);
            dialog.close();
            Notification.show("Course updated");
        });
        saveButton.getStyle()
                .set("background-color", "#4CAF50")
                .set("color", "#FFFFFF");

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        FormLayout form = new FormLayout(titleField,decriptionField);
        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);

        dialog.add(form, buttons);
        dialog.open();
    }

    private void deleteTag(Course course, Grid<Course> grid) {
        courseService.deleteCourse(course.getId());
        refresh(grid);
        Notification.show("Course deleted");
    }

    private void refresh(Grid<Course> grid) {
        grid.setItems((List<Course>)courseService.getAllCourses().responseObject());
    }
}
