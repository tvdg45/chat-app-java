//Author: Timothy van der Graaff
package apps;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import java.sql.Connection;
import configuration.Config;
import utilities.Form_Validation;
import controllers.Control_Search_Company_Users;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.*;

import org.springframework.web.servlet.View;

public class Admin_Chat_Interface implements View {
    
    @Override
    public String getContentType() {
        return "text/html";
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        response.setContentType(getContentType());
        PrintWriter writer = response.getWriter();
        writer.println("This is my custom dummy view.<br/>");
        writer.println("<h3>Model attributes</h3>");
        for (Map.Entry<String, ?> entry : model.entrySet()) {
            writer.printf("%s = %s<br/>", entry.getKey(), entry.getValue());
        }
    }
}
