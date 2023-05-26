/**
 * by Jakub Wawak
 * kubawawak@gmail.com / j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.database;

import usp.jakubwawak.seriall.SeriallApplication;

/**
 * Object for maintaing reconnection to database
 */
public class Database_ConnectorUpdater implements Runnable{

    int time;

    /**
     * Constructor
     * @param time
     */
    public Database_ConnectorUpdater(int time){
        SeriallApplication.database.log("DB-RECONNECT","Trying to reconnect to database!");
        this.time = time;
    }
    @Override
    public void run() {
        try{
            while(true){
                if (SeriallApplication.database != null){
                    if ( SeriallApplication.database.validate_object() ){
                        SeriallApplication.database.connect();
                        if ( SeriallApplication.database.connected ){
                            SeriallApplication.database.log("DB-RECONNECT","Reconnected successfully!");
                        }
                        else{
                            SeriallApplication.database.log("DB-RECONNECT-FAILED","Failed to reconnect application! Check log!");
                        }
                    }
                }
                Thread.sleep(time);
            }
        }catch(Exception ex){
            SeriallApplication.database.log("THREAD-RECONNECT-FAILED","Failed to auto reconnect database ("+ex.toString()+")");
        }


    }
}
