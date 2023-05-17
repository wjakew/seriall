/**
 * by Jakub Wawak
 * kubawawak@gmail.com / j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.ui.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import usp.jakubwawak.database_object.GridElement;
import usp.jakubwawak.seriall.SeriallApplication;

import java.io.File;
import java.util.ArrayList;

/**
 * Object for creating component for Exporting
 */
public class ExportSourcePickerComponent {
    public Dialog main_dialog;
    VerticalLayout main_layout;

    Button export_button;

    ComboBox<GridElement> source_combobox, format_combobox;

    /**
     * Constructor
     */
    public ExportSourcePickerComponent(){
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();

        export_button = new Button("Export", VaadinIcon.FILE.create(),this::exportbutton_action);
        export_button.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

        ArrayList<GridElement> sources = new ArrayList<>();
        sources.add(new GridElement("Wszystkie kody"));
        sources.add(new GridElement("Kody unikalne"));

        ArrayList<GridElement> formats = new ArrayList<>();
        formats.add(new GridElement("TXT (Plik tekstowy)"));

        source_combobox = new ComboBox<>("Źródło danych");
        source_combobox.setItems(sources);
        source_combobox.setItemLabelGenerator(GridElement::getGridelement_text);
        source_combobox.setAllowCustomValue(false);

        format_combobox = new ComboBox<>("Format Pliku");
        format_combobox.setItems(formats);
        format_combobox.setItemLabelGenerator(GridElement::getGridelement_text);
        format_combobox.setAllowCustomValue(false);

        prepare_dialog();
    }

    /**
     * Function for preparing dialog components
     */
    void prepare_dialog(){
        main_layout.add(new H3("Kreator Eksportu"));
        main_layout.add(new Text("Wybierz źródło danych i format eksportu."));
        main_layout.add(new HorizontalLayout(source_combobox,format_combobox));
        main_layout.add(export_button);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");
        main_dialog.add(main_layout);
    }

    /**
     * Function for exporting object to files
     * @param e
     */
    private void exportbutton_action(ClickEvent e){
        GridElement selected_source = source_combobox.getValue();
        GridElement selected_format = format_combobox.getValue();
        int source = -1;
        File to_download = null;
        switch (selected_source.getGridelement_text()) {
            case "Wszystkie kody" -> {
                source = 1;
            }
            case "Kody unikalne" -> {
                source = 2;
            }
            default -> source = -3;
        }

        if (selected_format.getGridelement_text().equals("TXT (Plik tekstowy)")) {
            if (source > 0) {
                to_download = SeriallApplication.current_session.export_to_txt(source);
                if (to_download != null){
                    FileDownloaderComponent fdc = new FileDownloaderComponent(to_download);
                    main_layout.add(fdc.dialog);
                    fdc.dialog.open();
                }
            }
            else
                Notification.show("Błędnie wybrane źródło!");
        } else {
            Notification.show("Błędnie wybrany format pliku!");
        }
    }
}
