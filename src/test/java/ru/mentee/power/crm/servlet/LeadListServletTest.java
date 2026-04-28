package ru.mentee.power.crm.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import gg.jte.resolve.DirectoryCodeResolver;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mentee.power.crm.model.LeadDto;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.service.LeadService;

@ExtendWith(MockitoExtension.class)
class LeadListServletTest {

  @Mock
  private HttpServletRequest mockRequest;

  @Mock
  private HttpServletResponse mockResponse;

  @Mock
  private ServletContext mockServletContext;

  @Mock
  private ServletConfig mockServletConfig;

  @Mock
  private LeadService mockLeadService;

  private LeadListServlet servlet;
  private TemplateEngine templateEngine;

  @BeforeEach
  void setUp() throws ServletException {
    servlet = new LeadListServlet();
    when(mockServletConfig.getServletContext()).thenReturn(mockServletContext);
    servlet.init(mockServletConfig);

    Path templatePath = Path.of("src/main/jte");
    DirectoryCodeResolver codeResolver = new DirectoryCodeResolver(templatePath);
    templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
  }

  @Test
  void doGetShouldSetContentTypeToHtml() throws ServletException, IOException {
    when(mockServletContext.getAttribute("leadService")).thenReturn(mockLeadService);
    when(mockLeadService.findAll()).thenReturn(List.of());

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(mockResponse.getWriter()).thenReturn(printWriter);

    servlet.doGet(mockRequest, mockResponse);

    verify(mockResponse).setContentType("text/html; charset=UTF-8");
  }

  @Test
  void doGetShouldCallFindAllOnLeadService() throws ServletException, IOException {
    when(mockServletContext.getAttribute("leadService")).thenReturn(mockLeadService);
    when(mockLeadService.findAll()).thenReturn(List.of());

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(mockResponse.getWriter()).thenReturn(printWriter);

    servlet.doGet(mockRequest, mockResponse);

    verify(mockLeadService).findAll();
  }

  @Test
  void doGetShouldRenderTemplateWithEmptyLeadsList() throws ServletException, IOException {
    when(mockServletContext.getAttribute("leadService")).thenReturn(mockLeadService);
    when(mockLeadService.findAll()).thenReturn(List.of());

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(mockResponse.getWriter()).thenReturn(printWriter);

    servlet.doGet(mockRequest, mockResponse);

    StringOutput expectedOutput = new StringOutput();
    templateEngine.render("leads/list.jte", Map.of("leads", List.of()), expectedOutput);

    assertThat(stringWriter.toString()).isEqualTo(expectedOutput.toString());
  }

  @Test
  void doGetShouldRenderTemplateWithSingleLead() throws ServletException, IOException {
    LeadDto lead = new LeadDto("1", "test@example.com", "+123456789",
        "Test Company", LeadStatus.NEW);
    List<LeadDto> leads = List.of(lead);

    when(mockServletContext.getAttribute("leadService")).thenReturn(mockLeadService);
    when(mockLeadService.findAll()).thenReturn(leads);

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(mockResponse.getWriter()).thenReturn(printWriter);

    servlet.doGet(mockRequest, mockResponse);

    StringOutput expectedOutput = new StringOutput();
    templateEngine.render("leads/list.jte", Map.of("leads", leads), expectedOutput);

    assertThat(stringWriter.toString()).isEqualTo(expectedOutput.toString());
  }

  @Test
  void doGetShouldRenderTemplateWithMultipleLeads() throws ServletException, IOException {
    LeadDto leadFirst = new LeadDto("1", "first@example.com", "+111111111",
        "Company First", LeadStatus.NEW);
    LeadDto leadSecond = new LeadDto("2", "second@example.com", "+222222222",
        "Company Second", LeadStatus.QUALIFIED);
    LeadDto leadThird = new LeadDto("3", "third@example.com", "+333333333",
        "Company Third", LeadStatus.CONVERTED);
    List<LeadDto> leads = List.of(leadFirst, leadSecond, leadThird);

    when(mockServletContext.getAttribute("leadService")).thenReturn(mockLeadService);
    when(mockLeadService.findAll()).thenReturn(leads);

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(mockResponse.getWriter()).thenReturn(printWriter);

    servlet.doGet(mockRequest, mockResponse);

    StringOutput expectedOutput = new StringOutput();
    templateEngine.render("leads/list.jte", Map.of("leads", leads), expectedOutput);

    assertThat(stringWriter.toString()).isEqualTo(expectedOutput.toString());
  }
}