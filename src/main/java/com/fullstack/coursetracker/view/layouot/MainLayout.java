package com.fullstack.coursetracker.view.layouot;

import com.fullstack.coursetracker.view.pages.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {
    public MainLayout() {
        H1 title = new H1("Course Tracker");
        title.getStyle()
                .set("margin", "0")
                .set("font-size", "1.5em")
                .set("align-self", "center")
                .set("font-weight", "bold");

        Button exitButton = new Button("Exit", event -> System.exit(0));
        exitButton.getStyle()
                .set("margin-left", "auto")
                .set("align-self", "center")
                .set("cursor", "pointer")
                .set("transition", "color 0.2s")
                .set("background-color", "#FF0000")
                .set("color", "#FFFFFF");

        // Create the tabs
        Tabs tabs = new Tabs(
                new Tab(new RouterLink("Home", MainView.class)),
                new Tab(new RouterLink("Stats", StatsView.class)),
                new Tab(new RouterLink("Courses", CourseView.class)),
                new Tab(new RouterLink("Instructors", InstructorView.class)),
                new Tab(new RouterLink("Tags", TagsView.class)),
                new Tab(new RouterLink("Students", StudentView.class)),
                new Tab(new RouterLink("Lessons", LessonsView.class)),
                new Tab(new RouterLink("Transactions", TransactionsView.class))
        );
        tabs.getStyle()
                .set("margin-left", "2em")
                .set("align-self", "center")
                .set("cursor", "pointer")
                .set("transition", "color 0.2s");

        HorizontalLayout header = new HorizontalLayout(title, tabs, exitButton);
        header.setWidthFull();
        header.setSpacing(false);
        header.setPadding(true);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.getStyle()
                .set("background-color", "#f3f4f6")
                .set("box-shadow", "0px 2px 4px rgba(0,0,0,0.1)");

        addToNavbar(header);
    }
}