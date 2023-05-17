/**
 * by Jakub Wawak
 * kubawawak@gmail.com / j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.ui.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import usp.jakubwawak.database_object.GridElement;
import usp.jakubwawak.seriall.SeriallApplication;

import java.util.Set;

/**
 * Component for viewing data entry
 */
public class DataEntryViewerComponent {

    public Dialog main_dialog;
    VerticalLayout main_layout;
    Button add_button;

    Grid<GridElement> serial_grid;

    TextField search_field;

    /**
     * Constructor
     */
    public DataEntryViewerComponent(){
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();

        serial_grid = new Grid<>(GridElement.class,false);
        serial_grid.addColumn(GridElement::getGridelement_id).setHeader("ID");
        serial_grid.addColumn(GridElement::getGridelement_details).setHeader("Czas");
        serial_grid.addColumn(GridElement::getGridelement_text).setHeader("Kod");


        serial_grid.setSizeFull();serial_grid.setWidth("850px");serial_grid.setHeight("550px");
        serial_grid.setSelectionMode(Grid.SelectionMode.MULTI);
        serial_grid.setItems(SeriallApplication.database.list_data());

        // search grid field
        search_field = new TextField();
        search_field.setHeight("75px"); search_field.setWidth("400px");
        search_field.setPlaceholder("Wyszukaj kod...");
        GridListDataView<GridElement> dataView = serial_grid.setItems(SeriallApplication.database.list_data());
        search_field.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        search_field.setValueChangeMode(ValueChangeMode.EAGER);
        search_field.addValueChangeListener(e -> dataView.refreshAll());

        dataView.addFilter(code ->{
            String searchTerm = search_field.getValue().trim();

            if ( searchTerm.isEmpty() ){
                return true;
            }

            return code.getGridelement_text().equals(search_field.getValue());
        });


        add_button = new Button("Dodaj do sesji", VaadinIcon.PLUS.create(),this::addbutton_action);
        add_button.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        prepare_dialog();
    }

    /**
     * Function for creating components
     */
    void prepare_dialog(){
        main_layout.add(new H3("Archiwum danych"));
        main_layout.add(search_field);
        main_layout.add(serial_grid);
        main_layout.add(add_button);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");
        main_dialog.add(main_layout);
    }

    /**
     * Function for adding element to current session
     * @param e
     */
    private void addbutton_action(ClickEvent e){
        Set<GridElement> selected = serial_grid.getSelectedItems();
        int counter = 0;
        for(GridElement element : selected){
            SeriallApplication.current_session.add_serialcode(element.getGridelement_text());
            counter++;
        }

        if ( counter > 0 ){
            Notification.show("Dodano "+counter+" elementów!");
        }
        SeriallApplication.current_layout.refresh();
    }
}
