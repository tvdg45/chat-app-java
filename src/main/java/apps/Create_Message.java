//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Create_Message;

import java.time.LocalDate;
import java.time.LocalTime;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.*;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
public class Create_Message extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
    * Handles the HTTP <code>GET</code> method.
    *
    * @param request servlet request
    * @param response servlet response
    * @throws ServletException if a servlet-specific error occurs
    * @throws IOException if an I/O error occurs
    */
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        //processRequest(request, response);

    }

    /**
    * Handles the HTTP <code>POST</code> method.
    *
    * @param request servlet request
    * @param response servlet response
    * @throws ServletException if a servlet-specific error occurs
    * @throws IOException if an I/O error occurs
    */
    
    @Override
    @PostMapping("/create-message")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        processRequest(request, response);
        
        response.setContentType("text/html");
        response.addHeader("Access-Control-Allow-Origin", "https://www.timothysdigitalsolutions.com");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
  
        PrintWriter out = response.getWriter();
  
        Connection use_open_connection;
  
        use_open_connection = Config.openConnection();
        
        String admin_session;
        String guest_full_name = "";
        String guest_session = "";
        String conversation_owner = "";
  
        DateTimeFormatter time_format = DateTimeFormatter.ofPattern("hh:mm a 'EST'");
          
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now(ZoneId.of("America/New_York"));
  
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
                
                response.addHeader("Set-Cookie", "guest_full_name=; SameSite=None; Secure; Max-Age=0");
                response.addHeader("Set-Cookie", "guest_session=; SameSite=None; Secure; Max-Age=0");
                response.addHeader("Set-Cookie", "conversation_owner=; SameSite=None; Secure; Max-Age=0");
                
                out.println("<script type=\"text/javascript\">");
                out.println("window.location = document.location.href.replace(\"#\", \"\");");
                out.println("</script>");
            } 
        } catch (NullPointerException e) {
      
            if (e.getMessage() == null) {
        
                guest_full_name = "";
                guest_session = "";
                conversation_owner = "";
            }
        }
        
        out.println("<script type=\"text/javascript\" src=\"https://www.timothysdigitalsolutions.com/backstretch/js/jquery-3.2.1.js\"></script>");
        out.println("<script type=\"text/javascript\" src=\"https://chat-app-node-1.herokuapp.com/socket.io/socket.io.js\"></script>");
        out.println("<script type=\"text/javascript\">");
        out.println("var socket = io.connect('https://chat-app-node-1.herokuapp.com');");
  
        if (String.valueOf(request.getParameter("create_message")).equals("Send")) {
            
            if (!(admin_session.equals("")) || (!(guest_full_name.equals("")) && !(guest_session.equals(""))
                && !(conversation_owner.equals("")))) {
        
                Control_Create_Message.use_connection = use_open_connection;
                Control_Create_Message.admin_session = admin_session;
                Control_Create_Message.guest_full_name = guest_full_name;
                Control_Create_Message.guest_session = guest_session;
                Control_Create_Message.conversation_owner = conversation_owner;
                Control_Create_Message.message = String.valueOf(request.getParameter("message"));
                Control_Create_Message.date_received = String.valueOf(localDate);
                Control_Create_Message.time_received = String.valueOf(time_format.format(localTime));
                Control_Create_Message.create_message = String.valueOf(request.getParameter("create_message"));
        
                out.println(Control_Create_Message.control_add_instant_chat_message());
            } else {
                
                out.println("window.location = document.location.href.replace(\"#\", \"\");");
            }
        }
  
        out.println("</script>");
    }

    /**
    * Returns a short description of the servlet.
    *
    * @return a String containing servlet description
    */
    
    @Override
    public String getServletInfo() {

        return "Short description";
    } // </editor-fold>
}
