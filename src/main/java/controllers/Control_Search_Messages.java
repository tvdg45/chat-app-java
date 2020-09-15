//Author: Timothy van der Graaff
package controllers;

import views.Show_Messages;
import java.sql.Connection;

public class Control_Search_Messages extends models.Search_Messages {
    
	//global variables
    public static Connection use_connection;
    public static String conversation_owner;
    
    public static String request_messages() {
        
        String output = "";
        
        connection = use_connection;
        
        set_conversation_owner(conversation_owner);
        
        if (!(search_messages().get(0).get(0).equals("fail"))
                && !(search_messages().get(0).get(0).equals("no message"))) {
            
            Show_Messages.conversation_owner = conversation_owner;
            Show_Messages.messages = search_messages();
            
            output = Show_Messages.show_messages();
        }
        
        Control_Search_Company_Users.use_connection = use_connection;
        
        if (Control_Search_Company_Users.control_search_company_user_status(conversation_owner).equals(
                "Available - online")
            || Control_Search_Company_Users.control_search_company_user_status(conversation_owner).equals(
                "Not available")) {
            
            output = "log user out";
        }
        
        return output;
    }
    
    public static void control_delete_my_messages() {
        
        connection = use_connection;
        
        set_conversation_owner(conversation_owner);
        
        delete_my_messages();
    }
}