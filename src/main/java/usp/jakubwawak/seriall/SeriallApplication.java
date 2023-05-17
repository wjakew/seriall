/**
 * by Jakub Wawak
 * kubawawak@gmail.com / j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.seriall;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import usp.jakubwawak.database.Database_Connector;
import usp.jakubwawak.database_object.Session_Object;
import usp.jakubwawak.ui.views.HomeView;

import java.io.Console;
import java.util.ArrayList;
import java.util.Scanner;

@SpringBootApplication
@EnableVaadin({"usp.jakubwawak"})
public class SeriallApplication {

	public static String version = "v1.0.1";
	public static String build = "SLL-170523REV1";

	public static Database_Connector database;

	public static Session_Object current_session;

	public static HomeView current_layout;

	/**
	 * Main Application Function
	 * @param args
	 */
	public static void main(String[] args) {
		show_header();
		current_session = new Session_Object();
		if ( args.length == 0){
			prepare_database_connection(args);
		}
		else if (args.length == 4){
			// String user, String password,String name, String ip
			// localhost seriall_database admin password
			database = new Database_Connector(args[2],args[3],args[1],args[0]);
			System.out.println("Logged with ip: "+args[0]+",db name: "+args[1]+", user: "+args[2]);
			database.connect();
			if (database.connected){
				database.log("APPLICATION-CONNECTION","Connected to database!");
				SpringApplication.run(SeriallApplication.class, args);
			}
		}

	}

	/**
	 * Function for preparing database connection
	 */
	public static void prepare_database_connection(String[] args){
		Scanner scanner = new Scanner(System.in);
		System.out.println("Welcome to seriall!");
		System.out.println("Database Connector Manager");
		System.out.print("database ip?");
		String ip = scanner.nextLine();
		System.out.print("database name?");
		String name = scanner.nextLine();
		System.out.print("database user login?");
		String user = scanner.nextLine();
		char[] ch;
		try{
			Console cnsl = System.console();
			ch = cnsl.readPassword("database password?");
		}catch(Exception e){
			System.out.print("database password?");
			String user_raw_password = scanner.nextLine();
			ch = user_raw_password.toCharArray();
		}

		if ( !user.equals("") && !name.equals("") && !String.copyValueOf(ch).equals("") && !ip.equals("") ){
			database = new Database_Connector(user,String.copyValueOf(ch),name,ip);
			database.connect();
			if (database.connected){
				database.log("APPLICATION-CONNECTION","Connected to database!");
				SpringApplication.run(SeriallApplication.class, args);
			}
		}
	}

	/**
	 * Function for showing header
	 */
	public static void show_header(){
		String header = "               _       _ _ \n" +
				" ___  ___ _ __(_) __ _| | |\n" +
				"/ __|/ _ \\ '__| |/ _` | | |\n" +
				"\\__ \\  __/ |  | | (_| | | |\n" +
				"|___/\\___|_|  |_|\\__,_|_|_|\n";
		header = header + "\n"+"by Jakub Wawak "+version+"/"+build;
	}

}
