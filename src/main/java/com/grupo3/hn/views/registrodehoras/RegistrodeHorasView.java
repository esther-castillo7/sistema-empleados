package com.grupo3.hn.views.registrodehoras;

import com.grupo3.hn.data.Horas;
import com.grupo3.hn.services.HorasService;
import com.grupo3.hn.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Registro de Horas")
@Route(value = "horas-registro/:horasID?/:action?(edit)", layout = MainLayout.class)
public class RegistrodeHorasView extends Div implements BeforeEnterObserver {

    private final String HORAS_ID = "horasID";
    private final String HORAS_EDIT_ROUTE_TEMPLATE = "horas-registro/%s/edit";

    private final Grid<Horas> grid = new Grid<>(Horas.class, false);

    private TextField codigo;
    private DatePicker fecha_ingreso;
    private DatePicker fecha_salida;
    private TextField horas;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<Horas> binder;

    private Horas horas1;

    private final HorasService horas1Service;

    public RegistrodeHorasView(HorasService horas1Service) {
        this.horas1Service = horas1Service;
        addClassNames("registrode-horas-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("codigo").setAutoWidth(true);
        grid.addColumn("fecha_ingreso").setAutoWidth(true);
        grid.addColumn("fecha_salida").setAutoWidth(true);
        grid.addColumn("horas").setAutoWidth(true);
        grid.setItems(query -> horas1Service.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(HORAS_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(RegistrodeHorasView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Horas.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(codigo).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("codigo");
        binder.forField(horas).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("horas");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.horas1 == null) {
                    this.horas1 = new Horas();
                }
                binder.writeBean(this.horas1);
                horas1Service.update(this.horas1);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(RegistrodeHorasView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> horas1Id = event.getRouteParameters().get(HORAS_ID).map(Long::parseLong);
        if (horas1Id.isPresent()) {
            Optional<Horas> horas1FromBackend = horas1Service.get(horas1Id.get());
            if (horas1FromBackend.isPresent()) {
                populateForm(horas1FromBackend.get());
            } else {
                Notification.show(String.format("The requested horas1 was not found, ID = %s", horas1Id.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(RegistrodeHorasView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        codigo = new TextField("Codigo");
        fecha_ingreso = new DatePicker("Fecha_ingreso");
        fecha_salida = new DatePicker("Fecha_salida");
        horas = new TextField("Horas");
        formLayout.add(codigo, fecha_ingreso, fecha_salida, horas);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Horas value) {
        this.horas1 = value;
        binder.readBean(this.horas1);

    }
}
