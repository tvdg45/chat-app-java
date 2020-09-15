//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Search_Company_Users;
import utilities.Form_Validation;
import java.io.IOException;

import java.sql.Connection;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://tdscloud-dev-ed--c.visualforce.com", maxAge = 3600)
@RestController
@EnableAutoConfiguration
@RequestMapping("/admin-change-user-status")
public class Admin_Change_User_Status {
    
    @RequestMapping(method = RequestMethod.POST)
    String home(
            @RequestParam(value = "status", defaultValue = "") String status,
            @RequestParam(value = "admin_session", defaultValue = "") String admin_session,
            @RequestParam(value = "change_status", defaultValue = "") String change_status
    ) {
        
        Connection use_open_connection;
        
        try {
            
            use_open_connection = Config.openConnection();
            
            Control_Search_Company_Users.use_connection = use_open_connection;
            Control_Search_Company_Users.admin_session = admin_session;
            Control_Search_Company_Users.status = status;
            
            if (change_status.equals("Change status")
                    && !(Form_Validation.is_string_null_or_white_space(admin_session))) {
                
                return Control_Search_Company_Users.control_change_status();
            } else {
                
                return "";
            }
        } catch (IOException e) {
            
            return "";
        }
    }
	
    public static void main(String[] args) throws Exception, IOException {
		
        SpringApplication.run(Admin_Change_User_Status.class, args);
    }
}
