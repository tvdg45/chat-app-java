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
public class Admin_Change_Users extends HttpServlet {
    
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
    @PostMapping("/admin-change-users")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        processRequest(request, response);
        
        response.setContentType("text/html");
        response.addHeader("Access-Control-Allow-Origin", "https://tdscloud-dev-ed--c.visualforce.com");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        
        Connection use_open_connection;
        
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
            
            String[] first_names = Find_and_replace.find_and_replace(find, replace,
                    String.valueOf(request.getParameter("first_name"))).split(",");
            
            Control_Search_Company_Users.first_name = first_names;
        } catch (NullPointerException e) {
            
            first_name_exception = "yes";
        }
        
        try {
            
            String[] last_names = Find_and_replace.find_and_replace(find, replace,
                    String.valueOf(request.getParameter("last_name"))).split(",");
            
            Control_Search_Company_Users.last_name = last_names;
        } catch (NullPointerException e) {
            
            last_name_exception = "yes";
        }
        
        try {
            
            String[] emails = Find_and_replace.find_and_replace(find, replace,
                    String.valueOf(request.getParameter("email"))).split(",");
            
            Control_Search_Company_Users.email = emails;
        } catch (NullPointerException e) {
            
            email_exception = "yes";
        }
        
        try {
            
            String[] user_ids = Find_and_replace.find_and_replace(find, replace,
                    String.valueOf(request.getParameter("user_id"))).split(",");
            
            Control_Search_Company_Users.user_id = user_ids;
        } catch (NullPointerException e) {
            
            user_id_exception = "yes";
        }
        
        Control_Search_Company_Users.date_received = String.valueOf(localDate);
        Control_Search_Company_Users.time_received = String.valueOf(time_format.format(localTime));
        
        if (String.valueOf(request.getParameter("add_user")).equals("Add user")
            && !(first_name_exception.equals("yes") || last_name_exception.equals("yes")
                || email_exception.equals("yes") || user_id_exception.equals("yes"))) {
            
            Control_Search_Company_Users.control_add_user();
        }
        
        if (String.valueOf(request.getParameter("change_user")).equals("Change user")
            && !(first_name_exception.equals("yes") || last_name_exception.equals("yes")
                || email_exception.equals("yes") || user_id_exception.equals("yes"))) {
            
            Control_Search_Company_Users.control_change_user();
        }
        
        if (String.valueOf(request.getParameter("delete_user")).equals("Delete user")
            && !(first_name_exception.equals("yes") || last_name_exception.equals("yes")
                || email_exception.equals("yes") || user_id_exception.equals("yes"))) {
            
            Control_Search_Company_Users.control_delete_user();
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
