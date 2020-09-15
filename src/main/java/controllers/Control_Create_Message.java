//Author: Timothy van der Graaff
package controllers;

import utilities.Form_Validation;
import java.sql.Connection;
import java.sql.SQLException;
import views.Show_Live_Message_Update;

public class Control_Create_Message extends models.Create_Message {
    
	//global variables
    public static String guest_full_name;
    public static String message;
    public static String date_received;
    public static String time_received;
    public static String conversation_owner;
    public static String guest_session;
    public static String admin_session;
    public static String create_message;
    public static Connection use_connection;
    
    public static String control_add_instant_chat_message() {
        
        String output = "";
        
        if (create_message.equals("Send")) {
            
            if (!(Form_Validation.is_string_null_or_white_space(message))) {
                
                connection = use_connection;
                
                set_guest_full_name(guest_full_name);
                set_message(message);
                set_date_received(date_received);
                set_time_received(time_received);
                set_conversation_owner(conversation_owner);
                set_guest_session(guest_session);
                set_admin_session(admin_session);
                
                if (add_instant_chat_message().equals("success")) {
                    
                    Show_Live_Message_Update.admin_session = admin_session;
                    Show_Live_Message_Update.conversation_owner = conversation_owner;
                    Show_Live_Message_Update.guest_session = guest_session;
                    
                    if (!(Form_Validation.is_string_null_or_white_space(admin_session))) {
                        
                        Show_Live_Message_Update.admin_full_name = search_admin_full_name(admin_session).get(0);
                    }
                    
                    Show_Live_Message_Update.guest_full_name = guest_full_name;
                    Show_Live_Message_Update.message = message;
                    Show_Live_Message_Update.date_received = date_received;
                    Show_Live_Message_Update.time_received = time_received;
                    
                    output = Show_Live_Message_Update.show_live_message_update();
                    
                    try {
                        
                        use_connection.close();
                    } catch (SQLException e) {
                    }
                }
            }
        }
        
        return output;
    }
}
