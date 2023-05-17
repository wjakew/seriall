/**
 * by Jakub Wawak
 * kubawawak@gmail.com / j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.ui.components;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import usp.jakubwawak.database_object.GridElement;
import usp.jakubwawak.seriall.SeriallApplication;

/**
 * Object for showing log data
 */
public class LogViewerComponent {
    public Dialog main_dialog;
    VerticalLayout main_layout;

    Grid<GridElement> log_grid;

    /**
     * Constructor
     */
    public LogViewerComponent(){
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();

        log_grid = new Grid<>(GridElement.class,false);
        log_grid.addColumn(GridElement::getGridelement_id).setHeader("ID");
        log_grid.addColumn(GridElement::getGridelement_details).setHeader("Czas");
        log_grid.addColumn(GridElement::getGridelement_text).setHeader("Log");
        log_grid.setItems(SeriallApplication.database.list_log());
        log_grid.setSizeFull();log_grid.setWidth("850px");log_grid.setHeight("550pxs");
        prepare_dialog();
    }

    /**
     * Function for preparing dialog components
     */
    void prepare_dialog(){
        main_layout.add(new H3("Archiwum Zdarzeń"));
        main_layout.add(log_grid);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");
        main_dialog.add(main_layout);
    }
}
