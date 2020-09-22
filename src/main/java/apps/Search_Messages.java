//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Search_Messages;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;

import utilities.Form_Validation;

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
public class Search_Messages extends HttpServlet {

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
    @PostMapping("/search-messages")
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

        Control_Search_Messages.use_connection = use_open_connection;
        
        String admin_session;
        
        try {
            
            Control_Search_Messages.conversation_owner = request.getParameter("conversation_owner");
        
            admin_session = request.getParameter("admin_session");
        } catch (NullPointerException e) {
            
            Control_Search_Messages.conversation_owner = "";
        
            admin_session = "";            
        }
        
        //If the user is a guest, he or she will be logged out.
        //Otherwise, the user is an administrator and does not get logged out.
        if (Control_Search_Messages.request_messages().equals("log user out")) {

            //Log out any guest, relating to a change in an administrator's availability status.
            try {
      
                response.addHeader("Set-Cookie", "guest_full_name=; SameSite=None; Secure; Max-Age=0");
                response.addHeader("Set-Cookie", "guest_session=; SameSite=None; Secure; Max-Age=0");
                response.addHeader("Set-Cookie", "conversation_owner=; SameSite=None; Secure; Max-Age=0");
                
                if (Form_Validation.is_string_null_or_white_space(admin_session)) {
                    
                    out.println("[{\"user_id\": \"log user out\", \"full_name\": \"log user out\", " +
                        "\"conversation_owner\": \"log user out\", \"message\": \"log user out\", " +
                        "\"date_received\": \"log user out\", \"time_received\": \"log user out\"}]");
                }
            } catch (NullPointerException e) {
            }
        } else {
            
            out.println(Control_Search_Messages.request_messages());
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
