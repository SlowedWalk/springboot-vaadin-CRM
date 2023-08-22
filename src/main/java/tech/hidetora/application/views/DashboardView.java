package tech.hidetora.application.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import tech.hidetora.application.data.service.CrmService;

/**
 * @author Hidetora
 * @version 1.0.0
 * @since 2022/04/18
 */
//@PermitAll
@Route(value = "dashboard", layout = MainLayout.class) // DashboardView is mapped to the "dashboard" path and uses MainLayout as a parent layout.
@PageTitle("Dashboard | Vaadin CRM")
@RolesAllowed("ADMIN") // Only users with the ROLE_ADMIN role can access the view.
public class DashboardView extends VerticalLayout {
    private final CrmService service;

    public DashboardView(CrmService service) { // Takes CrmService as a constructor parameter and saves it as a field.
        this.service = service;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER); // Centers the contents of the layout.
        add(getContactStats(), getCompaniesChart());
    }

    private Component getContactStats() {
        Span stats = new Span(service.countContacts() + " contacts"); // Calls the service to get the number of contacts.
        stats.addClassNames(
                LumoUtility.FontSize.XLARGE,
                LumoUtility.Margin.Top.MEDIUM);
        return stats;
    }

    private Chart getCompaniesChart() {
        Chart chart = new Chart(ChartType.PIE);

        DataSeries dataSeries = new DataSeries();
        /**
         * Calls the service to get all companies, then creates a DataSeriesItem for each,
         * containing the company name and employee count.
         * Don’t worry about the compilation error,the missing method is added in the next step.
         * */
        service.findAllCompanies().forEach(company ->
                dataSeries.add(new DataSeriesItem(company.getName(), company.getEmployeeCount())));
        chart.getConfiguration().setSeries(dataSeries);
        return chart;
    }

}
