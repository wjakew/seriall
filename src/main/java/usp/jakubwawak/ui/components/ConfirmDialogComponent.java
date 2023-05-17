/**
 * by Jakub Wawak
 * kubawawak@gmail.com / j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.ui.components;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;

/**
 * Dialog for confirming important data
 */
public class ConfirmDialogComponent {

    public boolean decision_flag;

    public ConfirmDialog dialog;

    String header,text;

    /**
     * Constructor
     * @param header
     * @param text
     */
    public ConfirmDialogComponent(String header, String text){
        this.header = header;
        this.text = text;

        decision_flag = false;
        dialog = new ConfirmDialog();
        prepare_dialog();
    }

    /**
     * Function for preparing dialog components
     */
    void prepare_dialog(){
        dialog.setHeader(header);
        dialog.setText(text);

        dialog.setCancelable(true);
        dialog.addCancelListener(event -> decision_flag = false);

        dialog.setRejectable(true);
        dialog.setRejectText("Discard");
        dialog.addRejectListener(event -> decision_flag = false);

        dialog.setConfirmText("Save");
        dialog.addConfirmListener(event -> decision_flag = true);
    }
}
