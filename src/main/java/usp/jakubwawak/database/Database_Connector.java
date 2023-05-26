/**
 * by Jakub Wawak
 * kubawawak@gmail.com / j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.database;

import usp.jakubwawak.database_object.GridElement;
import usp.jakubwawak.database_object.Session_Object;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

/**
 * Connector for connecting to seriall database
 */
public class Database_Connector {

    public boolean connected;
    LocalDateTime run_time;
    public Connection con;

    private String user, password,name,ip;

    /**
     * Constructor with parameters
     * Parameters contains information needed to connection
     * @param user
     * @param password
     * @param name
     * @param ip
     */
    public Database_Connector(String user, String password,String name, String ip){
        this.user = user;
        this.password = password;
        this.name = name;
        this.ip = ip;
        connected = false;
        run_time = null;
    }

    /**
     * Function for checking if object has correct connection data
     * @return boolean
     */
    public boolean validate_object(){
        return !user.equals("") && !password.equals("") && !name.equals("") && !ip.equals("");
    }

    /**
     * Function for connecting service to local database
     */
    public void connect(){
        if ( user!= null && password != null && name != null && ip != null  ){
            String login_data = "jdbc:mysql://"+ip+"/"+name+"?"
                    + "useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&" +
                    "user="+user+"&password="+password;

            try{
                con = DriverManager.getConnection(login_data);
                run_time = LocalDateTime.now( ZoneId.of( "Europe/Warsaw" ) );
                connected = true;
            }catch(SQLException e){
                connected = false;
                System.out.println("Failed to connect to database ("+e.toString()+")");
            }
        }
        else{
            System.out.println("Failed to connect, configuration file is empty.");
        }
    }

