package com.fullstack.coursetracker.view.pages;

import com.fullstack.coursetracker.service.DownloadService;
import com.fullstack.coursetracker.view.layouot.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

@Route(value = "", layout = MainLayout.class)
public class MainView extends VerticalLayout {

    private final DownloadService downloadService;

    public MainView(DownloadService downloadService) {
        this.downloadService = downloadService;

        // Center everything on the screen
        setSizeFull(); // Make the layout take up the full screen
        setJustifyContentMode(JustifyContentMode.CENTER); // Center vertically
        setDefaultHorizontalComponentAlignment(Alignment.CENTER); // Center horizontally
        setPadding(true);
        setSpacing(true);

        // Add a heading
        H1 heading = new H1("Welcome to Course Tracker!");
        heading.getStyle().set("text-align", "center");

        // Add a description
        Paragraph description = new Paragraph(
                "Use nav bar options to manage your courses, instructors, lessons, and more. " +
                        "You can also download a CSV file of all transactions."
        );
        description.getStyle().set("text-align", "center");

        // Create a StreamResource for the CSV file
        StreamResource csvResource = new StreamResource("transactions.csv", () -> {
            // Call the DownloadService to get the CSV file
            return new java.io.ByteArrayInputStream(downloadService.downlaodCsv());
        });

        // Create an Anchor component for downloading the CSV file
        Anchor downloadLink = new Anchor(csvResource, "Download Transactions CSV");
        downloadLink.getElement().setAttribute("download", true); // Ensure the file is downloaded
        downloadLink.getStyle()
                .set("font-size", "1.5em") // Make the link text larger
                .set("padding", "1em 2em") // Add padding to make the link bigger
                .set("background-color", "#4CAF50") // Green color
                .set("color", "#FFFFFF") // White text
                .set("text-decoration", "none") // Remove underline
                .set("border-radius", "5px"); // Add rounded corners

        // Add components to the layout
        add(heading, description, downloadLink);
    }
}