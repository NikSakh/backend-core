import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;
import ru.mentee.power.crm.service.LeadService;
import ru.mentee.power.crm.servlet.LeadListServlet;

import java.io.File;

public static void main(String[] args) throws Exception {
  System.out.println("Starting Tomcat...");

  LeadRepository leadRepository = new LeadRepository();
  LeadService leadService = new LeadService(leadRepository);

  leadService.addLead("john@example.com", "TechCorp", LeadStatus.NEW);
  leadService.addLead("alice@example.com", "Innovate Inc", LeadStatus.QUALIFIED);
  leadService.addLead("bob@example.com", "Global Solutions", LeadStatus.CONVERTED);
  leadService.addLead("carol@example.com", "Future Tech", LeadStatus.NEW);
  leadService.addLead("dave@example.com", "Digital Works", LeadStatus.QUALIFIED);

  Tomcat tomcat = new Tomcat();
  tomcat.setPort(8080);

  Context context = tomcat.addContext("", new File(".").getAbsolutePath());
  context.getServletContext().setAttribute("leadService", leadService);

  tomcat.addServlet(context, "LeadListServlet", new LeadListServlet());
  context.addServletMappingDecoded("/leads", "LeadListServlet");

  tomcat.getConnector();

  tomcat.start();

  System.out.println("Server started at http://localhost:8080/leads");
  System.out.println("Press Ctrl+C to stop");

  tomcat.getServer().await();
}