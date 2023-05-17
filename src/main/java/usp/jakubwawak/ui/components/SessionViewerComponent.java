/**
 * by Jakub Wawak
 * kubawawak@gmail.com / j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.ui.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import usp.jakubwawak.database_object.GridElement;
import usp.jakubwawak.database_object.Session_Object;
import usp.jakubwawak.seriall.SeriallApplication;

import java.util.Set;

/**
 * Function for showing session data
 */
public class SessionViewerComponent {
    public Dialog main_dialog;
    VerticalLayout main_layout;

    Button loadsession_button;

    Grid<GridElement> session_grid;

    /**
     * Constructor
     */
    public SessionViewerComponent(){
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        session_grid = new Grid<>(GridElement.class,false);

        loadsession_button = new Button("Załaduj sesje", VaadinIcon.UPLOAD.create(),this::loadsessionbutton_action);
        loadsession_button.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

        session_grid.addColumn(GridElement::getGridelement_id).setHeader("ID");
        session_grid.addColumn(GridElement::getGridelement_details).setHeader("Czas");
        session_grid.addColumn(GridElement::getGridelement_text).setHeader("Opis");
        session_grid.setItems(SeriallApplication.database.list_sessions());
        session_grid.setWidth("900px"); session_grid.setHeight("450px");
        prepare_dialog();
    }

    /**
     * Function for preparing dialog components
     */
    void prepare_dialog(){
        main_layout.add(new H3("Archiwum Sesji"));
        main_layout.add(session_grid);
        main_layout.add(loadsession_button);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");
        main_dialog.add(main_layout);
    }

    /**
     * Function for setting session from database
     * @param e
     */
    private void loadsessionbutton_action(ClickEvent e){
        Set<GridElement> selected = session_grid.getSelectedItems();
        for(GridElement element : selected){
            Session_Object so = SeriallApplication.database.get_session_obj(element.getGridelement_id());
            if ( so != null ){
                SeriallApplication.current_session = so;
                SeriallApplication.current_layout.refresh();
                Notification.show("Sesja archiwalna została załadowana!");
                UI.getCurrent().getPage().reload();
            }
            else{
                Notification.show("Błąd bazy danych przy pobieraniu sesji. Sprawdź log!");
            }
            break;
        }
    }
}
