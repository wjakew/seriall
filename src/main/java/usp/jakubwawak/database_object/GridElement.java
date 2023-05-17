/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com / j.wawak@usp.pl
 */
package usp.jakubwawak.database_object;

import java.io.Serializable;

/**
 * Object for creating grid element data
 */
public class GridElement implements Serializable {

    String gridelement_text;
    int gridelement_id;

    String gridelement_details;

    /**
     * Constructor
     * @param gridelement_text
     */
    public GridElement(String gridelement_text){
        this.gridelement_text = gridelement_text;
    }

    /**
     * Constructor with ID
     * @param gridelement_text
     * @param gridelement_id
     */
    public GridElement(String gridelement_text,int gridelement_id){
        this.gridelement_text = gridelement_text;
        this.gridelement_id = gridelement_id;
    }

    /**
     * Constructor with ID and details
     * @param gridelement_text
     * @param gridelement_id
     * @param gridelement_details
     */
    public GridElement(String gridelement_text,int gridelement_id,String gridelement_details){
        this.gridelement_text = gridelement_text;
        this.gridelement_id = gridelement_id;
        this.gridelement_details = gridelement_details;
    }

    public String getGridelement_text() {
        return gridelement_text;
    }
    public int getGridelement_id(){return gridelement_id;}

    public String getGridelement_details(){return gridelement_details;}
}