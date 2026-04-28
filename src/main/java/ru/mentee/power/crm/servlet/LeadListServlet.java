package ru.mentee.power.crm.servlet;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import gg.jte.resolve.DirectoryCodeResolver;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.mentee.power.crm.model.LeadDto;
import ru.mentee.power.crm.service.LeadService;

@WebServlet("/leads")
public class LeadListServlet extends HttpServlet {

  private TemplateEngine templateEngine;

  @Override
  public void init() throws ServletException {
    Path templatePath = Path.of("src/main/jte");
    DirectoryCodeResolver codeResolver = new DirectoryCodeResolver(templatePath);
    this.templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    LeadService service = (LeadService) getServletContext().getAttribute("leadService");
    List<LeadDto> leads = service.findAll();

    Map<String, Object> params = new HashMap<>();
    params.put("leads", leads);

    StringOutput output = new StringOutput();
    templateEngine.render("leads/list.jte", params, output);

    response.setContentType("text/html; charset=UTF-8");
    response.getWriter().write(output.toString());
  }
}