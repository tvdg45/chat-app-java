//Author: Timothy van der Graaff
package models;

import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Search_Messages {
    
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public static Connection connection;
    
    //global variables
    private static String conversation_owner;
    
    //mutators
    protected static void set_conversation_owner(String this_conversation_owner) {
        
        conversation_owner = this_conversation_owner;
    }
    
    //accessors
    private static String get_conversation_owner() {
        
        return conversation_owner;
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
    
    protected static ArrayList<ArrayList<String>> search_messages() {
       
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        
        ArrayList<String> each_user_id = new ArrayList<>();
        ArrayList<String> each_full_name = new ArrayList<>();
        ArrayList<String> each_owner_id = new ArrayList<>();
        ArrayList<String> each_message = new ArrayList<>();
        ArrayList<String> each_date_received = new ArrayList<>();
        ArrayList<String> each_time_received = new ArrayList<>();
        
        int message_count = 0;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {

            select_statement = connection.prepareStatement("SELECT user_id, full_name, " +
                    "owner_id, message, date_received, time_received FROM company_instant_chat_messages " +
                    "WHERE owner_id = ? ORDER BY row_id ASC");
            
            select_statement.setString(1, get_conversation_owner());
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                each_user_id.add(select_results.getString(1));
                each_full_name.add(select_results.getString(2));
                each_owner_id.add(select_results.getString(3));
                each_message.add(select_results.getString(4));
                each_date_received.add(select_results.getString(5));
                each_time_received.add(select_results.getString(6));
                
                message_count++;
            }
            
            if (message_count == 0) {
                
                each_user_id.add("no message");
                each_full_name.add("no message");
                each_owner_id.add("no message");
                each_message.add("no message");
                each_date_received.add("no message");
                each_time_received.add("no message");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_instant_chat_messages' " +
                    "table is corrupt or does not exist");
            
            create_new_table();
            
            each_user_id.add("fail");
            each_full_name.add("fail");
            each_owner_id.add("fail");
            each_message.add("fail");
            each_date_received.add("fail");
            each_time_received.add("fail");
        }
        
        output.add(each_user_id);
        output.add(each_full_name);
        output.add(each_owner_id);
        output.add(each_message);
        output.add(each_date_received);
        output.add(each_time_received);
        
        return output;
    }
    
    protected static void delete_my_messages() {
        
        PreparedStatement delete_statement;
        
        try {

            delete_statement = connection.prepareStatement("DELETE FROM company_instant_chat_messages " +
                    "WHERE owner_id = ? ORDER BY row_id ASC");
            
            delete_statement.setString(1, get_conversation_owner());
            
            delete_statement.addBatch();
            
            delete_statement.executeBatch();
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_instant_chat_messages' " +
                    "table is corrupt or does not exist");
            
            create_new_table();
        }        
    }
}
