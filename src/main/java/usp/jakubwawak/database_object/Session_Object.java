/**
 * by Jakub Wawak
 * kubawawak@gmail.com / j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.database_object;

import usp.jakubwawak.seriall.SeriallApplication;

import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

/**
 * Object for storing session data
 */
public class Session_Object implements Serializable {

    public ArrayList<String> serial_codes;
    public ArrayList<String> matching_codes;

    public ArrayList<GridElement> serial_codes_grid;
    public ArrayList<GridElement> matching_codes_grid;

    public LocalDateTime start_time;

    /**
     * Constructor
     */
    public Session_Object(){
        start_time = LocalDateTime.now(ZoneId.of("Europe/Warsaw"));
        serial_codes = new ArrayList<>();
        matching_codes = new ArrayList<>();
        serial_codes_grid = new ArrayList<>();
        matching_codes_grid = new ArrayList<>();
    }

    /**
     * Function for adding serial code to collection
     * @param code
     */
    public void add_serialcode(String code){
        if ( serial_codes.contains(code)){
            if ( !matching_codes.contains(code)){
                matching_codes.add(code);
                matching_codes_grid.add(new GridElement(code));
            }
        }
        serial_codes.add(code);
        serial_codes_grid.add(new GridElement(code));
    }

    /**
     * Function for removing serial code from collection
     * @param code
     */
    public void remove_serialcode(String code){
        serial_codes.remove(code);
        if (matching_codes.contains(code)){
            matching_codes.remove(code);
        }
        serial_codes_grid.clear();
        for(String element : serial_codes){
            serial_codes_grid.add(new GridElement(element));
        }
        matching_codes_grid.clear();
        for(String element : matching_codes){
            matching_codes_grid.add(new GridElement(element));
        }
    }

    /**
     * Function for creating object description
     * @return String
     */
    public String get_desc(){
        return "Liczba elementow: "+serial_codes.size()+", Liczba powtórzeń: "+matching_codes.size();
    }

    /**
     * Function for clearing dataset
     */
    public void clear(){
        serial_codes.clear();
        matching_codes.clear();
        serial_codes_grid.clear();;
        matching_codes_grid.clear();
    }

    /**
     * Function for checking if object is empty
     * @return boolean
     */
    public boolean empty(){
        return serial_codes.size() == 0 && matching_codes.size() == 0;
    }

    /**
     * Function for exporting source
     * @param source
     * @return File
     */
    public File export_to_txt(int source){
        try {
            File currDir = new File(".");
            String path = currDir.getAbsolutePath();
            String fileName = LocalDateTime.now().toString();
            fileName = fileName.replaceAll("-","");
            fileName = fileName.replaceAll(":","");
            fileName = fileName + "-export.txt";
            String fileLocation = path.substring(0,path.length()-1) + fileName;

            File file = new File(fileLocation);

            FileWriter fw = new FileWriter(file);

            ArrayList<String> source_collection = null;
            switch (source) {
                case 1 -> {
                    // source set to all codes
                    source_collection = serial_codes;
                }
                case 2 -> {
                    // source set to matched codes
                    source_collection = matching_codes;
                }
            }

            if ( source_collection != null ){
                for(String code : source_collection){
                    fw.write(code+"\n");
                }
                fw.close();
                SeriallApplication.database.log("EXPORT","Exported data to file! ("+file.getAbsolutePath()+")");
                return file;
            }
            return null;
        } catch (Exception iox) {
            SeriallApplication.database.log("EXPORT-FAILED","Failed to export file ("+iox.toString()+")");
            return null;
        }
    }

}
