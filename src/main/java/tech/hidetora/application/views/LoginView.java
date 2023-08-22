package tech.hidetora.application.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

/**
 * @author Hidetora
 * @version 1.0.0
 * @since 2022/04/18
 */
@PermitAll
@Route("login")
@PageTitle("Login | Vaadin CRM")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    /**
     * Instantiate a LoginForm component to capture username and password.
     * */
    private final LoginForm login = new LoginForm();

    /**
     * Make LoginView full size and center its content both horizontally and vertically by calling
     * setAlignItems(`Alignment.CENTER)` and setJustifyContentMode(`JustifyContentMode.CENTER)`.
     * */
    public LoginView(){
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        /**
         * Set the LoginForm action to "login" to post the login form to Spring Security.
         * */
        login.setAction("login");

        add(new H1("CRM Built With Vaadin"), login);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
        if(beforeEnterEvent.getLocation() // Read query parameters and show an error if a login attempt fails.
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}
