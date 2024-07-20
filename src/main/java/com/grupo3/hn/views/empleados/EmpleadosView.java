package com.grupo3.hn.views.empleados;

import com.grupo3.hn.data.Datos;
import com.grupo3.hn.services.DatosService;
import com.grupo3.hn.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Empleados")
@Route(value = "/:datosID?/:action?(edit)", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class EmpleadosView extends Div implements BeforeEnterObserver {

    private final String DATOS_ID = "datosID";
    private final String DATOS_EDIT_ROUTE_TEMPLATE = "/%s/edit";

    private final Grid<Datos> grid = new Grid<>(Datos.class, false);

    private TextField codigo;
    private TextField nombre_completo;
    private TextField email;
    private TextField departamento;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<Datos> binder;

    private Datos datos;

    private final DatosService datosService;

    public EmpleadosView(DatosService datosService) {
        this.datosService = datosService;
        addClassNames("empleados-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("codigo").setAutoWidth(true);
        grid.addColumn("nombre_completo").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.addColumn("departamento").setAutoWidth(true);
        grid.setItems(query -> datosService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(DATOS_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(EmpleadosView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Datos.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(codigo).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("codigo");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.datos == null) {
                    this.datos = new Datos();
                }
                binder.writeBean(this.datos);
                datosService.update(this.datos);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(EmpleadosView.class);
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
        Optional<Long> datosId = event.getRouteParameters().get(DATOS_ID).map(Long::parseLong);
        if (datosId.isPresent()) {
            Optional<Datos> datosFromBackend = datosService.get(datosId.get());
            if (datosFromBackend.isPresent()) {
                populateForm(datosFromBackend.get());
            } else {
                Notification.show(String.format("The requested datos was not found, ID = %s", datosId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(EmpleadosView.class);
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
        nombre_completo = new TextField("Nombre_completo");
        email = new TextField("Email");
        departamento = new TextField("Departamento");
        formLayout.add(codigo, nombre_completo, email, departamento);

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

    private void populateForm(Datos value) {
        this.datos = value;
        binder.readBean(this.datos);

    }
}