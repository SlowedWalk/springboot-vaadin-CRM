package tech.hidetora.application.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import tech.hidetora.application.views.list.ListView;

/**
 * @author Hidetora
 * @version 1.0.0
 * @since 2022/04/18
 */

public class MainLayout extends AppLayout { // AppLayout is a Vaadin layout with a header and a responsive drawer.
    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Vaadin CRM");
        /**
         * Instead of styling the text with raw CSS,
         * use Lumo Utility Classes shipped with the default theme.
         * */
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);

        /**
         * DrawerToggle is a menu button that toggles the visibility of the sidebar.
         * */
        var header = new HorizontalLayout(new DrawerToggle(), logo );

        /**
         * Centers the components in the header along the vertical axis.
         * */
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);

        /**
         * Adds the header layout to the application layout’s nav bar,
         * the section at the top of the screen.
         * */
        addToNavbar(header);

    }

    /**
     * Wraps the router link in a VerticalLayout and adds it to the AppLayout drawer.
     * Creates a RouterLink with the text "List" and ListView.class as the destination view.
     * */
    private void createDrawer() {
        addToDrawer(new VerticalLayout(
                new RouterLink("List", ListView.class)
        ));
    }
}
