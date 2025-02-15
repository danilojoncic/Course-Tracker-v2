package com.fullstack.coursetracker.view.pages;

import com.fullstack.coursetracker.dto.ServiceResponse;
import com.fullstack.coursetracker.model.Instructor;
import com.fullstack.coursetracker.service.InstructorService;
import com.fullstack.coursetracker.service.TransactionService;
import com.fullstack.coursetracker.view.layouot.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route(value = "stats", layout = MainLayout.class)
public class StatsView extends VerticalLayout {
    private final TransactionService transactionService;
    private final InstructorService instructorService;

    public StatsView(TransactionService transactionService, InstructorService instructorService) {
        this.transactionService = transactionService;
        this.instructorService = instructorService;

        // Center the content
        setSizeFull(); // Make the layout take up the full height of the screen
        setAlignItems(Alignment.CENTER); // Center horizontally
        setJustifyContentMode(JustifyContentMode.CENTER); // Center vertically

        setPadding(true);
        setSpacing(true);

        // Add a title
        add(new H2("Statistics"));

        // Display total revenue
        displayTotalRevenue();

        // Display revenue by instructor
        displayRevenueByInstructor();
    }

    private void displayTotalRevenue() {
        ServiceResponse response = transactionService.getTotalRevenue();
        Integer totalRevenue = (Integer) response.responseObject();

        Div card = new Div();
        card.getStyle()
                .set("border", "1px solid #ccc")
                .set("border-radius", "4px")
                .set("padding", "16px")
                .set("background-color", "#f9f9f9");

        Span title = new Span("Total Revenue");
        title.getStyle()
                .set("font-size", "18px")
                .set("font-weight", "bold");

        Span value = new Span("RSD: " + totalRevenue);
        value.getStyle()
                .set("font-size", "24px")
                .set("color", "#4CAF50");

        card.add(title, new Div(), value);
        add(card);
    }

    private void displayRevenueByInstructor() {
        List<String> instructorNames = new ArrayList<>();
        List<Instructor> ins = (List<Instructor>) instructorService.getAllInstructors().responseObject();
        ins.forEach(instructor -> instructorNames.add(instructor.getName()));

        ComboBox<String> instructorComboBox = new ComboBox<>("Select Instructor");
        instructorComboBox.setItems(instructorNames);

        Div card = new Div();
        card.getStyle()
                .set("border", "1px solid #ccc")
                .set("border-radius", "4px")
                .set("padding", "16px")
                .set("background-color", "#f9f9f9");

        Span title = new Span("Revenue by Instructor");
        title.getStyle()
                .set("font-size", "18px")
                .set("font-weight", "bold");

        Span value = new Span();
        value.getStyle()
                .set("font-size", "24px")
                .set("color", "#4CAF50");

        Button fetchButton = new Button("Fetch Revenue", e -> {
            String selectedInstructor = instructorComboBox.getValue();
            if (selectedInstructor != null) {
                ServiceResponse response = transactionService.getTotalRevenueByInstructor(selectedInstructor);
                if (response.code() == 200) {
                    Integer revenue = (Integer) response.responseObject();
                    value.setText("RSD: " + revenue);
                } else {
                    value.setText("Error: " + response.responseObject());
                }
            } else {
                value.setText("Please select an instructor.");
            }
        });

        card.add(title, new Div(), instructorComboBox, new Div(), fetchButton, new Div(), value);
        add(card);
    }
}