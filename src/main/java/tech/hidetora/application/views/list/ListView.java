package tech.hidetora.application.views.list;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import tech.hidetora.application.data.entity.Contact;
import tech.hidetora.application.data.service.CrmService;
import tech.hidetora.application.views.MainLayout;


@PageTitle("Contacts | Vaadin CRM")
@Route(value="", layout = MainLayout.class)
@Slf4j
public class ListView extends VerticalLayout { // The view extends VerticalLayout, which places all child components vertically.
    Grid<Contact> grid = new Grid<>(Contact.class); // The Grid component is typed with Contact.
    TextField filterText = new TextField();
    ContactForm form; // Creates a reference to the form, so you have access to it from other methods.
    CrmService service; // Creates a reference to the service, so you have access to it from other methods.

    public ListView(CrmService service) { // Autowire CrmService through the constructor. Save it in a field, so you can access it from other methods.

        this.service = service;
        addClassName("list-view");
        // The grid configuration is extracted to a separate method to keep the constructor easier to read.
        configureGrid();
        // Create a method for initializing the form.
        configureForm();
        setSizeFull();
        // Add the toolbar and grid to the VerticalLayout.
        add(createToolBar(), getContent());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    public void configureGrid() {
        addClassName("contact-grid");
        grid.setSizeFull();
        // Define which properties of Contact the grid should show.
        grid.setColumns("firstName", "lastName", "email");
        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status");
        // Define custom columns for nested objects.
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");
        // Configure the columns to adjust automatically their size to fit their content.
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event -> editContact(event.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid); // Use setFlexGrow() to specify that the Grid should have twice the space of the form.
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private HorizontalLayout createToolBar() {
        filterText.setPlaceholder("Filter by name ...");
        filterText.setClearButtonVisible(true);
        /**
         * Configure the search field to fire value-change events only when the user stops typing.
         * This way you avoid unnecessary database calls,but the listener is still fired without the user leaving the focus from the field.
         **/
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList()); // Call updateList() any time the filter changes.

//        filterText.addInputListener(e -> {
//            log.info("Filter text: " + filterText.getValue());
//            updateList();
//        });
        Button addContactButton = new Button("Add Contact", new Icon(VaadinIcon.PLUS));
        addContactButton.addClassName("hover");
        addContactButton.addClickListener(click -> addContact());

        // The toolbar uses a HorizontalLayout to place the TextField and Button next to each other.
        var toolBar = new HorizontalLayout(filterText, addContactButton);
        // Adding some class names to components makes it easier to style the application later using CSS.
        toolBar.addClassName("toolbar");
        return  toolBar;
    }

    // Initialize the form with empty company and status lists: you’ll add these on the next part.
    private void configureForm() {
        // Use the service to fetch the companies and statuses from the database.
        form = new ContactForm(service.findAllCompanies(), service.findAllStatuses());
        form.setWidth("25em");
        form.addSaveListener(this::saveContact);
        form.addDeleteListener(this::deleteContact);
        form.addCloseListener(e -> closeEditor());
    }

    // updateList() sets the grid items by calling the service with the value from the filter text field.
    private void updateList() {
        System.out.println("Filter text: " + filterText.getValue());
        grid.setItems(service.findAllContacts(filterText.getValue()));
    }

    public void editContact(Contact contact) {
        if (contact == null) {
            closeEditor();
        } else {
            form.setContact(contact);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }

    private void saveContact(ContactForm.SaveEvent event) {
        service.saveContact(event.getContact());
        updateList();
        closeEditor();
    }

    private void deleteContact(ContactForm.DeleteEvent event) {
        service.deleteContact(event.getContact());
        updateList();
        closeEditor();
    }
}
