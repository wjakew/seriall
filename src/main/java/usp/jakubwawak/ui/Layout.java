/**
 * by Jakub Wawak
 * kubawawak@gmail.com / j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.ui;


import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import usp.jakubwawak.seriall.SeriallApplication;
import usp.jakubwawak.ui.components.*;

/**
 * Object for creating webpage application layout
 */
public class Layout extends AppLayout {

    DrawerToggle drawerToggle;

    Button archive_button,archive_session_button,data_button;

    Button clearset_button; // button for clearing session
    Button export_button; // button for exporting data


    /**
     * Constructor
     */
    public Layout(){
        this.getElement().setAttribute("theme", Lumo.DARK);
        drawerToggle = new DrawerToggle();
        archive_button = new Button("Archiwum Zdarzeń", VaadinIcon.ARCHIVE.create(),this::archiveaction_button);
        data_button = new Button("Archiwum Danych",VaadinIcon.CALC_BOOK.create(),this::databutton_action);
        archive_session_button = new Button("Archiwum Sesji",VaadinIcon.CLOCK.create(),this::archivesessionbutton_action);


        clearset_button = new Button("Wyzeruj",this::clearsetbutton_action);
        clearset_button.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);

        export_button = new Button("Export",this::exportbutton_action);
        export_button.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_SUCCESS);


        this.setDrawerOpened(false);
        createHeader();
        createMenu();
    }

    /**
     * Function for creating header
     */
    private void createHeader(){
        HorizontalLayout header = new HorizontalLayout(drawerToggle,new H3("seriall"),clearset_button,export_button);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");
        addToNavbar(header);
    }

    /**
     * Function for creaiting side menu
     */
    private void createMenu(){
        VerticalLayout menu_layout = new VerticalLayout();
        archive_button.setSizeFull();data_button.setSizeFull(); archive_session_button.setSizeFull();
        archive_button.setHeight("75px");data_button.setHeight("75px"); archive_session_button.setHeight("75px");
        menu_layout.add(archive_button,data_button,archive_session_button);
        menu_layout.add(new H3(SeriallApplication.version));
        menu_layout.add(new H4(SeriallApplication.build));
        menu_layout.add(new H5("by Jakub Wawak"));
        menu_layout.setAlignItems(FlexComponent.Alignment.CENTER);
        menu_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        addToDrawer(menu_layout);
    }

    /**
     * Function for opening data entry archive
     * @param e
     */
    private void databutton_action(ClickEvent e){
        DataEntryViewerComponent devc = new DataEntryViewerComponent();
        SeriallApplication.current_layout.add(devc.main_dialog);
        devc.main_dialog.open();
    }

    /**
     * Action for clearing dataset action
     * @param e
     */
    private void clearsetbutton_action(ClickEvent e){
        if ( !SeriallApplication.current_session.empty() ){
            int ans = SeriallApplication.database.add_serialobject(SeriallApplication.current_session);
            if ( ans == 1 ){
                Notification data1 = Notification.show("Zapisano set w bazie danych!");
                data1.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
            else{
                Notification data1 = Notification.show("Błąd zapisu setu w bazie danych! Sprawdź log!");
                data1.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }

        SeriallApplication.current_session.clear();
        SeriallApplication.current_layout.clear_view();
        Notification.show("Usunięto aktualny set!");
    }

    /**
     * Function for exporintg set action
     * @param e
     */
    private void exportbutton_action(ClickEvent e){
        if ( !SeriallApplication.current_session.empty() ){
            ExportSourcePickerComponent espc = new ExportSourcePickerComponent();
            SeriallApplication.current_layout.add(espc.main_dialog);
            espc.main_dialog.open();
        }
        else{
            Notification.show("Aktualna sesja jest pusta!");
        }
    }

    /**
     * Function for archive session action
     * @param e
     */
    private void archivesessionbutton_action(ClickEvent e){
        SessionViewerComponent svc = new SessionViewerComponent();
        SeriallApplication.current_layout.add(svc.main_dialog);
        svc.main_dialog.open();
    }

    /**
     * Function for archiving action
     * @param e
     */
    private void archiveaction_button(ClickEvent e){
        LogViewerComponent lvc = new LogViewerComponent();
        SeriallApplication.current_layout.add(lvc.main_dialog);
        lvc.main_dialog.open();
    }
}