    /**
     * Function for adding log to database
     * @param code
     * @param desc
     */
    public int log(String code,String desc){
        /**
         * CREATE TABLE SERIALL_APPLOG -- table for storing application log
         * (
         *     seriall_applog_id INT PRIMARY KEY AUTO_INCREMENT,
         *     seriall_applog_code VARCHAR(100),
         *     seriall_applog_desc TEXT,
         *     seriall_applog_time TIMESTAMP
         * );
         */
        String query = "INSERT INTO SERIALL_APPLOG (seriall_applog_code,seriall_applog_desc,seriall_applog_time) VALUES (?,?,?);";
        try{
            PreparedStatement ppst = con.prepareStatement(query);

            LocalDateTime ldt = LocalDateTime.now(ZoneId.of("Europe/Warsaw" ));

            ppst.setString(1,code);
            ppst.setString(2,desc);
            ppst.setObject(3,ldt);
            ppst.execute();
            return 1;
        }catch(SQLException ex){
            System.out.println("Failed to log data ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for adding data entry to database
     * @param serialcode
     * @return Integer
     */
    public int add_data_entry(String serialcode){
        /**
         * CREATE TABLE SERIALL_OBJECT -- table for storing data entry
         * (
         *     seriall_object_id INT PRIMARY KEY AUTO_INCREMENT,
         *     seriall_object_time TIMESTAMP,
         *     seriall_object_data VARCHAR(350)
         * );
         */
        String query = "INSERT INTO SERIALL_OBJECT (seriall_object_time,seriall_object_data) VALUES (?,?);";
        try{
            PreparedStatement ppst = con.prepareStatement(query);
            ppst.setObject(1,LocalDateTime.now(ZoneId.of("Europe/Warsaw")));
            ppst.setString(2,serialcode);
            ppst.execute();
            return 1;
        }catch(SQLException e){
            log("DATA-ENTRY-ADD-FAILED","Failed to add data entry ("+e.toString()+")");
            return -1;
        }
    }

    /**
     * Function for listing data entry to collection
     * @return ArrayList
     */
    public ArrayList<GridElement> list_data(){
        ArrayList<GridElement> data = new ArrayList<>();
        String query = "SELECT * FROM SERIALL_OBJECT;";
        try{
            PreparedStatement ppst = con.prepareStatement(query);
            ResultSet rs = ppst.executeQuery();
            while(rs.next()){
                data.add(new GridElement(rs.getString("seriall_object_data"),rs.getInt("seriall_object_id")
                        ,rs.getObject("seriall_object_time",LocalDateTime.class).toString()));
            }
        }catch(SQLException ex){
            log("LIST-DATA-FAILED","Failed to list data entry ("+ex.toString()+")");
        }
        return data;
    }

    /**
     * Function for listing session data
     * @return ArrayList
     */
    public ArrayList<GridElement> list_sessions(){
        ArrayList<GridElement> data = new ArrayList<>();
        String query = "SELECT * FROM SERIALL_SESSION";
        try{
            PreparedStatement ppst = con.prepareStatement(query);
            ResultSet rs = ppst.executeQuery();
            while(rs.next()){
                data.add(new GridElement(rs.getString("seriall_session_desc"),rs.getInt("seriall_session_id"),rs.getObject("seriall_session_time_start",LocalDateTime.class).toString()));
            }
        }catch(SQLException ex){
            log("LIST-SESSION-FAILED","Failed to list sessions ("+ex.toString()+")");
        }
        return data;
    }

    /**
     * Function for listing log data
     * @return ArrayList
     */
    public ArrayList<GridElement> list_log(){
        /**
         * CREATE TABLE SERIALL_APPLOG -- table for storing application log
         * (
         *     seriall_applog_id INT PRIMARY KEY AUTO_INCREMENT,
         *     seriall_applog_code VARCHAR(100),
         *     seriall_applog_desc TEXT,
         *     seriall_applog_time TIMESTAMP
         * );
         */
        ArrayList<GridElement> data = new ArrayList<>();
        String query = "SELECT * FROM SERIALL_APPLOG;";
        try{
            PreparedStatement ppst = con.prepareStatement(query);
            ResultSet rs = ppst.executeQuery();
            while(rs.next()){
                data.add(new GridElement(rs.getString("seriall_applog_desc"),rs.getInt("seriall_applog_id"),rs.getObject("seriall_applog_time",LocalDateTime.class).toString()));
            }
        }catch(SQLException ex){
            log("LOG-LIST-FAILED","Failed to list log ("+ex.toString()+")");
        }
        return data;
    }

    /**
     * Function for loading Session_Object object from database with given ID
     * @param seriall_session_id
     * @return Session_Object
     */
    public Session_Object get_session_obj(int seriall_session_id){
        String query = "SELECT seriall_session_obj FROM SERIALL_SESSION WHERE seriall_session_id = ?;";
        try{
            PreparedStatement ppst = con.prepareStatement(query);
            ppst.setInt(1,seriall_session_id);
            ResultSet rs = ppst.executeQuery();
            if (rs.next()){
                Blob blob = rs.getBlob("seriall_session_obj");
                byte byteArray[] = blob.getBytes(1,(int)blob.length());
                ByteArrayInputStream baip = new ByteArrayInputStream(byteArray);
                ObjectInputStream ois = new ObjectInputStream(baip);
                return (Session_Object) ois.readObject();
            }
            return null;
        }catch(Exception ex){
            log("GET-SESSION-OBJ-FAILED","Failed to get session object ("+ex.toString()+")");
            return null;
        }
    }

    /**
     * Function for adding serialobject to database
     * @param to_add
     * @return Integer
     */
    public int add_serialobject(Session_Object to_add){
        /**
         * CREATE TABLE SERIALL_SESSION -- table for storing program sessions
         * (
         * 	seriall_session_id INT PRIMARY KEY AUTO_INCREMENT,
         *     seriall_session_time_start TIMESTAMP,
         *     seriall_session_time_end TIMESTAMP,
         *     seriall_session_desc VARCHAR(300),
         *     seriall_session_obj LONGBLOB
         * );
         */
        String query = "INSERT INTO SERIALL_SESSION (seriall_session_time_start, seriall_session_time_end, seriall_session_desc,seriall_session_obj) VALUES (?,?,?,?);";
        try{
            PreparedStatement ppst = con.prepareStatement(query);
            ppst.setObject(1,to_add.start_time);
            ppst.setObject(2,LocalDateTime.now(ZoneId.of("Europe/Warsaw")));
            ppst.setString(3,to_add.get_desc());

            byte[] data = null;
            // saving data to bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(to_add);
            oos.flush();
            oos.close();
            baos.close();
            data = baos.toByteArray();
            ppst.setObject(4,data);
            ppst.execute();
            log("ADDED-SERIALL-OBJ","Added new seriall object to database!");
            return 1;
        }catch(Exception ex){
            log("ADDED-SERIALL-OBJ-FAILED","Failed to add new seriall object to database ("+ex.toString()+")");
            return -1;
        }
    }

}
