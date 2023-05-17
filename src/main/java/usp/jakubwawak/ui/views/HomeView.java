/**
 * by Jakub Wawak
 * kubawawak@gmail.com / j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.ui.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import usp.jakubwawak.database_object.GridElement;
import usp.jakubwawak.seriall.SeriallApplication;
import usp.jakubwawak.ui.Layout;

/**
 * Object for showing home view data
 */
@PageTitle("seriall by Jakub Wawak")
@Route(value = "/", layout = Layout.class)
public class HomeView extends VerticalLayout {

    Grid<GridElement> serialcodes_grid;
    Grid<GridElement> matching_grid;
    Grid<GridElement> unique_grid;

    Label serialcode_counter,uniquecode_counter,matching_counter;

    TextField main_code_field;

    TextField search_codefield_field,search_matching_field;

    /**
     * Constructor
     */
    public HomeView(){
        this.getElement().setAttribute("theme", Lumo.DARK);
        SeriallApplication.current_layout = this;

        prepare_components();
        prepare_view2();

        setSizeFull();
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    /**
     * Function for preparing components data
     */
    void prepare_components(){
        serialcodes_grid = new Grid(GridElement.class,false);
        matching_grid = new Grid(GridElement.class,false);
        unique_grid = new Grid(GridElement.class,false);

        serialcodes_grid.setSizeFull();matching_grid.setSizeFull();
        serialcodes_grid.setHeight("650px"); serialcodes_grid.setWidth("266px");
        unique_grid.setHeight("650px"); unique_grid.setWidth("266px");
        matching_grid.setHeight("650px");matching_grid.setWidth("266px");
        main_code_field = new TextField();
        main_code_field.setPlaceholder("Nowy kod kreskowy...");
        main_code_field.setPrefixComponent(VaadinIcon.BARCODE.create());
        main_code_field.setHeight("75px");main_code_field.setWidth("700px");

        // loading grids layout and items
        serialcodes_grid.addColumn(GridElement::getGridelement_text).setHeader("Wszystkie kody");
        matching_grid.addColumn(GridElement::getGridelement_text).setHeader("Kody Powtarzające");
        unique_grid.addColumn(GridElement::getGridelement_text).setHeader("Kody Unikalne");
        serialcodes_grid.setItems(SeriallApplication.current_session.serial_codes_grid);
        matching_grid.setItems(SeriallApplication.current_session.matching_codes_grid);
        unique_grid.setItems(SeriallApplication.current_session.unique_codes_grid);

        // setting counters
        serialcode_counter = new Label("Ilość wszystkich kodów: "+ Integer.toString(SeriallApplication.current_session.serial_codes.size()));
        uniquecode_counter = new Label("Ilość kodów unikalnych: "+Integer.toString(SeriallApplication.current_session.unique_codes.size()));
        matching_counter = new Label("Ilość kodów powtarzalnych: "+ Integer.toString(SeriallApplication.current_session.matching_codes.size()));


        // serial codes grid search
        search_codefield_field = new TextField();
        search_codefield_field.setHeight("75px"); search_codefield_field.setWidth("300px");
        search_codefield_field.setPlaceholder("Przeszukaj wszystkie kody...");
        GridListDataView<GridElement> dataView_codefield = serialcodes_grid.setItems(SeriallApplication.current_session.serial_codes_grid);
        search_codefield_field.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        search_codefield_field.setValueChangeMode(ValueChangeMode.EAGER);
        search_codefield_field.addValueChangeListener(e -> dataView_codefield.refreshAll());

        dataView_codefield.addFilter(code -> {
            String searchTerm = search_codefield_field.getValue().trim();

            if (searchTerm.isEmpty())
                return true;

            return code.getGridelement_text().equals(search_codefield_field.getValue());
        });

        // matching codes grid search
        search_matching_field = new TextField();
        search_matching_field.setHeight("75px");search_matching_field.setWidth("300px");
        search_matching_field.setPlaceholder("Przeszukaj kody powtarzające się...");
        GridListDataView<GridElement> dataView_matching = matching_grid.setItems(SeriallApplication.current_session.matching_codes_grid);
        search_matching_field.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        search_matching_field.setValueChangeMode(ValueChangeMode.EAGER);
        search_matching_field.addValueChangeListener(e -> dataView_matching.refreshAll());

        dataView_matching.addFilter(code ->{
           String searchTerm = search_matching_field.getValue().trim();

           if ( searchTerm.isEmpty() ){
               return true;
           }

           return code.getGridelement_text().equals(search_matching_field.getValue());
        });

        // adding listener on enter
        main_code_field.addKeyPressListener(Key.ENTER, e->
        {
            if ( !main_code_field.getValue().equals("") ){
                int matching_size = SeriallApplication.current_session.matching_codes.size();

                SeriallApplication.current_session.add_serialcode(main_code_field.getValue());
                serialcodes_grid.getDataProvider().refreshAll();
                matching_grid.getDataProvider().refreshAll();
                unique_grid.getDataProvider().refreshAll();

                serialcode_counter.setText("Ilość wszystkich kodów: "+ Integer.toString(SeriallApplication.current_session.serial_codes.size()));
                uniquecode_counter.setText("Ilość kodów unikalnych: "+Integer.toString(SeriallApplication.current_session.unique_codes.size()));
                matching_counter.setText("Ilość kodów powtarzalnych: "+ Integer.toString(SeriallApplication.current_session.matching_codes.size()));

                Notification data = Notification.show("Dodano "+main_code_field.getValue()+"!");
                data.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                if ( matching_size < SeriallApplication.current_session.matching_codes.size() ){
                    Notification data2 = Notification.show("Wykryto powtórzenie! ("+main_code_field.getValue()+")");
                    data2.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
                }

                int ans = SeriallApplication.database.add_data_entry(main_code_field.getValue());

                if ( ans == -1 ){
                    Notification data3 = Notification.show("Błąd bazy danych dodawania kodu! Sprawdź log!");
                    data3.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }

                main_code_field.setValue("");
            }
        });
    }

    /**
     * Function for preparing components layout
     */
    void prepare_view(){
        HorizontalLayout main_counter_layout = new HorizontalLayout(serialcode_counter,uniquecode_counter,matching_counter);
        HorizontalLayout main_search_layout = new HorizontalLayout(search_codefield_field,search_matching_field);
        HorizontalLayout main_grid_layout = new HorizontalLayout(serialcodes_grid,matching_grid);
        main_grid_layout.setSizeFull();
        add(main_counter_layout);
        add(main_search_layout);
        add(main_grid_layout);
        add(main_code_field);
    }

    /**
     * Function for preparing components layout with 3 tables
     */
    void prepare_view2(){
        HorizontalLayout main_counter_layout = new HorizontalLayout(serialcode_counter,uniquecode_counter,matching_counter);
        HorizontalLayout main_search_layout = new HorizontalLayout(search_codefield_field,search_matching_field);
        HorizontalLayout main_grid_layout = new HorizontalLayout(serialcodes_grid,unique_grid,matching_grid);
        main_grid_layout.setSizeFull();
        add(main_counter_layout);
        add(main_search_layout);
        add(main_grid_layout);
        add(main_code_field);
    }

    /**
     * Function for refreshing data
     */
    public void refresh(){
        serialcodes_grid.getDataProvider().refreshAll();
        matching_grid.getDataProvider().refreshAll();
        unique_grid.getDataProvider().refreshAll();
        main_code_field.setValue("");
        serialcode_counter.setText("Ilość wszystkich kodów: "+ Integer.toString(SeriallApplication.current_session.serial_codes.size()));
        uniquecode_counter.setText("Ilość kodów unikalnych: "+Integer.toString(SeriallApplication.current_session.unique_codes.size()));
        matching_counter.setText("Ilość kodów powtarzalnych: "+ Integer.toString(SeriallApplication.current_session.matching_codes.size()));
    }

    /**
     * Function for clearing view
     */
    public void clear_view(){
        serialcodes_grid.getDataProvider().refreshAll();
        matching_grid.getDataProvider().refreshAll();
        unique_grid.getDataProvider().refreshAll();
        main_code_field.setValue("");
        serialcode_counter.setText("Ilość wszystkich kodów: "+ Integer.toString(SeriallApplication.current_session.serial_codes.size()));
        uniquecode_counter.setText("Ilość kodów unikalnych: "+Integer.toString(SeriallApplication.current_session.unique_codes.size()));
        matching_counter.setText("Ilość kodów powtarzalnych: "+ Integer.toString(SeriallApplication.current_session.matching_codes.size()));
    }
}
