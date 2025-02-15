package com.fullstack.coursetracker.view.pages;

import com.fullstack.coursetracker.dto.CreateTag;
import com.fullstack.coursetracker.model.Tag;
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

@Route(value = "tags", layout = MainLayout.class)
public class TagsView extends VerticalLayout {
    private final TagsService tagsService;

    public TagsView(TagsService tagsService) {
        this.tagsService = tagsService;

        setPadding(true);
        setSpacing(true);

        Grid<Tag> grid = new Grid<>(Tag.class);
        grid.setColumns("id", "title");

        grid.addComponentColumn(tag -> createActionsColumn(tag, grid)).setHeader("Actions");

        grid.addItemClickListener(event -> openEditModal(event.getItem(), grid));

        List<Tag> tags = (List<Tag>) tagsService.getAllTags().responseObject();
        grid.setItems(tags);

        Button createBtn = new Button("Create", event -> openCreateModal(grid));
        createBtn.getStyle()
                .set("background-color", "#4CAF50")
                .set("color", "#FFFFFF");

        add(grid, createBtn);

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    private HorizontalLayout createActionsColumn(Tag tag, Grid<Tag> grid) {
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

    private void openCreateModal(Grid<Tag> grid) {
        Dialog dialog = new Dialog();
        TextField titleField = new TextField("Title");

        Button saveButton = new Button("Save", e -> {
            tagsService.createTag(new CreateTag(titleField.getValue()));
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

    private void openEditModal(Tag tag, Grid<Tag> grid) {
        Dialog dialog = new Dialog();
        TextField titleField = new TextField("Title", tag.getTitle());

        Button saveButton = new Button("Save", e -> {
            tagsService.editTag(tag.getId(), new CreateTag(titleField.getValue()));
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

    private void deleteTag(Tag tag, Grid<Tag> grid) {
        tagsService.deleteTag(tag.getId());
        refresh(grid);
        Notification.show("Tag deleted");
    }

    private void refresh(Grid<Tag> grid) {
        grid.setItems((List<Tag>) tagsService.getAllTags().responseObject());
    }
}