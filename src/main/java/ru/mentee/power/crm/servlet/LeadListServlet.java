package ru.mentee.power.crm.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.mentee.power.crm.model.LeadDto;
import ru.mentee.power.crm.service.LeadService;

public class LeadListServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    LeadService leadService = (LeadService) getServletContext().getAttribute("leadService");

    if (leadService == null) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          "LeadService not found in ServletContext");
      return;
    }

    List<LeadDto> leads = leadService.findAll();

    response.setContentType("text/html; charset=UTF-8");
    PrintWriter writer = response.getWriter();

    writer.println("<!DOCTYPE html>");
    writer.println("<html>");
    writer.println("<head><title>CRM - Lead List</title></head>");
    writer.println("<body>");
    writer.println("<h1>Lead List</h1>");
    writer.println("<table border='1'>");
    writer.println("<thead>");
    writer.println("<tr>");
    writer.println("<th>Email</th>");
    writer.println("<th>Company</th>");
    writer.println("<th>Status</th>");
    writer.println("</tr>");
    writer.println("</thead>");
    writer.println("<tbody>");

    for (LeadDto lead : leads) {
      writer.println("<tr>");
      writer.println("<td>" + (lead.email() != null ? lead.email() : "") + "</td>");
      writer.println("<td>" + (lead.company() != null ? lead.company() : "") + "</td>");
      writer.println("<td>" + (lead.status() != null ? lead.status() : "") + "</td>");
      writer.println("</tr>");
    }

    writer.println("</tbody>");
    writer.println("</table>");
    writer.println("</body>");
    writer.println("</html>");
  }
}