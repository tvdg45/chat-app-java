//Author: Timothy van der Graaff
package apps;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import configuration.Config;
import controllers.Control_Search_Company_Users;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
public class Log_In extends HttpServlet {
    
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
    @PostMapping("/log-in")
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
        
        Control_Search_Company_Users.use_connection = use_open_connection;
		
        if (String.valueOf(request.getParameter("log_in")).equals("Log in")) {
            
            if (Control_Search_Company_Users.control_search_company_users(String.valueOf(request.getParameter("select_person"))).contains("Available - online")
                || Control_Search_Company_Users.control_search_company_users(String.valueOf(request.getParameter("select_person"))).contains("Available - offline")) {
                
                Date expires_date = new Date(new Date().getTime() + 86400);
                
                response.setHeader("Set-Cookie", "guest_full_name=" + String.valueOf(request.getParameter("full_name")) +
                        "; HttpOnly; SameSite=None; Secure; Expires=" + expires_date);
                response.setHeader("Set-Cookie", "guest_session=" + String.valueOf(request.getParameter("guest_session")) +
                        "; HttpOnly; SameSite=None; Secure; Expires=" + expires_date);
                response.setHeader("Set-Cookie", "conversation_owner=" + String.valueOf(request.getParameter("select_person")) +
                        "; HttpOnly; SameSite=None; Secure; Expires=" + expires_date);
                /*Cookie guest_full_name = new Cookie("guest_full_name", String.valueOf(request.getParameter("full_name")));
        
                guest_full_name.setMaxAge(86400);
        
                response.addCookie(guest_full_name);
        
                Cookie guest_session = new Cookie("guest_session", String.valueOf(request.getParameter("guest_session")));
        
                guest_session.setMaxAge(86400);
                
                guest_session.
        
                response.addCookie(guest_session);
            
                Cookie conversation_owner = new Cookie("conversation_owner", String.valueOf(request.getParameter("select_person")));
        
                conversation_owner.setMaxAge(86400);
        
                response.addCookie(conversation_owner);*/
                
                out.println("<script type=\"text/javascript\" src=\"https://chat-app-node-1.herokuapp.com/socket.io/socket.io.js\"></script>");
                out.println("<script type=\"text/javascript\">");
                out.println("");
                out.println("var socket = io.connect(\"https://chat-app-node-1.herokuapp.com\");");
                out.println("");
                out.println("socket.emit('refresh_admin_window', '" + String.valueOf(request.getParameter("select_person")) + "');");
                out.println("</script>");
            } else {
                
                out.println("<label><span style='color: red'>Sorry, but this person is not available to chat.</span></label>");
            }
        }
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
