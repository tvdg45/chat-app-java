//Author: Timothy van der Graaff
package views;

import java.util.ArrayList;
import utilities.Find_and_replace;

public class Show_Messages {
    
    public static String conversation_owner;
    public static ArrayList<ArrayList<String>> messages;
    
    public static String show_messages() {
        
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
        
        output += "[";
        
        for (int i = 0; i < messages.get(0).size(); i++) {
            
            output += "{\"user_id\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, messages.get(0).get(i).replaceAll("[\r\n]+", " ").replaceAll("<", "&lt;").replaceAll(">", "&gt;")) +
                    "\", \"full_name\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, messages.get(1).get(i).replaceAll("[\r\n]+", " ").replaceAll("<", "&lt;").replaceAll(">", "&gt;")) +
                    "\", \"conversation_owner\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, messages.get(2).get(i).replaceAll("[\r\n]+", " ").replaceAll("<", "&lt;").replaceAll(">", "&gt;")) +
                    "\", \"message\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, messages.get(3).get(i).replaceAll("[\r\n]+", " ")) +
                    "\", \"date_received\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, messages.get(4).get(i).replaceAll("[\r\n]+", " ").replaceAll("<", "&lt;").replaceAll(">", "&gt;")) +
                    "\", \"time_received\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, messages.get(5).get(i).replaceAll("[\r\n]+", " ").replaceAll("<", "&lt;").replaceAll(">", "&gt;")) +
                    "\"}, ";
        }
        
        output += "{}]";
        
        return output;
    }
}