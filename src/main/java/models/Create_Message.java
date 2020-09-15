//Author: Timothy van der Graaff
package models;

import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import utilities.Form_Validation;

public abstract class Create_Message {
    
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public static Connection connection;
    
    //global variables
    private static String guest_full_name;
    private static String message;
    private static String date_received;
    private static String time_received;
    private static String conversation_owner;
    private static String guest_session;
    private static String admin_session;
    
    //mutators
    protected static void set_guest_full_name(String this_guest_full_name) {
        
        guest_full_name = this_guest_full_name;
    }
    
    protected static void set_message(String this_message) {
        
        message = this_message;
    }
    
    protected static void set_date_received(String this_date_received) {
        
        date_received = this_date_received;
    }
    
    protected static void set_time_received(String this_time_received) {
        
        time_received = this_time_received;
    }
    
    protected static void set_conversation_owner(String this_conversation_owner) {
        
        conversation_owner = this_conversation_owner;
    }
    
    protected static void set_guest_session(String this_guest_session) {
        
        guest_session = this_guest_session;
    }
    
    protected static void set_admin_session(String this_admin_session) {
        
        admin_session = this_admin_session;
    }
    
    //accessors
    private static String get_guest_full_name() {
       
        return guest_full_name;
    }
    
    private static String get_message() {
        
        return message;
    }
    
    private static String get_date_received() {
        
        return date_received;
    }
    
    private static String get_time_received() {
        
        return time_received;
    }
    
    private static String get_conversation_owner() {
        
        return conversation_owner;
    }
    
    private static String get_guest_session() {
        
        return guest_session;
    }
    
    private static String get_admin_session() {
        
        return admin_session;
    }
    
    
    
    
    
    private static int generate_row_id() {
        
        int output;
        
        try {
            
            PreparedStatement prepared_statement = connection.prepareStatement("SELECT row_id FROM " +
                    "company_instant_chat_messages ORDER BY row_id DESC LIMIT 1");
            
            ResultSet select_results = prepared_statement.executeQuery();
            
            select_results.last();
            
            if (select_results.getRow() > 0) {
               
                output = select_results.getInt(1);
            } else {
                
                output = 0;
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "{0}", e);
            
            output = 0;
        }
        
        return output + 1;
    }
    
    private static void create_new_table() {
        
        try {
            
            PreparedStatement create_statement = connection.prepareStatement(
                    
                    "CREATE TABLE company_instant_chat_messages (row_id INT NOT NULL, user_id " +
                            "TEXT NOT NULL, full_name TEXT NOT NULL, owner_id TEXT NOT NULL, message " +
                            "TEXT NOT NULL, date_received TEXT NOT NULL, time_received TEXT NOT NULL, " +
                            "PRIMARY KEY (row_id)) ENGINE = MYISAM;");
            
            create_statement.execute();
        } catch (SQLException e) {

            LOGGER.log(Level.INFO, "The 'company_instant_chat_messages' " +
                    "table was not created because it already exists.  " +
                    "This is not necessarily an error.");
        }
    }
    
    protected static ArrayList<String> search_admin_full_name(String admin_session) {
        
        ArrayList<String> output = new ArrayList<>();
        
        try {
            
            PreparedStatement select_statement = connection.prepareStatement("SELECT first_name, last_name " +
                    "FROM company_users WHERE user_id = ? ORDER BY row_id DESC");
            
            select_statement.setString(1, admin_session);
            
            ResultSet select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                output.add(select_results.getString(1) + " " + select_results.getString(2));
            }
            
            if (output.isEmpty()) {
                
                output.add("Anonymous");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_users' " +
                    "table is corrupt or does not exist");
            
            output.add("Anonymous");
        }
        
        return output;
    }
    
    protected static String add_instant_chat_message() {
        
        String output;
        
        try {
            
            PreparedStatement insert_statement = connection.prepareStatement("INSERT INTO " +
                    "company_instant_chat_messages (row_id, user_id, full_name, owner_id, message, " +
                    "date_received, time_received) VALUES(?, ?, ?, ?, ?, ?, ?)");
            
            insert_statement.setInt(1, generate_row_id());
            
            if (!(Form_Validation.is_string_null_or_white_space(get_admin_session()))) {
                
                insert_statement.setString(2, get_admin_session());
                insert_statement.setString(3, search_admin_full_name(get_admin_session()).get(0));
                insert_statement.setString(4, get_admin_session());
            } else {
                
                insert_statement.setString(2, get_guest_session());
                insert_statement.setString(3, get_guest_full_name());
                insert_statement.setString(4, get_conversation_owner());
            }
            
            insert_statement.setString(5, get_message());
            insert_statement.setString(6, get_date_received());
            insert_statement.setString(7, get_time_received());
            
            insert_statement.addBatch();
            
            insert_statement.executeBatch();
            
            output = "success";
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_instant_chat_messages' " +
                    "table is corrupt or does not exist");
            
            create_new_table();
            
            output = "fail";
        }
        
        return output;
    }
}