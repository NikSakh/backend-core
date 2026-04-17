package ru.mentee.power.crm.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

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

  @BeforeEach
  void setUp() throws ServletException {
    servlet = new LeadListServlet();
    when(mockServletConfig.getServletContext()).thenReturn(mockServletContext);
    servlet.init(mockServletConfig);
  }

  @Test
  void doGetShouldReturnInternalServerErrorWhenLeadServiceIsNull()
      throws ServletException, IOException {
    when(mockServletContext.getAttribute("leadService")).thenReturn(null);

    servlet.doGet(mockRequest, mockResponse);

    verify(mockResponse).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
        "LeadService not found in ServletContext");
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
  void doGetShouldRenderHtmlWithTableHeaders() throws ServletException, IOException {
    when(mockServletContext.getAttribute("leadService")).thenReturn(mockLeadService);
    when(mockLeadService.findAll()).thenReturn(List.of());

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(mockResponse.getWriter()).thenReturn(printWriter);

    servlet.doGet(mockRequest, mockResponse);

    String html = stringWriter.toString();
    assertThat(html).contains("<!DOCTYPE html>");
    assertThat(html).contains("<title>CRM - Lead List</title>");
    assertThat(html).contains("<h1>Lead List</h1>");
    assertThat(html).contains("<table border='1'>");
    assertThat(html).contains("<th>Email</th>");
    assertThat(html).contains("<th>Company</th>");
    assertThat(html).contains("<th>Status</th>");
  }

  @Test
  void doGetShouldRenderEmptyTableWhenNoLeads() throws ServletException, IOException {
    when(mockServletContext.getAttribute("leadService")).thenReturn(mockLeadService);
    when(mockLeadService.findAll()).thenReturn(List.of());

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(mockResponse.getWriter()).thenReturn(printWriter);

    servlet.doGet(mockRequest, mockResponse);

    String html = stringWriter.toString();
    assertThat(html).contains("<tbody>");
    assertThat(html).contains("</tbody>");
    assertThat(html).doesNotContain("<td>");
  }

  @Test
  void doGetShouldRenderSingleLeadCorrectly() throws ServletException, IOException {
    LeadDto lead = new LeadDto("1", "test@example.com", "+123456789",
        "Test Company", LeadStatus.NEW);
    List<LeadDto> leads = List.of(lead);

    when(mockServletContext.getAttribute("leadService")).thenReturn(mockLeadService);
    when(mockLeadService.findAll()).thenReturn(leads);

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(mockResponse.getWriter()).thenReturn(printWriter);

    servlet.doGet(mockRequest, mockResponse);

    String html = stringWriter.toString();
    assertThat(html).contains("<td>test@example.com</td>");
    assertThat(html).contains("<td>Test Company</td>");
    assertThat(html).contains("<td>Новый</td>");
  }

  @Test
  void doGetShouldRenderMultipleLeadsCorrectly() throws ServletException, IOException {
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

    String html = stringWriter.toString();
    assertThat(html).contains("<td>first@example.com</td>");
    assertThat(html).contains("<td>Company First</td>");
    assertThat(html).contains("<td>Новый</td>");
    assertThat(html).contains("<td>second@example.com</td>");
    assertThat(html).contains("<td>Company Second</td>");
    assertThat(html).contains("<td>Квалифицирован</td>");
    assertThat(html).contains("<td>third@example.com</td>");
    assertThat(html).contains("<td>Company Third</td>");
    assertThat(html).contains("<td>Конвертирован</td>");
  }

  @Test
  void doGetShouldHandleNullEmailInLead() throws ServletException, IOException {
    LeadDto lead = new LeadDto("1", null, "+123456789", "Test Company", LeadStatus.NEW);
    List<LeadDto> leads = List.of(lead);

    when(mockServletContext.getAttribute("leadService")).thenReturn(mockLeadService);
    when(mockLeadService.findAll()).thenReturn(leads);

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(mockResponse.getWriter()).thenReturn(printWriter);

    servlet.doGet(mockRequest, mockResponse);

    String html = stringWriter.toString();
    assertThat(html).contains("<td></td>");
    assertThat(html).contains("<td>Test Company</td>");
    assertThat(html).contains("<td>Новый</td>");
  }

  @Test
  void doGetShouldHandleNullCompanyInLead() throws ServletException, IOException {
    LeadDto lead = new LeadDto("1", "test@example.com", "+123456789", null, LeadStatus.NEW);
    List<LeadDto> leads = List.of(lead);

    when(mockServletContext.getAttribute("leadService")).thenReturn(mockLeadService);
    when(mockLeadService.findAll()).thenReturn(leads);

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(mockResponse.getWriter()).thenReturn(printWriter);

    servlet.doGet(mockRequest, mockResponse);

    String html = stringWriter.toString();
    assertThat(html).contains("<td>test@example.com</td>");
    assertThat(html).contains("<td></td>");
    assertThat(html).contains("<td>Новый</td>");
  }

  @Test
  void doGetShouldHandleNullStatusInLead() throws ServletException, IOException {
    LeadDto lead = new LeadDto("1", "test@example.com", "+123456789",
        "Test Company", null);
    List<LeadDto> leads = List.of(lead);

    when(mockServletContext.getAttribute("leadService")).thenReturn(mockLeadService);
    when(mockLeadService.findAll()).thenReturn(leads);

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(mockResponse.getWriter()).thenReturn(printWriter);

    servlet.doGet(mockRequest, mockResponse);

    String html = stringWriter.toString();
    assertThat(html).contains("<td>test@example.com</td>");
    assertThat(html).contains("<td>Test Company</td>");
    assertThat(html).contains("<td></td>");
  }

  @Test
  void doGetShouldHandleAllNullFieldsInLead() throws ServletException, IOException {
    LeadDto lead = new LeadDto("1", null, null, null, null);
    List<LeadDto> leads = List.of(lead);

    when(mockServletContext.getAttribute("leadService")).thenReturn(mockLeadService);
    when(mockLeadService.findAll()).thenReturn(leads);

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(mockResponse.getWriter()).thenReturn(printWriter);

    servlet.doGet(mockRequest, mockResponse);

    String html = stringWriter.toString();
    assertThat(html).contains("<td></td>");
    assertThat(html).contains("<tr>");
    assertThat(html).contains("</tr>");
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
  void doGetShouldRenderCompleteHtmlStructure() throws ServletException, IOException {
    when(mockServletContext.getAttribute("leadService")).thenReturn(mockLeadService);
    when(mockLeadService.findAll()).thenReturn(List.of());

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(mockResponse.getWriter()).thenReturn(printWriter);

    servlet.doGet(mockRequest, mockResponse);

    String html = stringWriter.toString();
    assertThat(html).startsWith("<!DOCTYPE html>");
    assertThat(html).contains("<html>");
    assertThat(html).contains("<head>");
    assertThat(html).contains("<body>");
    assertThat(html).contains("<thead>");
    assertThat(html).contains("<tbody>");
    assertThat(html).contains("</html>");
  }
}