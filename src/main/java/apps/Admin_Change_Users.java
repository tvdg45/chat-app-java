//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Search_Company_Users;

import java.time.LocalDate;
import java.time.LocalTime;

import java.io.IOException;

import java.sql.Connection;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import utilities.Find_and_replace;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://tdscloud-dev-ed--c.visualforce.com", maxAge = 3600)
@RestController
@EnableAutoConfiguration
@RequestMapping("/admin-change-users")
public class Admin_Change_Users {
    
    @RequestMapping(method = RequestMethod.POST)
    String home(
            @RequestParam(value = "first_name", defaultValue = "") String first_name,
            @RequestParam(value = "last_name", defaultValue = "") String last_name,
            @RequestParam(value = "email", defaultValue = "") String email,
            @RequestParam(value = "user_id", defaultValue = "") String user_id,
            @RequestParam(value = "add_user", defaultValue = "") String add_user,
            @RequestParam(value = "change_user", defaultValue = "") String change_user,
            @RequestParam(value = "delete_user", defaultValue = "") String delete_user
    ) {
        
        Connection use_open_connection;
        
        try {
            
            use_open_connection = Config.openConnection();
            
            DateTimeFormatter time_format = DateTimeFormatter.ofPattern("hh:mm a 'EST'");
            
            LocalDate localDate = LocalDate.now();
            LocalTime localTime = LocalTime.now(ZoneId.of("America/New_York"));
            
            Control_Search_Company_Users.use_connection = use_open_connection;
            
            String first_name_exception = "";
            String last_name_exception = "";
            String email_exception = "";
            String user_id_exception = "";
            
            ArrayList<String> find = new ArrayList<>();
            ArrayList<String> replace = new ArrayList<>();
            
            find.add("(");
            find.add(")");
            find.add(", ");
            
            replace.add("");
            replace.add("");
            replace.add(",");
            
            try {
                
                String[] first_names = Find_and_replace.find_and_replace(find, replace, first_name).split(",");
                
                Control_Search_Company_Users.first_name = first_names;
            } catch (NullPointerException e) {
                
                first_name_exception = "yes";
            }
            
            try {
                
                String[] last_names = Find_and_replace.find_and_replace(find, replace, last_name).split(",");
                
                Control_Search_Company_Users.last_name = last_names;
            } catch (NullPointerException e) {
                
                last_name_exception = "yes";
            }
            
            try {
                
                String[] emails = Find_and_replace.find_and_replace(find, replace, email).split(",");
                
                Control_Search_Company_Users.email = emails;
            } catch (NullPointerException e) {
                
                email_exception = "yes";
            }
            
            try {
                
                String[] user_ids = Find_and_replace.find_and_replace(find, replace, user_id).split(",");
                
                Control_Search_Company_Users.user_id = user_ids;
            } catch (NullPointerException e) {
                
                user_id_exception = "yes";
            }
            
            Control_Search_Company_Users.date_received = String.valueOf(localDate);
            Control_Search_Company_Users.time_received = String.valueOf(time_format.format(localTime));
            
            if (add_user.equals("Add user") && !(first_name_exception.equals("yes")
                    || last_name_exception.equals("yes") || email_exception.equals("yes")
                    || user_id_exception.equals("yes"))) {
                
                Control_Search_Company_Users.control_add_user();
            }
            
            if (change_user.equals("Change user") && !(first_name_exception.equals("yes")
                    || last_name_exception.equals("yes") || email_exception.equals("yes")
                    || user_id_exception.equals("yes"))) {
                
                Control_Search_Company_Users.control_change_user();
            }
            
            if (delete_user.equals("Delete user") && !(first_name_exception.equals("yes")
                    || last_name_exception.equals("yes") || email_exception.equals("yes")
                    || user_id_exception.equals("yes"))) {
                
                Control_Search_Company_Users.control_delete_user();
            }
            
            return "";
        } catch (IOException e) {
            
            return "";
        }
    }
	
    public static void main(String[] args) throws Exception, IOException {
		
        SpringApplication.run(Admin_Change_Users.class, args);
    }
}
