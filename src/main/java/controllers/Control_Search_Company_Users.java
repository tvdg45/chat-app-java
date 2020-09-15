//Author: Timothy van der Graaff
package controllers;

import java.sql.Connection;
import utilities.Form_Validation;
import views.Show_Company_Users;

public class Control_Search_Company_Users extends models.Search_Company_Users {
    
	//global variables
    public static Connection use_connection;
    public static String admin_session;
    public static String status;
    public static String[] first_name;
    public static String[] last_name;
    public static String[] email;
    public static String[] user_id;
    public static String date_received;
    public static String time_received;
    
    //This method show users that are available to chat.
    public static String control_search_company_users() {
        
        String output;
        
        connection = use_connection;
        
        if (!(search_company_users().get(0).get(0).equals("fail"))
                && !(search_company_users().get(0).get(0).equals("no users"))) {
            
            Show_Company_Users.company_users = search_company_users();
            
            output = Show_Company_Users.show_company_users();
        } else {
            
            output = Show_Company_Users.show_company_users_sorry_message();
        }
        
        return output;
    }
    
    //This method checks to make sure that a particular user is available to chat.
    public static String control_search_company_users(String conversation_owner) {
        
        String output = "";
        
        if (!(Form_Validation.is_string_null_or_white_space(conversation_owner))) {
            
            connection = use_connection;

            if (!(search_company_users().get(0).get(0).equals("fail"))
                    && !(search_company_users().get(0).get(0).equals("no users"))) {
            
                Show_Company_Users.company_users = search_company_users();
            
                output = Show_Company_Users.show_company_users(conversation_owner);
                
                if (!(output.equals(""))) {
                    
                    update_user_status(conversation_owner);
                }
            }
        }
        
        return output;
    }
    
    //Find a status of a user, in order to determine how a conversation thread should print.
    //If an administrator is available offline, a guest can still talk with that administrator.
    //However, that guest can only see his/her own messages.
    //If an administrator is not available, this means that a conversation is taking place.
    //If an administrator is available online, this means a guest can start a conversation with
    //the administrator messages also being shown.
    public static String control_search_company_user_status(String conversation_owner) {
        
        String output = "";
        
        if (!(Form_Validation.is_string_null_or_white_space(conversation_owner))) {
            
            connection = use_connection;

            if (!(search_company_users().get(0).get(0).equals("fail"))
                    && !(search_company_users().get(0).get(0).equals("no users"))) {
            
                Show_Company_Users.company_users = search_company_users();
            
                output = Show_Company_Users.show_company_user_status(conversation_owner);
            }
        }
        
        return output;
    }
    
    public static String control_change_status() {
        
        String output = "";
        
        connection = use_connection;
        
        if (!(status.equals("Choose"))) {
            
            if (!(update_user_status(admin_session, status).equals("fail"))) {
                
                output = Show_Company_Users.show_change_status(admin_session);
            }
        } else {
            
            output = Show_Company_Users.show_change_status_sorry_message();
        }
        
        return output;
    }
    
    public static void control_add_user() {
        
        connection = use_connection;
        
        set_first_name(first_name);
        set_last_name(last_name);
        set_email(email);
        set_user_id(user_id);
        set_date_received(date_received);
        set_time_received(time_received);
        
        add_user();
    }
    
    public static void control_change_user() {
        
        connection = use_connection;
        
        set_first_name(first_name);
        set_last_name(last_name);
        set_email(email);
        set_user_id(user_id);
        
        change_user();
    }
    
    public static void control_delete_user() {
        
        connection = use_connection;
        
        set_user_id(user_id);
        
        delete_user();
    }
}