//Author: Timothy van der Graaff
package models;

import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Search_Company_Users {
    
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public static Connection connection;
    
    //global variables
    private static String[] first_name;
    private static String[] last_name;
    private static String[] email;
    private static String[] user_id;
    private static String date_received;
    private static String time_received;
    
    //mutators
    protected static void set_first_name(String[] this_first_name) {
        
        first_name = this_first_name;
    }
    
    protected static void set_last_name(String[] this_last_name) {
        
        last_name = this_last_name;
    }
    
    protected static void set_email(String[] this_email) {
        
        email = this_email;
    }
    
    protected static void set_user_id(String[] this_user_id) {
        
        user_id = this_user_id;
    }
    
    protected static void set_date_received(String this_date_received) {
        
        date_received = this_date_received;
    }
    
    protected static void set_time_received(String this_time_received) {
        
        time_received = this_time_received;
    }
    
    //accessors
    private static String[] get_first_name() {
        
        return first_name;
    }
    
    private static String[] get_last_name() {
        
        return last_name;
    }
    
    private static String[] get_email() {
        
        return email;
    }
    
    private static String[] get_user_id() {
        
        return user_id;
    }
    
    private static String get_date_received() {
        
        return date_received;
    }
    
    private static String get_time_received() {
        
        return time_received;
    }
    
    
    
    
    private static void create_new_table() {
        
        try {
            
            PreparedStatement create_statement = connection.prepareStatement(
                    
                    "CREATE TABLE company_users (row_id INT NOT NULL, first_name " +
                            "TEXT NOT NULL, last_name TEXT NOT NULL, email_address TEXT NOT NULL, user_id " +
                            "TEXT NOT NULL, status TEXT NOT NULL, date_received TEXT NOT NULL, time_received " +
                            "TEXT NOT NULL, PRIMARY KEY (row_id)) ENGINE = MYISAM;");
            
            create_statement.execute();
        } catch (SQLException e) {

            LOGGER.log(Level.INFO, "The 'company_users' " +
                    "table was not created because it already exists.  " +
                    "This is not necessarily an error.");
        }
    }
    
    private static int generate_row_id() {
        
        int output;
        
        try {
            
            PreparedStatement prepared_statement = connection.prepareStatement("SELECT row_id FROM " +
                    "company_users ORDER BY row_id DESC LIMIT 1");
            
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
    
    protected static ArrayList<ArrayList<String>> search_company_users() {
        
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        
        ArrayList<String> each_first_name = new ArrayList<>();
        ArrayList<String> each_last_name = new ArrayList<>();
        ArrayList<String> each_email_address = new ArrayList<>();
        ArrayList<String> each_user_id = new ArrayList<>();
        ArrayList<String> each_status = new ArrayList<>();
        
        int user_count = 0;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            select_statement = connection.prepareStatement("SELECT first_name, last_name, email_address, " +
                    "user_id, status FROM company_users ORDER BY row_id DESC");
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
               
                each_first_name.add(select_results.getString(1));
                each_last_name.add(select_results.getString(2));
                each_email_address.add(select_results.getString(3));
                each_user_id.add(select_results.getString(4));
                each_status.add(select_results.getString(5));
                
                user_count++;
            }
            
            if (user_count == 0) {
                
                each_first_name.add("no users");
                each_last_name.add("no users");
                each_email_address.add("no users");
                each_user_id.add("no users");
                each_status.add("no users");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_users' " +
                    "table is corrupt or does not exist");
            
            create_new_table();
            
            each_first_name.add("fail");
            each_last_name.add("fail");
            each_email_address.add("fail");
            each_user_id.add("fail");
            each_status.add("fail");
        }
        
        output.add(each_first_name);
        output.add(each_last_name);
        output.add(each_email_address);
        output.add(each_user_id);
        output.add(each_status);
        
        return output;
    }
    
    protected static void update_user_status(String conversation_owner) {
        
        PreparedStatement update_statement;
        
        try {
            
            update_statement = connection.prepareStatement("UPDATE company_users SET status = ? " +
                    "WHERE user_id = ? AND NOT status = ?");
            
            update_statement.setString(1, "Occupied");
            update_statement.setString(2, conversation_owner);
            update_statement.setString(3, "Available - offline");
            
            update_statement.addBatch();
            
            update_statement.executeBatch();
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_users' " +
                    "table is corrupt or does not exist");
            
            create_new_table();
        }        
    }
    
    protected static String update_user_status(String conversation_owner, String status) {
        
        String output = "";
        
        PreparedStatement update_statement;
        
        try {
            
            update_statement = connection.prepareStatement("UPDATE company_users SET status = ? " +
                    "WHERE user_id = ?");
            
            update_statement.setString(1, status);
            update_statement.setString(2, conversation_owner);
            
            update_statement.addBatch();
            
            update_statement.executeBatch();
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_users' " +
                    "table is corrupt or does not exist");
            
            create_new_table();
            
            output = "fail";
        }
        
        return output;
    }
    
    protected static void add_user() {
        
        ArrayList<ArrayList<String>> search_company_users = search_company_users();
        int records_to_insert = 0;
        int row_id;
        
        PreparedStatement insert_statement;
        
        try {
            
            row_id = generate_row_id();
            
            insert_statement = connection.prepareStatement("INSERT INTO " +
                    "company_users (row_id, first_name, last_name, email_address, user_id, status, " +
                    "date_received, time_received) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
            
            for (int i = 0; i < get_user_id().length; i++) {
                
                if (!(search_company_users.get(3).contains(get_user_id()[i]))
                        && !(search_company_users.get(2).contains(get_email()[i]))) {
                
                    insert_statement.setInt(1, row_id);
                    insert_statement.setString(2, get_first_name()[i]);
                    insert_statement.setString(3, get_last_name()[i]);
                    insert_statement.setString(4, get_email()[i]);
                    insert_statement.setString(5, get_user_id()[i]);
                    insert_statement.setString(6, "Not available");
                    insert_statement.setString(7, get_date_received());
                    insert_statement.setString(8, get_time_received());
                    
                    insert_statement.addBatch();
                    
                    records_to_insert++;
                    row_id++;
                }
            }
            
            if (records_to_insert > 0) {
                
                insert_statement.executeBatch();
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_users' " +
                    "table is corrupt or does not exist");
            
            create_new_table();
        }
    }
    
    protected static void change_user() {
        
        int records_to_update = 0;
        
        PreparedStatement update_statement;
        
        try {
            
            update_statement = connection.prepareStatement("UPDATE company_users SET first_name = ?, " +
                    "last_name = ?, email_address = ? WHERE user_id = ?");
            
            for (int i = 0; i < get_user_id().length; i++) {
                
                update_statement.setString(1, get_first_name()[i]);
                update_statement.setString(2, get_last_name()[i]);
                update_statement.setString(3, get_email()[i]);
                update_statement.setString(4, get_user_id()[i]);
                    
                update_statement.addBatch();
                    
                records_to_update++;
            }
            
            if (records_to_update > 0) {
                
                update_statement.executeBatch();
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_users' " +
                    "table is corrupt or does not exist");
            
            create_new_table();
        }        
    }
    
    protected static void delete_user() {
        
        int records_to_delete = 0;
        
        PreparedStatement delete_statement;
        
        try {
            
            delete_statement = connection.prepareStatement("DELETE FROM company_users WHERE user_id = ?");
            
            for (int i = 0; i < get_user_id().length; i++) {
                
                delete_statement.setString(1, get_user_id()[i]);
                    
                delete_statement.addBatch();
                    
                records_to_delete++;
            }
            
            if (records_to_delete > 0) {
                
                delete_statement.executeBatch();
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_users' " +
                    "table is corrupt or does not exist");
            
            create_new_table();
        }        
    }
}