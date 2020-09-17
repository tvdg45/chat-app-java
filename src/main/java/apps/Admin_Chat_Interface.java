//Author: Timothy van der Graaff
package apps;

import java.io.IOException;

import java.sql.Connection;
import configuration.Config;
import utilities.Form_Validation;
import controllers.Control_Search_Company_Users;
import javax.servlet.http.*;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://tdscloud-dev-ed--c.visualforce.com", maxAge = 3600)
@RestController
@EnableAutoConfiguration
@RequestMapping("/admin-chat-interface")
public class Admin_Chat_Interface {
    
    @RequestMapping(method = RequestMethod.POST)
    String home(
			HttpServletRequest request,
			HttpServletResponse response,
            @RequestParam(value = "admin_session", defaultValue = "") String admin_session
    ) {
        
        Connection use_open_connection;
        
        try {
            
            use_open_connection = Config.openConnection();
            
            String guest_full_name = "";
            String guest_session = "";
            String conversation_owner = "";
            String user_status;
            String return_string = "";
            
            return_string += "<label>If page does not load, click <a href=\"https://tdscloud-dev-ed.lightning.force.com/lightning/page/home\">here</a> to refresh.</label><br /><br />\n";

            //Attempt to find a logged in guest.
            try {
                
                Cookie each_cookie[] = request.getCookies();
                
                if (each_cookie.length == 3) {
                    
                    guest_full_name = each_cookie[0].getValue();
                    guest_session = each_cookie[1].getValue();
                    conversation_owner = each_cookie[2].getValue();
                } else {
                    
                    Cookie cookie_guest_full_name = new Cookie("guest_full_name", "");
                    
                    cookie_guest_full_name.setMaxAge(0);
                    
                    response.addCookie(cookie_guest_full_name);
                    
                    Cookie cookie_guest_session = new Cookie("guest_session", "");
                    
                    cookie_guest_session.setMaxAge(0);
                    
                    response.addCookie(cookie_guest_session);
                    
                    Cookie cookie_conversation_owner = new Cookie("conversation_owner", "");
                    
                    cookie_conversation_owner.setMaxAge(0);
                    
                    response.addCookie(cookie_conversation_owner);
                    
                    return_string += "<script type=\"text/javascript\">\n";
                    return_string += "window.location = document.location.href.replace(\"#\", \"\");\n";
                    return_string += "</script>";
                }
            } catch (NullPointerException e) {
                
                if (e.getMessage() == null) {
                    
                    guest_full_name = "";
                    guest_session = "";
                    conversation_owner = "";
                }
            }
            
            return_string += "<script type=\"text/javascript\" src=\"https://www.timothysdigitalsolutions.com/backstretch/js/jquery-3.2.1.js\"></script>\n";
            return_string += "<script type=\"text/javascript\" src=\"https://www.timothysdigitalsolutions.com/backstretch/js/jquery.min.js\"></script>\n";
            return_string += "<script type=\"text/javascript\" src=\"https://www.timothysdigitalsolutions.com/backstretch/js/jquery.backstretch.js\"></script>\n";
            return_string += "<script type=\"text/javascript\" src=\"https://chat-app-node-1.herokuapp.com/socket.io/socket.io.js\"></script>\n";
            
            if (!(admin_session.equals("")) || (!(guest_full_name.equals("")) && !(guest_session.equals(""))
                    && !(conversation_owner.equals("")))) {
                
                return_string += "<script type=\"text/javascript\">\n";
                return_string += "function check_html(html) {\n\n";
                return_string += "var doc = document.createElement('div');\n\n";
                return_string += "doc.innerHTML = html;\n\n";
                return_string += "return (doc.innerHTML === html);\n";
                return_string += "}\n\n";
                return_string += "var number_of_submissions = 0;\n\n";
                return_string += "setInterval(function() {\n\n";
                return_string += "number_of_submissions = 0;\n";
                return_string += "}, 1000);\n\n";
                return_string += "function send_message() {\n\n";
                return_string += "number_of_submissions++;\n\n";
                return_string += "document.getElementById(\"message\").innerHTML = " +
                        "document.getElementById(\"message\").innerHTML.replace(/<div><br><\\/div><div><br><\\/div>/g, \"\");\n\n";
                return_string += "if (number_of_submissions <= 1 " +
                        "&& document.getElementById(\"message\").innerHTML != \"\" " +
                        "&& document.getElementById(\"message\").innerHTML.replace(/<div><br><\\/div><div><br><\\/div>/g, \"\").length > 0 " +
                        "&& document.getElementById(\"message\").innerHTML.replace(/\\s/g, \"\").length > 0) {\n\n";
                return_string += "var xhttp = new XMLHttpRequest();\n\n";
                return_string += "xhttp.onreadystatechange = function() {\n\n";
                return_string += "if (this.readyState == 4 && this.status == 200) {\n\n";
                return_string += "$(\"#send_message\").html(this.responseText);\n";
                return_string += "}\n";
                return_string += "};\n\n";
                return_string += "xhttp.open(\"POST\", \"" + Config.third_party_domain() + "/admin-create-message\");\n";
                return_string += "xhttp.setRequestHeader(\"Content-type\", \"application/x-www-form-urlencoded\");\n\n";
                return_string += "var message = \"\";\n\n";
                return_string += "if (!(check_html(document.getElementById(\"message\").innerHTML.replace(/<a/g, \"<a target=\\\"_blank\\\"\")))) {\n\n";
                return_string += "message = document.getElementById(\"message\").innerHTML.replace(/<a/g, \"<a target=\\\"_blank\\\"\").replace(/</g, \"&lt;\").replace(/>/g, \"&gt;\");\n";
                return_string += "} else {\n\n";
                return_string += "message = document.getElementById(\"message\").innerHTML.replace(/<a/g, \"<a target=\\\"_blank\\\"\");\n";
                return_string += "}\n";
                
                if (!(Form_Validation.is_string_null_or_white_space(admin_session))) {
                   
                    return_string += "xhttp.send(\"message=\" + message + \"&admin_session=" + admin_session + "&create_message=Send\");\n\n";
                } else {
                    
                    return_string += "xhttp.send(\"message=\" + message + \"&create_message=Send\");\n\n";
                }
                
                return_string += "document.getElementById(\"message\").innerHTML = \"\";\n";
                return_string += "}\n";
                return_string += "}\n\n";
                return_string += "var socket = io.connect(\"https://chat-app-node-1.herokuapp.com\");\n\n";
                return_string += "window.onload = function() {\n\n";
                return_string += "var xhttp = new XMLHttpRequest();\n\n";
                return_string += "xhttp.onreadystatechange = function() {\n\n";
                return_string += "if (this.readyState == 4 && this.status == 200) {\n\n";
                return_string += "var compare_socket_data = \"\";\n\n";
                
                if (!(Form_Validation.is_string_null_or_white_space(admin_session))) {
                    
                    return_string += "compare_socket_data = \"" + admin_session + "\";\n\n";
                } else {
                    
                    return_string += "compare_socket_data = \"" + conversation_owner + "\";\n\n";
                }
                
                return_string += "if (this.responseText != \"\" && this.responseText.replace(/\\s/g, \"\").length != 0 && this.responseText != \"[object XMLDocument]\") {\n\n";
                return_string += "var live_thread;\n";
                return_string += "var thread_content = \"\";\n\n";
                return_string += "live_thread = JSON.parse(this.responseText.replace(\", {}\", \"\"));\n\n";
                
                if (Form_Validation.is_string_null_or_white_space(admin_session)) {
                    
                    return_string += "if (live_thread[0][\"user_id\"] == \"log user out\") {\n\n";
                    return_string += "window.location = document.location.href.replace(\"#\", \"\");\n";
                    return_string += "}\n\n";
                }
                
                return_string += "for (var i = 0; i < live_thread.length; i++) {\n\n";
                
                if (!(Form_Validation.is_string_null_or_white_space(admin_session))) {
                    
                    return_string += "if (live_thread[i][\"conversation_owner\"] == \"" + admin_session + "\") {\n\n";
                    return_string += "thread_content += \"<div style='text-align: left; padding-top: 20px; " +
                            "padding-bottom: 20px; word-wrap: break-word'>\";\n";
                    return_string += "thread_content += \"<div style='text-align: left; width: 100%'>" +
                            "<label style='font-size: 12pt'><b>\" + live_thread[i][\"full_name\"] + \":</b> \";\n\n";
                    return_string += "if (!(check_html(live_thread[i][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\")))) {\n\n";
                    return_string += "thread_content += live_thread[i][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\").replace(/</g, \"&lt;\").replace(/>/g, \"&gt;\");\n";
                    return_string += "} else {\n\n";
                    return_string += "thread_content += live_thread[i][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\");\n";
                    return_string += "}\n\n";
                    return_string += "thread_content += \"</label><br /><br />\";\n";
                    return_string += "thread_content += \"</div>\";\n";
                    return_string += "thread_content += \"<div style='text-align: left; width: 100%'>" +
                            "<label style='font-size: 12pt'><b>On \" + live_thread[i][\"date_received\"] + \" at " +
                            "\" + live_thread[i][\"time_received\"] + \"</b></label>\";\n";
                    return_string += "thread_content += \"</div>\";\n";
                    return_string += "thread_content += \"</div>\";\n";
                    return_string += "}\n";
                } else {
                    
                    Control_Search_Company_Users.use_connection = use_open_connection;
                    
                    user_status = Control_Search_Company_Users.control_search_company_user_status(conversation_owner);
                    
                    if (!(user_status.equals("Available - online"))) {
                        
                        if (user_status.equals("Available - offline")) {
                            
                            
                        }
                    }
                }
            }
            
            return return_string;
        } catch (IOException e) {
            
            return "";
        }
    }
	
    public static void main(String[] args) throws Exception, IOException {
		
        SpringApplication.run(Admin_Chat_Interface.class, args);
    }
}
