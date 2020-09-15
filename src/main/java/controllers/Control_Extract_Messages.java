//Author: Timothy van der Graaff
package controllers;

import views.Show_Messages;
import java.sql.Connection;

public class Control_Extract_Messages extends models.Search_Messages {
    
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
        
        return output;
    }
}