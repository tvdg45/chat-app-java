//Author: Timothy van der Graaff
package apps;

import java.io.IOException;

import java.sql.Connection;
import configuration.Config;
import utilities.Form_Validation;
import controllers.Control_Search_Company_Users;
import java.io.PrintWriter;
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
    
    @RequestMapping(method = RequestMethod.GET)
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
		
        response.getWriter().println("<label>If page does not load, click <a href=\"https://tdscloud-dev-ed.lightning.force.com/lightning/page/home\">here</a> to refresh.</label><br /><br />");
        
        //Attempt to find a logged in administrator.
        if (request.getParameter("admin_session") == null) {
      
            admin_session = "";
        } else {
      
            admin_session = request.getParameter("admin_session");
        }
  
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
                
                response.getWriter().println("<script type=\"text/javascript\">");
                response.getWriter().println("window.location = document.location.href.replace(\"#\", \"\");");
                response.getWriter().println("</script>");
            } 
        } catch (NullPointerException e) {
      
            if (e.getMessage() == null) {
        
                guest_full_name = "";
                guest_session = "";
                conversation_owner = "";
            }
        }
        
        response.getWriter().println("<script type=\"text/javascript\" src=\"https://www.timothysdigitalsolutions.com/backstretch/js/jquery-3.2.1.js\"></script>");
        response.getWriter().println("<script type=\"text/javascript\" src=\"https://www.timothysdigitalsolutions.com/backstretch/js/jquery.min.js\"></script>");
        response.getWriter().println("<script type=\"text/javascript\" src=\"https://www.timothysdigitalsolutions.com/backstretch/js/jquery.backstretch.js\"></script>");
        response.getWriter().println("<script type=\"text/javascript\" src=\"https://chat-app-node-1.herokuapp.com/socket.io/socket.io.js\"></script>");
        
        if (!(admin_session.equals("")) || (!(guest_full_name.equals("")) && !(guest_session.equals(""))
            && !(conversation_owner.equals("")))) {
            
            response.getWriter().println("<script type=\"text/javascript\">");
            response.getWriter().println("function check_html(html) {\n" +
                    "	\n" +
                    "	var doc = document.createElement('div');\n" +
                    "	\n" +
                    "	doc.innerHTML = html;\n" +
                    "	\n" +
                    "	return (doc.innerHTML === html);\n" +
                    "}");
            response.getWriter().println("");
            response.getWriter().println("var number_of_submissions = 0;");
            response.getWriter().println("");
            response.getWriter().println("setInterval(function() {");
            response.getWriter().println("");
            response.getWriter().println("number_of_submissions = 0;");
            response.getWriter().println("}, 1000);");
            response.getWriter().println("");
            response.getWriter().println("function send_message() {");
            response.getWriter().println("");
            response.getWriter().println("number_of_submissions++;");
            response.getWriter().println("");
            response.getWriter().println("document.getElementById(\"message\").innerHTML = " +
                    "document.getElementById(\"message\").innerHTML.replace(/<div><br><\\/div><div><br><\\/div>/g, \"\");");
            response.getWriter().println("");
            response.getWriter().println("if (number_of_submissions <= 1 " +
                    "&& document.getElementById(\"message\").innerHTML != \"\" " +
                    "&& document.getElementById(\"message\").innerHTML.replace(/<div><br><\\/div><div><br><\\/div>/g, \"\").length > 0 " +
                    "&& document.getElementById(\"message\").innerHTML.replace(/\\s/g, \"\").length > 0) {");
            response.getWriter().println("");
            response.getWriter().println("var xhttp = new XMLHttpRequest();");
            response.getWriter().println("");
            response.getWriter().println("xhttp.onreadystatechange = function() {");
            response.getWriter().println("");
            response.getWriter().println("if (this.readyState == 4 && this.status == 200) {");
            response.getWriter().println("");
            response.getWriter().println("$(\"#send_message\").html(this.responseText);");
            response.getWriter().println("}");
            response.getWriter().println("};");
            response.getWriter().println("");
            response.getWriter().println("xhttp.open(\"POST\", \"" + Config.third_party_domain() + "/admin-create-message\");");
            response.getWriter().println("xhttp.setRequestHeader(\"Content-type\", \"application/x-www-form-urlencoded\");");
            response.getWriter().println("");
            response.getWriter().println("var message = \"\";");
            response.getWriter().println("");
            response.getWriter().println("if (!(check_html(document.getElementById(\"message\").innerHTML.replace(/<a/g, \"<a target=\\\"_blank\\\"\")))) {");
            response.getWriter().println("");
            response.getWriter().println("message = document.getElementById(\"message\").innerHTML.replace(/<a/g, \"<a target=\\\"_blank\\\"\").replace(/</g, \"&lt;\").replace(/>/g, \"&gt;\");");
            response.getWriter().println("} else {");
            response.getWriter().println("");
            response.getWriter().println("message = document.getElementById(\"message\").innerHTML.replace(/<a/g, \"<a target=\\\"_blank\\\"\");");
            response.getWriter().println("}");
            
            if (!(Form_Validation.is_string_null_or_white_space(admin_session))) {
                
                response.getWriter().println("xhttp.send(\"message=\" + message + \"&admin_session=" + admin_session + "&create_message=Send\");");
            } else {
                
                response.getWriter().println("xhttp.send(\"message=\" + message + \"&create_message=Send\");");
            }
            
            response.getWriter().println("");
            response.getWriter().println("document.getElementById(\"message\").innerHTML = \"\";");
            response.getWriter().println("}");
            response.getWriter().println("}");
            response.getWriter().println("");
            response.getWriter().println("var socket = io.connect(\"https://chat-app-node-1.herokuapp.com\");");
            response.getWriter().println("");
            response.getWriter().println("window.onload = function() {");
            response.getWriter().println("");
            response.getWriter().println("var xhttp = new XMLHttpRequest();");
            response.getWriter().println("");
            response.getWriter().println("xhttp.onreadystatechange = function() {");
            response.getWriter().println("");
            response.getWriter().println("if (this.readyState == 4 && this.status == 200) {");
            response.getWriter().println("");
            response.getWriter().println("var compare_socket_data = \"\";");
            response.getWriter().println("");
            
            if (!(Form_Validation.is_string_null_or_white_space(admin_session))) {
                
                response.getWriter().println("compare_socket_data = \"" + admin_session + "\";");
            } else {
                
                response.getWriter().println("compare_socket_data = \"" + conversation_owner + "\";");
            }
            
            response.getWriter().println("");
            response.getWriter().println("if (this.responseText != \"\" && this.responseText.replace(/\\s/g, \"\").length != 0 && this.responseText != \"[object XMLDocument]\") {");
            response.getWriter().println("");
            response.getWriter().println("var live_thread;");
            response.getWriter().println("var thread_content = \"\";");
            response.getWriter().println("");
            response.getWriter().println("live_thread = JSON.parse(this.responseText.replace(\", {}\", \"\"));");
            response.getWriter().println("");
            
            if (Form_Validation.is_string_null_or_white_space(admin_session)) {
                
                response.getWriter().println("if (live_thread[0][\"user_id\"] == \"log user out\") {");
                response.getWriter().println("");
                response.getWriter().println("window.location = document.location.href.replace(\"#\", \"\");");
                response.getWriter().println("}");
            }
            
            response.getWriter().println("");
            response.getWriter().println("for (var i = 0; i < live_thread.length; i++) {");
            response.getWriter().println("");
            
            if (!(Form_Validation.is_string_null_or_white_space(admin_session))) {

                response.getWriter().println("if (live_thread[i][\"conversation_owner\"] == \"" + admin_session + "\") {");
                response.getWriter().println("");
                response.getWriter().println("thread_content += \"<div style='text-align: left; padding-top: 20px; " +
                        "padding-bottom: 20px; word-wrap: break-word'>\";");
                response.getWriter().println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                        "<label style='font-size: 12pt'><b>\" + live_thread[i][\"full_name\"] + \":</b> \";");
                response.getWriter().println("");
                response.getWriter().println("if (!(check_html(live_thread[i][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\")))) {");
                response.getWriter().println("");
                response.getWriter().println("thread_content += live_thread[i][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\").replace(/</g, \"&lt;\").replace(/>/g, \"&gt;\");");
                response.getWriter().println("} else {");
                response.getWriter().println("");
                response.getWriter().println("thread_content += live_thread[i][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\");");
                response.getWriter().println("}");
                response.getWriter().println("");
                response.getWriter().println("thread_content += \"</label><br /><br />\";");
                response.getWriter().println("thread_content += \"</div>\";");
                response.getWriter().println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                        "<label style='font-size: 12pt'><b>On \" + live_thread[i][\"date_received\"] + \" at " +
                        "\" + live_thread[i][\"time_received\"] + \"</b></label>\";");
                response.getWriter().println("thread_content += \"</div>\";");
                response.getWriter().println("thread_content += \"</div>\";");
                response.getWriter().println("}");
            } else {
                
                Control_Search_Company_Users.use_connection = use_open_connection;
                
                user_status = Control_Search_Company_Users.control_search_company_user_status(conversation_owner);
                
                if (!(user_status.equals("Available - online"))) {
                    
                    if (user_status.equals("Available - offline")) {
                        
                        response.getWriter().println("if (live_thread[i][\"user_id\"] == \"" + guest_session + "\" && " +
                                "live_thread[i][\"conversation_owner\"] == \"" + conversation_owner + "\") {");
                        response.getWriter().println("");
                        response.getWriter().println("thread_content += \"<div style='text-align: left; padding-top: 20px; " +
                                "padding-bottom: 20px; word-wrap: break-word'>\";");
                        response.getWriter().println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                                "<label style='font-size: 12pt'><b>\" + live_thread[i][\"full_name\"] + \":</b> \";");
                        response.getWriter().println("");
                        response.getWriter().println("if (!(check_html(live_thread[i][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\")))) {");
                        response.getWriter().println("");
                        response.getWriter().println("thread_content += live_thread[i][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\").replace(/</g, \"&lt;\").replace(/>/g, \"&gt;\");");
                        response.getWriter().println("} else {");
                        response.getWriter().println("");
                        response.getWriter().println("thread_content += live_thread[i][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\");");
                        response.getWriter().println("}");
                        response.getWriter().println("");
                        response.getWriter().println("thread_content += \"</label><br /><br />\";");
                        response.getWriter().println("thread_content += \"</div>\";");
                        response.getWriter().println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                                "<label style='font-size: 12pt'><b>On \" + live_thread[i][\"date_received\"] + \" at " +
                                "\" + live_thread[i][\"time_received\"] + \"</b></label>\";");
                        response.getWriter().println("thread_content += \"</div>\";");
                        response.getWriter().println("thread_content += \"</div>\";");
                        response.getWriter().println("}");                        
                    } else {
                        
                        response.getWriter().println("if (live_thread[i][\"conversation_owner\"] == \"" + conversation_owner + "\") {");
                        response.getWriter().println("");
                        response.getWriter().println("thread_content += \"<div style='text-align: left; padding-top: 20px; " +
                                "padding-bottom: 20px; word-wrap: break-word'>\";");
                        response.getWriter().println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                                "<label style='font-size: 12pt'><b>\" + live_thread[i][\"full_name\"] + \":</b> \";");
                        response.getWriter().println("");
                        response.getWriter().println("if (!(check_html(live_thread[i][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\")))) {");
                        response.getWriter().println("");
                        response.getWriter().println("thread_content += live_thread[i][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\").replace(/</g, \"&lt;\").replace(/>/g, \"&gt;\");");
                        response.getWriter().println("} else {");
                        response.getWriter().println("");
                        response.getWriter().println("thread_content += live_thread[i][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\");");
                        response.getWriter().println("}");
                        response.getWriter().println("");
                        response.getWriter().println("thread_content += \"</label><br /><br />\";");
                        response.getWriter().println("thread_content += \"</div>\";");
                        response.getWriter().println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                                "<label style='font-size: 12pt'><b>On \" + live_thread[i][\"date_received\"] + \" at " +
                                "\" + live_thread[i][\"time_received\"] + \"</b></label>\";");
                        response.getWriter().println("thread_content += \"</div>\";");
                        response.getWriter().println("thread_content += \"</div>\";");
                        response.getWriter().println("}");                        
                    }
                }                
            }
            
            response.getWriter().println("}");
            response.getWriter().println("");
            response.getWriter().println("$(\"#thread\").html(thread_content);");
            response.getWriter().println("document.getElementById(\"conversation\").scrollTop = document.getElementById(\"conversation\").scrollHeight - document.getElementById(\"conversation\").clientHeight;");
            response.getWriter().println("} else {");
            response.getWriter().println("");
            response.getWriter().println("$(\"#thread\").html(\"\");");
            response.getWriter().println("}");
            response.getWriter().println("}");
            response.getWriter().println("};");
            response.getWriter().println("");
            response.getWriter().println("xhttp.open(\"POST\", \"" + Config.third_party_domain() + "/admin-search-messages\");");
            response.getWriter().println("xhttp.setRequestHeader(\"Content-type\", \"application/x-www-form-urlencoded\");");
            response.getWriter().println("");
            
            if (!(Form_Validation.is_string_null_or_white_space(admin_session))) {
                
                response.getWriter().println("xhttp.send(\"conversation_owner=" + admin_session + "&admin_session=" + admin_session + "\");");
            } else {
                
                response.getWriter().println("xhttp.send(\"conversation_owner=" + conversation_owner + "\");");
            }
            
            response.getWriter().println("}");
            
            if (!(Form_Validation.is_string_null_or_white_space(conversation_owner))
                && Form_Validation.is_string_null_or_white_space(admin_session)) {
                
                response.getWriter().println("");
                response.getWriter().println("socket.on('log_other_users_out', function(data) {");
                response.getWriter().println("");
                response.getWriter().println("if (data == \"" + conversation_owner + "\") {");
                response.getWriter().println("");
                response.getWriter().println("window.location = document.location.href.replace(\"#\", \"\");");
                response.getWriter().println("");
                response.getWriter().println("log_out();");
                response.getWriter().println("}");
                response.getWriter().println("});");
            }
            
            if (!(Form_Validation.is_string_null_or_white_space(admin_session))) {

                response.getWriter().println("");
                response.getWriter().println("socket.on('refresh_admin_window', function(data) {");
                response.getWriter().println("");
                response.getWriter().println("if (data == \"" + admin_session + "\") {");
                response.getWriter().println("");
                response.getWriter().println("window.location = document.location.href.replace(\"#\", \"\");");
                response.getWriter().println("}");
                response.getWriter().println("});");
            }
            
            response.getWriter().println("");
            response.getWriter().println("socket.on('load_threads', function(data) {");
            response.getWriter().println("");
            response.getWriter().println("var live_thread;");
            response.getWriter().println("var thread_content = \"\";");
            response.getWriter().println("");
            response.getWriter().println("live_thread = data;");
            response.getWriter().println("");
            
            Control_Search_Company_Users.use_connection = use_open_connection;
            
            user_status = Control_Search_Company_Users.control_search_company_user_status(conversation_owner);
            
            if (!(Form_Validation.is_string_null_or_white_space(admin_session))) {
                
                response.getWriter().println("if (live_thread[0][\"conversation_owner\"] == \"" + admin_session + "\") {");
                response.getWriter().println("");
                response.getWriter().println("thread_content += \"<div style='text-align: left; padding-top: 20px; " +
                        "padding-bottom: 20px; word-wrap: break-word'>\";");
                response.getWriter().println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                        "<label style='font-size: 12pt'><b>\" + live_thread[0][\"full_name\"] + \":</b> \";");
                response.getWriter().println("");
                response.getWriter().println("if (!(check_html(live_thread[0][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\")))) {");
                response.getWriter().println("");
                response.getWriter().println("thread_content += live_thread[0][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\").replace(/</g, \"&lt;\").replace(/>/g, \"&gt;\");");
                response.getWriter().println("} else {");
                response.getWriter().println("");
                response.getWriter().println("thread_content += live_thread[0][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\");");
                response.getWriter().println("}");
                response.getWriter().println("");
                response.getWriter().println("thread_content += \"</label><br /><br />\";");
                response.getWriter().println("thread_content += \"</div>\";");
                response.getWriter().println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                        "<label style='font-size: 12pt'><b>On \" + live_thread[0][\"date_received\"] + \" at " +
                        "\" + live_thread[0][\"time_received\"] + \"</b></label>\";");
                response.getWriter().println("thread_content += \"</div>\";");
                response.getWriter().println("thread_content += \"</div>\";");
                response.getWriter().println("}");
                response.getWriter().println("");
                response.getWriter().println("$(\"#thread\").append(thread_content);");
            } else {

                switch (user_status) {
                    case "Available - offline":
                        
                        response.getWriter().println("if (live_thread[0][\"user_id\"] == \"" + guest_session + "\" && " +
                                "live_thread[0][\"conversation_owner\"] == \"" + conversation_owner + "\") {");
                        response.getWriter().println("");
                        response.getWriter().println("thread_content += \"<div style='text-align: left; padding-top: 20px; " +
                                "padding-bottom: 20px; word-wrap: break-word'>\";");
                        response.getWriter().println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                        "<label style='font-size: 12pt'><b>\" + live_thread[0][\"full_name\"] + \":</b> \";");
                        response.getWriter().println("");
                        response.getWriter().println("if (!(check_html(live_thread[0][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\")))) {");
                        response.getWriter().println("");
                        response.getWriter().println("thread_content += live_thread[0][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\").replace(/</g, \"&lt;\").replace(/>/g, \"&gt;\");");
                        response.getWriter().println("} else {");
                        response.getWriter().println("");
                        response.getWriter().println("thread_content += live_thread[0][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\");");
                        response.getWriter().println("}");
                        response.getWriter().println("");
                        response.getWriter().println("thread_content += \"</label><br /><br />\";");
                        response.getWriter().println("thread_content += \"</div>\";");
                        response.getWriter().println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                                "<label style='font-size: 12pt'><b>On \" + live_thread[0][\"date_received\"] + \" at " +
                                "\" + live_thread[0][\"time_received\"] + \"</b></label>\";");
                        response.getWriter().println("thread_content += \"</div>\";");
                        response.getWriter().println("thread_content += \"</div>\";");
                        response.getWriter().println("}");
                        response.getWriter().println("");
                        response.getWriter().println("$(\"#thread\").append(thread_content);");
                        
                        break;
                    case "Occupied":
                        
                        response.getWriter().println("if (live_thread[0][\"conversation_owner\"] == \"" + conversation_owner + "\") {");
                        response.getWriter().println("");
                        response.getWriter().println("thread_content += \"<div style='text-align: left; padding-top: 20px; " +
                                "padding-bottom: 20px; word-wrap: break-word'>\";");
                        response.getWriter().println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                        "<label style='font-size: 12pt'><b>\" + live_thread[0][\"full_name\"] + \":</b> \";");
                        response.getWriter().println("");
                        response.getWriter().println("if (!(check_html(live_thread[0][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\")))) {");
                        response.getWriter().println("");
                        response.getWriter().println("thread_content += live_thread[0][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\").replace(/</g, \"&lt;\").replace(/>/g, \"&gt;\");");
                        response.getWriter().println("} else {");
                        response.getWriter().println("");
                        response.getWriter().println("thread_content += live_thread[0][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\");");
                        response.getWriter().println("}");
                        response.getWriter().println("");
                        response.getWriter().println("thread_content += \"</label><br /><br />\";");
                        response.getWriter().println("thread_content += \"</div>\";");
                        response.getWriter().println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                                "<label style='font-size: 12pt'><b>On \" + live_thread[0][\"date_received\"] + \" at " +
                                "\" + live_thread[0][\"time_received\"] + \"</b></label>\";");
                        response.getWriter().println("thread_content += \"</div>\";");
                        response.getWriter().println("thread_content += \"</div>\";");
                        response.getWriter().println("}");
                        response.getWriter().println("");
                        response.getWriter().println("$(\"#thread\").append(thread_content);");
                        
                        break;
                }
            }
            
            response.getWriter().println("");
            response.getWriter().println("document.getElementById(\"conversation\").scrollTop = document.getElementById(\"conversation\").scrollHeight - document.getElementById(\"conversation\").clientHeight;");
            response.getWriter().println("});");
            response.getWriter().println("");
            response.getWriter().println("function log_out() {");
            response.getWriter().println("");
            response.getWriter().println("var xhttp = new XMLHttpRequest();");
            response.getWriter().println("");
            response.getWriter().println("xhttp.onreadystatechange = function() {");
            response.getWriter().println("");
            response.getWriter().println("if (this.readyState == 4 && this.status == 200) {");
            response.getWriter().println("");
            response.getWriter().println("$(\"#conversation\").html(this.responseText);");
            response.getWriter().println("}");
            response.getWriter().println("};");
            response.getWriter().println("");
            response.getWriter().println("xhttp.open(\"POST\", \"" + Config.third_party_domain() + "/log-out\");");
            response.getWriter().println("xhttp.setRequestHeader(\"Content-type\", \"application/x-www-form-urlencoded\");");
            response.getWriter().println("");
            response.getWriter().println("xhttp.send(\"log_out=Log out\");");
            response.getWriter().println("}");
            response.getWriter().println("");
            response.getWriter().println("function clear_message() {");
            response.getWriter().println("");
            response.getWriter().println("var default_message = document.getElementById(\"message\").innerHTML;");
            response.getWriter().println("");
            response.getWriter().println("if (default_message == \"<label>message</label>\") {");
            response.getWriter().println("");
            response.getWriter().println("document.getElementById(\"message\").innerHTML = \"\";");
            response.getWriter().println("}");
            response.getWriter().println("}");
            response.getWriter().println("");
            response.getWriter().println("function default_message() {");
            response.getWriter().println("");
            response.getWriter().println("var default_message = document.getElementById(\"message\").innerHTML;");
            response.getWriter().println("");
            response.getWriter().println("if (default_message == \"\") {");
            response.getWriter().println("");
            response.getWriter().println("document.getElementById(\"message\").innerHTML = \"<label>message</label>\";");
            response.getWriter().println("}");
            response.getWriter().println("}");
            response.getWriter().println("</script>");
            response.getWriter().println("<div id=\"conversation\" style=\"text-align: left; min-height: 350px; max-height: 350px; overflow: auto; width: 100%\">");
            response.getWriter().println("<div id=\"thread\" style=\"text-align: left; width: 100%\"></div>");
            response.getWriter().println("</div>");
            response.getWriter().println("<div style=\"text-align: left; width: 100%\"><br />");
            response.getWriter().println("<div id=\"message\" contenteditable=\"true\" onfocusout=\"default_message()\" onfocus=\"clear_message()\" style=\"width: 100%; overflow: auto; min-height: 100px; max-height: 100px\"><label>message</label></div>");
            response.getWriter().println("<br /><br /><input id=\"create_message\" type=\"button\" value=\"Send\" onclick=\"send_message()\" />");
            
            if (Form_Validation.is_string_null_or_white_space(admin_session)) {
            
                response.getWriter().println("<input id=\"sign_out\" type=\"button\" value=\"Leave chat\" onclick=\"log_out()\" />");
            } else {
                
                Control_Search_Company_Users.use_connection = use_open_connection;
                
                if (!(Control_Search_Company_Users.control_search_company_user_status(admin_session).equals("Occupied"))) {
                    
                    response.getWriter().println("");
                    response.getWriter().println("<script type=\"text/javascript\">");
                    response.getWriter().println("document.getElementById(\"message\").innerHTML = \"<label>message</label>\";");
                    response.getWriter().println("document.getElementById(\"message\").contentEditable = false;");
                    response.getWriter().println("document.getElementById(\"create_message\").disabled = true;");
                    response.getWriter().println("</script>");
                }
            }
            
            response.getWriter().println("</div>");
            
            if (!(Form_Validation.is_string_null_or_white_space(admin_session))) {
                
                response.getWriter().println("<div style=\"text-align: left; width: 100%\"><br />");
                response.getWriter().println("<label>Current status: " +
                        Control_Search_Company_Users.control_search_company_user_status(admin_session) + "</label>");
                response.getWriter().println("</div>");
                response.getWriter().println("<div style=\"text-align: left; width: 100%\"><br />");
                response.getWriter().println("<label>Change status:</label>");
                response.getWriter().println("<select id=\"status\" style=\"width: 98%\">");
                response.getWriter().println("<option value=\"Choose\">Choose</option>");
                response.getWriter().println("<option value=\"Available - online\">Available - online</option>");
                response.getWriter().println("<option value=\"Available - offline\">Available - offline</option>");
                response.getWriter().println("<option value=\"Not available\">Not available</option>");
                response.getWriter().println("</select>");
                response.getWriter().println("</div>");
                response.getWriter().println("<div style=\"text-align: left; width: 100%\"><br />");
                response.getWriter().println("<input type=\"button\" id=\"change_status\" value=\"Change status\" onclick=\"change_user_status()\" /><br /><br />");
                response.getWriter().println("</div>");
                response.getWriter().println("<script type=\"text/javascript\">");
                response.getWriter().println("");
                response.getWriter().println("function change_user_status() {");
                response.getWriter().println("");
                response.getWriter().println("var xhttp = new XMLHttpRequest();");
                response.getWriter().println("");
                response.getWriter().println("xhttp.onreadystatechange = function() {");
                response.getWriter().println("");
                response.getWriter().println("if (this.readyState == 4 && this.status == 200) {");
                response.getWriter().println("");
                response.getWriter().println("$(\".change_status\").html(this.responseText);");
                response.getWriter().println("}");
                response.getWriter().println("};");
                response.getWriter().println("");
                response.getWriter().println("xhttp.open(\"POST\", \"" + Config.third_party_domain() + "/admin-change-user-status\");");
                response.getWriter().println("xhttp.setRequestHeader(\"Content-type\", \"application/x-www-form-urlencoded\");");
                response.getWriter().println("");
                response.getWriter().println("xhttp.send(\"admin_session=" + 
                        admin_session + "&status=\" + document.getElementById(\"status\").value + " +
                        "\"&change_status=Change status\");");
                response.getWriter().println("}");
                response.getWriter().println("</script>");
            }
            
            response.getWriter().println("<div class=\"change_status\" style=\"text-align: left; width: 100%\"></div>");
            response.getWriter().println("<div id=\"send_message\" style=\"text-align: left; width: 100%\"></div>");
        } else {
            
            response.getWriter().println("<div style=\"text-align: left; width: 100%\">");
            response.getWriter().println("<div id=\"step_one\" style=\"text-align: left; width: 100%; display: none\">");
            response.getWriter().println("<div style=\"text-align: left; width: 100%\">");
            response.getWriter().println("<label>What is your name?</label>");
            response.getWriter().println("</div>");
            response.getWriter().println("<div style=\"text-align: left; width: 100%\">");
            response.getWriter().println("<input type=\"text\" id=\"full_name\" style=\"width: 98%\" />");
            response.getWriter().println("</div>");
            response.getWriter().println("<div style=\"text-align: left; width: 100%\">");
            response.getWriter().println("<br />");
            response.getWriter().println("<input type=\"checkbox\" id=\"no_name\" value=\"Anonymous\" onchange=\"exclude_full_name()\" />");
            response.getWriter().println("<label>I prefer not to answer.</label>");
            response.getWriter().println("</div>");
            response.getWriter().println("<div style=\"text-align: left; width: 100%\">");
            response.getWriter().println("<br />");
            response.getWriter().println("<input type=\"button\" id=\"provide_full_name\" value=\"Next\" onclick=\"provide_full_name()\" />");
            response.getWriter().println("</div>");
            response.getWriter().println("<div style=\"text-align: left; width: 100%\">");
            response.getWriter().println("<br />");
            response.getWriter().println("<div id=\"step_one_feedback\" style=\"text-align: left; width: 100%\"></div>");
            response.getWriter().println("</div>");
            response.getWriter().println("</div>");
            response.getWriter().println("<div id=\"step_two\" style=\"text-align: left; width: 100%; display: none\">");
            response.getWriter().println("<div style=\"text-align: left; width: 100%\">");
            response.getWriter().println("<br />");
            response.getWriter().println("<input type=\"button\" class=\"return_to_first_step\" value=\"Back\" onclick=\"return_to_first_step()\" />");
            response.getWriter().println("<br /><br />");
            response.getWriter().println("</div>");
            
            Control_Search_Company_Users.use_connection = use_open_connection;
            
            response.getWriter().println(Control_Search_Company_Users.control_search_company_users());
            
            response.getWriter().println("<div style=\"text-align: left; width: 100%\">");
            response.getWriter().println("<br />");
            response.getWriter().println("<input type=\"button\" class=\"return_to_first_step\" value=\"Back\" onclick=\"return_to_first_step()\" />");
            response.getWriter().println("</div>");
            response.getWriter().println("<div style=\"text-align: left; width: 100%\">");
            response.getWriter().println("<br />");
            response.getWriter().println("<div id=\"step_two_feedback\" style=\"text-align: left; width: 100%\"></div>");
            response.getWriter().println("</div>");            
            response.getWriter().println("</div>");
            response.getWriter().println("</div>");
            response.getWriter().println("");
            response.getWriter().println("<script type=\"text/javascript\">");
            response.getWriter().println("var choose_person = \"\";");
            response.getWriter().println("");
            response.getWriter().println("$(document).ready(function() {");
            response.getWriter().println("");
            response.getWriter().println("$(\"#step_one\").fadeIn();");
            response.getWriter().println("document.getElementById(\"full_name\").value = \"\";");
            response.getWriter().println("});");
            response.getWriter().println("");
            response.getWriter().println("function exclude_full_name() {");
            response.getWriter().println("");
            response.getWriter().println("if (document.getElementById(\"no_name\").checked) {");
            response.getWriter().println("");
            response.getWriter().println("document.getElementById(\"full_name\").disabled = true;");
            response.getWriter().println("document.getElementById(\"full_name\").value = \"Anonymous\";");
            response.getWriter().println("} else {");
            response.getWriter().println("");
            response.getWriter().println("document.getElementById(\"full_name\").disabled = false;");
            response.getWriter().println("document.getElementById(\"full_name\").value = \"\";");
            response.getWriter().println("}");
            response.getWriter().println("}");
            response.getWriter().println("");
            response.getWriter().println("function provide_full_name() {");
            response.getWriter().println("");
            response.getWriter().println("if (document.getElementById(\"full_name\").value == \"\" || document.getElementById(\"full_name\").value.replace(/\\s/g, \"\").length == 0) {");
            response.getWriter().println("");
            response.getWriter().println("$(\"#step_one_feedback\").html(\"<label><span style=\\\"color: red\\\">Please provide your name.</span></label>\");");
            response.getWriter().println("} else {");
            response.getWriter().println("");
            response.getWriter().println("$(\"#step_one\").fadeOut();");
            response.getWriter().println("$(\"#step_two\").fadeIn();");
            response.getWriter().println("}");
            response.getWriter().println("}");
            response.getWriter().println("");
            response.getWriter().println("function select_person(user_id) {");
            response.getWriter().println("");
            response.getWriter().println("choose_person = user_id;");
            response.getWriter().println("log_in();");
            response.getWriter().println("}");
            response.getWriter().println("");
            response.getWriter().println("function return_to_first_step() {");
            response.getWriter().println("");
            response.getWriter().println("$(\"#step_two\").fadeOut();");
            response.getWriter().println("$(\"#step_one\").fadeIn();");
            response.getWriter().println("}");
            response.getWriter().println("");
            response.getWriter().println("function log_in() {");
            response.getWriter().println("");
            response.getWriter().println("var guest_session = \"\";");
            response.getWriter().println("var characters = \"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789\";");
            response.getWriter().println("");
            response.getWriter().println("for (var i = 0; i < 20; i++) {");
            response.getWriter().println("");
            response.getWriter().println("guest_session += characters.charAt(Math.floor(Math.random() * characters.length));");
            response.getWriter().println("}");
            response.getWriter().println("");
            response.getWriter().println("var xhttp = new XMLHttpRequest();");
            response.getWriter().println("");
            response.getWriter().println("xhttp.onreadystatechange = function() {");
            response.getWriter().println("");
            response.getWriter().println("if (this.readyState == 4 && this.status == 200) {");
            response.getWriter().println("");
            response.getWriter().println("$(\"#step_two_feedback\").html(this.responseText);");
            response.getWriter().println("}");
            response.getWriter().println("};");
            response.getWriter().println("");
            response.getWriter().println("xhttp.open(\"POST\", \"" + Config.third_party_domain() + "/log-in\");");
            response.getWriter().println("xhttp.setRequestHeader(\"Content-type\", \"application/x-www-form-urlencoded\");");
            response.getWriter().println("");
            response.getWriter().println("xhttp.send(\"full_name=\" + document.getElementById(\"full_name\").value + \"&guest_session=\" + guest_session + \"&select_person=\" + choose_person + \"&log_in=Log in\");");
            response.getWriter().println("}");
            response.getWriter().println("");
            response.getWriter().println("var socket = io.connect(\"https://chat-app-node-1.herokuapp.com\");");
            response.getWriter().println("");
            response.getWriter().println("socket.on('refresh_admin_window', function(data) {");
            response.getWriter().println("");
            response.getWriter().println("if (data != \"\") {");
            response.getWriter().println("");
            response.getWriter().println("window.location = document.location.href.replace(\"#\", \"\");");
            response.getWriter().println("}");
            response.getWriter().println("});");
            response.getWriter().println("</script>");
        }
            
            return "";
        } catch (IOException e) {
            
            return "";
        } catch (Exception e) {
			
			return e.getMessage();
		}
    }
	
    public static void main(String[] args) throws Exception, IOException {
		
        SpringApplication.run(Admin_Chat_Interface.class, args);
    }
}
