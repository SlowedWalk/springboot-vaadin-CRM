package tech.hidetora.application.views.list;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import tech.hidetora.application.data.entity.Company;
import tech.hidetora.application.data.entity.Contact;
import tech.hidetora.application.data.entity.Status;

import java.util.List;

/**
 * @author Hidetora
 * @version 1.0.0
 * @since 2022/04/18
 **/

/**
 * ContactForm extends FormLayout:
 * a responsive layout that shows form fields in one or two columns,
 * depending on the viewport width.
 */
public class ContactForm extends FormLayout {
    Binder<Contact> contactBinder = new Binder<>(Contact.class); // Creates a Binder for Contact.
    TextField firstName = new TextField("First name"); //Creates all the UI components as fields in the component.
    TextField lastName = new TextField("Last name");
    EmailField email = new EmailField("Email");
    ComboBox<Status> status = new ComboBox<>("Status");
    ComboBox<Company> company = new ComboBox<>("Company");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    public ContactForm(List<Company> companies, List<Status> statuses) {
        // Gives the component a CSS class name, so you can style it later.
        addClassName("contact-form");
        // Binds the fields in this instance of ContactForm to the corresponding properties in Contact.
        contactBinder.bindInstanceFields(this);
        company.setItems(companies);
        company.setItemLabelGenerator(Company::getName);
        status.setItems(statuses);
        status.setItemLabelGenerator(Status::getName);
        /**
         * Adds all the UI components to the layout.
         * The buttons require a bit of extra configuration.
         * Create and call a new method, createButtonsLayout().
         */
        add(firstName, lastName, email, company, status, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        /**
         * Makes the buttons visually distinct from each other using built-in theme variants.
         */
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClassName("hover");
        delete.addClassName("hover");
        close.addClassName("hover");

        /**
         * Defines keyboard shortcuts: Enter to save and Escape to close the editor.
         * */
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        /**
         * The save button calls the validateAndSave() method.
         * */
        save.addClickListener(event -> validateAndSave());
        /**
         * The delete button triggers a delete event and passes the active contact.
         * */
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, contactBinder.getBean())));
        /**
         * The cancel button fires a close event.
         * */
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        /**
         * Validates the form every time it changes.
         * If it’s invalid, it disables the save button to avoid invalid submissions.
         * */
        contactBinder.addStatusChangeListener(e -> save.setEnabled(contactBinder.isValid()));

        /**
         * Returns a HorizontalLayout containing the buttons to place them next to each other.
         * */
        return new HorizontalLayout(save, delete, close);
    }

    public void setContact(Contact contact) {
        contactBinder.setBean(contact);
    }

    private void validateAndSave() {
        /**
         * Fire a save event, so the parent component can handle the action.
         * */
        save.setEnabled(false);
        if(contactBinder.isValid()) {
            fireEvent(new SaveEvent(this, contactBinder.getBean())); // (5)
        }
        Notification.show("Contact saved", 3000, Notification.Position.TOP_CENTER);
    }

    // Events
    public static abstract class ContactFormEvent extends ComponentEvent<ContactForm> {
        private Contact contact;

        /**
         * ContactFormEvent is a common superclass for all the events.
         * It contains the contact that was edited or deleted.
         * */
        protected ContactFormEvent(ContactForm source, Contact contact) {
            super(source, false);
            this.contact = contact;
        }

        public Contact getContact() {
            return contact;
        }

    }
    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(ContactForm source, Contact contact) {
            super(source, contact);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(ContactForm source, Contact contact) {
            super(source, contact);
        }

    }

    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(ContactForm source) {
            super(source, null);
        }
    }

    /**
     * The add*Listener() methods that passes the well-typed event type to Vaadin’s
     * event bus to register the custom event types.
     */
    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
