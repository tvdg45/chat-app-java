//Author: Timothy van der Graaff
package views;

import java.util.ArrayList;
import utilities.Form_Validation;
import utilities.Find_and_replace;

public class Show_Live_Message_Update {
    
    public static String admin_session;
    public static String conversation_owner;
    public static String guest_session;
    public static String admin_full_name;
    public static String guest_full_name;
    public static String message;
    public static String date_received;
    public static String time_received;
    
    public static String show_live_message_update() {
        
        String output = "";
		
        ArrayList find = new ArrayList<>();
        ArrayList replace = new ArrayList<>();
        
        find.add("<script");
        find.add("<style");
        find.add("\"");
        find.add("'");
        find.add("<br />");
        find.add("<br>");
        find.add("<div>");
        find.add("</div>");
        
        replace.add("&lt;script");
        replace.add("&lt;style");
        replace.add("&quot;");
        replace.add("&apos;");
        replace.add(" ");
        replace.add("");
        replace.add("");
        replace.add("");
        
        output += "var message = \"\";\n\n";
        
        if (!(Form_Validation.is_string_null_or_white_space(admin_session))
            || !(Form_Validation.is_string_null_or_white_space(conversation_owner))) {
            
            output += "$(document).ready(function() {\n\n";
            
            if (!(Form_Validation.is_string_null_or_white_space(admin_session))) {
                
                output += "message = [";
                output += "{\"user_id\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, admin_session.replaceAll("[\r\n]+", " ").replaceAll("<", "&lt;").replaceAll(">", "&gt;")) + "\", " +
                    "\"full_name\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, admin_full_name.replaceAll("[\r\n]+", " ").replaceAll("<", "&lt;").replaceAll(">", "&gt;")) + "\", " +
                    "\"conversation_owner\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, admin_session.replaceAll("[\r\n]+", " ").replaceAll("<", "&lt;").replaceAll(">", "&gt;")) + "\", " +
                    "\"message\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, message.replaceAll("[\r\n]+", " ")) + "\", " +
                    "\"date_received\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, date_received.replaceAll("[\r\n]+", " ").replaceAll("<", "&lt;").replaceAll(">", "&gt;")) + "\", " +
                    "\"time_received\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, time_received.replaceAll("[\r\n]+", " ").replaceAll("<", "&lt;").replaceAll(">", "&gt;")) + "\"} ";
                
                output += "];\n\n";
            } else {
               
                output += "message = [";
                output += "{\"user_id\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, guest_session.replaceAll("[\r\n]+", " ").replaceAll("<", "&lt;").replaceAll(">", "&gt;")) + "\", " +
                    "\"full_name\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, guest_full_name.replaceAll("[\r\n]+", " ").replaceAll("<", "&lt;").replaceAll(">", "&gt;")) + "\", " +
                    "\"conversation_owner\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, conversation_owner.replaceAll("[\r\n]+", " ").replaceAll("<", "&lt;").replaceAll(">", "&gt;")) + "\", " +
                    "\"message\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, message.replaceAll("[\r\n]+", " ")) + "\", " +
                    "\"date_received\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, date_received.replaceAll("[\r\n]+", " ").replaceAll("<", "&lt;").replaceAll(">", "&gt;")) + "\", " +
                    "\"time_received\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, time_received.replaceAll("[\r\n]+", " ").replaceAll("<", "&lt;").replaceAll(">", "&gt;")) + "\"} ";
                
                output += "];\n\n";
            }
            
            output += "socket.emit('load_threads', message);\n";
            output += "});\n\n";
        }
        
        return output;
    }
}