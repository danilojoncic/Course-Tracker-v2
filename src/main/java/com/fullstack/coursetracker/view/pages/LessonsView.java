package com.fullstack.coursetracker.view.pages;

import com.fullstack.coursetracker.dto.CreateLesson;
import com.fullstack.coursetracker.dto.ServiceResponse;
import com.fullstack.coursetracker.model.Course;
import com.fullstack.coursetracker.model.Instructor;
import com.fullstack.coursetracker.model.Lesson;
import com.fullstack.coursetracker.model.Tag;
import com.fullstack.coursetracker.service.CourseService;
import com.fullstack.coursetracker.service.InstructorService;
import com.fullstack.coursetracker.service.LessonService;
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
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.listbox.MultiSelectListBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Route(value = "lessons", layout = MainLayout.class)
public class LessonsView extends VerticalLayout {
    private final LessonService lessonService;
    private final CourseService courseService;
    private final InstructorService instructorService;
    private final TagsService tagsService;
    private final Grid<Lesson> grid = new Grid<>(Lesson.class);

    public LessonsView(LessonService lessonService,CourseService courseService,InstructorService instructorService,TagsService tagsService) {
        this.lessonService = lessonService;
        this.courseService = courseService;
        this.instructorService = instructorService;
        this.tagsService = tagsService;

        setPadding(true);
        setSpacing(true);


        grid.setColumns("id", "title", "createdAt");
        grid.addColumn(lesson -> lesson.getCourse().getTitle()).setHeader("Course");
        grid.addColumn(lesson -> lesson.getInstructors().stream()
                        .map(Instructor::getName)
                        .collect(Collectors.joining(", ")))
                .setHeader("Instructors");

        grid.addColumn(lesson -> lesson.getTags().stream()
                        .map(Tag::getTitle)
                        .collect(Collectors.joining(", ")))
                .setHeader("Tags");
        grid.addComponentColumn(lesson -> createActionsColumn(lesson)).setHeader("Actions");
        List<Lesson> lessons = (List<Lesson>)lessonService.getAllLessons().responseObject();
        grid.setItems(lessons);
        grid.addItemClickListener(event -> openEditModal(event.getItem()));

        Button createBtn = new Button("Create", event -> openCreateModal());
        createBtn.getStyle().set("background-color", "#4CAF50").set("color", "#FFFFFF");

        add(grid, createBtn);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    private HorizontalLayout createActionsColumn(Lesson lesson) {
        Button editButton = new Button("Edit", e -> openEditModal(lesson));
        editButton.getStyle().set("background-color", "#FFD700").set("color", "#000000");

        Button deleteButton = new Button("Delete", e -> deleteLesson(lesson));
        deleteButton.getStyle().set("background-color", "#FF0000").set("color", "#FFFFFF");

        return new HorizontalLayout(editButton, deleteButton);
    }

    private void openCreateModal() {
        Dialog dialog = new Dialog();
        TextField titleField = new TextField("Title");
        DatePicker createdAtField = new DatePicker("Created At");
        ComboBox<String> courseField = new ComboBox<>("Course");
        MultiSelectListBox<String> instructorsField = new MultiSelectListBox<>();
        MultiSelectListBox<String> tagsField = new MultiSelectListBox<>();

        List<String> courseOptions = new ArrayList<>();
        List<Course> courses = (List<Course>) courseService.getAllCourses().responseObject();
        courses.forEach(course -> courseOptions.add(course.getTitle()));
        courseField.setItems(courseOptions);

        List<String> insString = new ArrayList<>();
        List<Instructor> instructors = (List<Instructor>) instructorService.getAllInstructors().responseObject();
        instructors.forEach(instructor -> insString.add(instructor.getName()));
        instructorsField.setItems(insString);

        List<String> tagsString = new ArrayList<>();
        List<Tag> tags = (List<Tag>) tagsService.getAllTags().responseObject();
        tags.forEach(tag -> tagsString.add(tag.getTitle()));
        tagsField.setItems(tagsString);

        Button saveButton = new Button("Save", e -> {
            CreateLesson createLesson = new CreateLesson(
                    titleField.getValue(),
                    courseField.getValue(),
                    new ArrayList<>(tagsField.getValue()),
                    new ArrayList<>(instructorsField.getValue()),
                    createdAtField.getValue()
            );
            lessonService.createLesson(createLesson);
            refresh();
            dialog.close();
            Notification.show("Lesson created");
        });
        saveButton.getStyle().set("background-color", "#4CAF50").set("color", "#FFFFFF");

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        FormLayout form = new FormLayout(titleField, createdAtField, courseField, instructorsField, tagsField);
        dialog.add(form, new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }
    private void openEditModal(Lesson lesson) {
        Dialog dialog = new Dialog();
        TextField titleField = new TextField("Title", lesson.getTitle());
        DatePicker createdAtField = new DatePicker("Created At", lesson.getCreatedAt());
        ComboBox<String> courseField = new ComboBox<>("Course");
        MultiSelectListBox<String> instructorsField = new MultiSelectListBox<>();
        MultiSelectListBox<String> tagsField = new MultiSelectListBox<>();

        List<String> courseOptions = new ArrayList<>();
        List<Course> courses = (List<Course>) courseService.getAllCourses().responseObject();
        courses.forEach(course -> courseOptions.add(course.getTitle()));
        courseField.setItems(courseOptions);

        List<String> insString = new ArrayList<>();
        List<Instructor> instructors = (List<Instructor>) instructorService.getAllInstructors().responseObject();
        instructors.forEach(instructor -> insString.add(instructor.getName()));
        instructorsField.setItems(insString);

        List<String> tagsString = new ArrayList<>();
        List<Tag> tags = (List<Tag>) tagsService.getAllTags().responseObject();
        tags.forEach(tag -> tagsString.add(tag.getTitle()));
        tagsField.setItems(tagsString);

        courseField.setValue(lesson.getCourse().getTitle());
        instructorsField.setValue(lesson.getInstructors().stream().map(Instructor::getName).collect(Collectors.toSet()));
        tagsField.setValue(lesson.getTags().stream().map(Tag::getTitle).collect(Collectors.toSet()));

        Button saveButton = new Button("Save", e -> {
            CreateLesson createLesson = new CreateLesson(
                    titleField.getValue(),
                    courseField.getValue(),
                    new ArrayList<>(tagsField.getValue()),
                    new ArrayList<>(instructorsField.getValue()),
                    createdAtField.getValue()
            );
            ServiceResponse sr = lessonService.editOneLesson(lesson.getId(), createLesson);
            System.out.println(sr.code());
            System.out.println(sr.responseObject());
            refresh();
            dialog.close();
            Notification.show("Lesson updated");
        });
        saveButton.getStyle().set("background-color", "#4CAF50").set("color", "#FFFFFF");

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        FormLayout form = new FormLayout(titleField, createdAtField, courseField, instructorsField, tagsField);
        dialog.add(form, new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }

    private void deleteLesson(Lesson lesson) {
        lessonService.deleteOneLesson(lesson.getId());
        refresh();
        Notification.show("Lesson deleted");
    }

    private void refresh() {
        grid.setItems((List<Lesson>)lessonService.getAllLessons().responseObject());
    }
}
