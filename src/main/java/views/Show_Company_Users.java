//Author: Timothy van der Graaff
package views;

import java.util.ArrayList;
import utilities.Form_Validation;
import utilities.Find_and_replace;

public class Show_Company_Users {
    
    public static ArrayList<ArrayList<String>> company_users;
    
    public static String show_company_users() {
       
        String output = "";
        
        ArrayList find = new ArrayList<>();
        ArrayList replace = new ArrayList<>();
        
        int user_count = 0;
        
        find.add("<");
        find.add(">");
        
        replace.add("&lt;");
        replace.add("&gt;");
        
        for (int i = 0; i < company_users.get(0).size(); i++) {
            
            if (company_users.get(4).get(i).equals("Available - offline")
                    || company_users.get(4).get(i).equals("Available - online")) {
                
                output += "<div style=\"text-align: left; padding-top: 20px; padding-bottom: 20px; " +
                        "word-wrap: break-word\">\n";
                output += "<div style=\"text-align: left\">\n";
                output += "<label><b>" + Find_and_replace.find_and_replace(find, replace, company_users.get(0).get(i)) +
                        " " + Find_and_replace.find_and_replace(find, replace, company_users.get(1).get(i)) +
                        "</b></label><br />\n";
                output += "</div>\n";
                output += "<div style=\"text-align: left\"><br />\n";
                output += "<input type=\"button\" class=\"select_person\" value=\"Chat now\"" +
                        " onclick=\"select_person('" +
                        Find_and_replace.find_and_replace(find, replace, company_users.get(3).get(i)) + "')\" />\n";
                output += "</div>\n";
                output += "</div>\n";
                
                user_count++;
            }
        }
        
        if (user_count == 0) {
            
            output = show_company_users_sorry_message();
        }
        
        return output;
    }
    
    public static String show_company_users(String conversation_owner) {
       
        String output = "";
        
        ArrayList find = new ArrayList<>();
        ArrayList replace = new ArrayList<>();
        
        int user_count = 0;
        
        find.add("<");
        find.add(">");
        
        replace.add("&lt;");
        replace.add("&gt;");
        
        for (int i = 0; i < company_users.get(0).size(); i++) {
            
            if (company_users.get(3).get(i).equals(conversation_owner)
                && (company_users.get(4).get(i).equals("Available - offline")
                || company_users.get(4).get(i).equals("Available - online"))) {
                
                output = Find_and_replace.find_and_replace(find, replace, company_users.get(4).get(i));
                
                user_count++;
            }
        }
        
        if (user_count != 1) {
            
            output = "";
        }
        
        return output;
    }
    
    public static String show_company_user_status(String conversation_owner) {
       
        String output = "";
        
        ArrayList find = new ArrayList<>();
        ArrayList replace = new ArrayList<>();
        
        int user_count = 0;
        
        find.add("<");
        find.add(">");
        
        replace.add("&lt;");
        replace.add("&gt;");
        
        for (int i = 0; i < company_users.get(0).size(); i++) {
            
            if (company_users.get(3).get(i).equals(conversation_owner)) {
                
                output = Find_and_replace.find_and_replace(find, replace, company_users.get(4).get(i));
                
                user_count++;
            }
        }
        
        if (user_count != 1) {
            
            output = "";
        }
        
        return output;
    }
    
    public static String show_company_users_sorry_message() {
        
        String output;
        
        output = "<label>At this time, no one is available for a chat session." +
                " Please wait until someone is available.</label>\n";
        
        return output;
    }
    
    public static String show_change_status_sorry_message() {
        
        String output;
        
        output = "<label><span style=\"color: red\">Please choose a user status.</span></label>\n";
        
        return output;
    }
    
    public static String show_change_status(String conversation_owner) {
        
        String output = "";

        if (!(Form_Validation.is_string_null_or_white_space(conversation_owner))) {
            
            output += "<script type=\"text/javascript\">\n";
            output += "$(document).ready(function() {\n\n";
            output += "socket.emit('log_other_users_out', '" + conversation_owner + "');\n\n";
            output += "window.location = document.location.href.replace(\"#\", \"\");\n";
            output += "});\n\n";
            output += "</script>\n";
        }
        
        return output;
    }
}
