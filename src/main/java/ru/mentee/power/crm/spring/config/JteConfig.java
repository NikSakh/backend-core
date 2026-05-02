package ru.mentee.power.crm.spring.config;

import gg.jte.CodeResolver;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import gg.jte.resolve.DirectoryCodeResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Configuration
public class JteConfig {

  @Bean
  public TemplateEngine templateEngine() {
    CodeResolver codeResolver = new DirectoryCodeResolver(
        Paths.get("src/main/jte")
    );
    return TemplateEngine.create(codeResolver, ContentType.Html);
  }

  @Bean
  public ViewResolver jteViewResolver(TemplateEngine templateEngine) {
    return new ViewResolver() {
      @Override
      public View resolveViewName(String viewName, Locale locale) throws Exception {
        return new View() {
          @Override
          public void render(Map<String, ?> model, HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
            response.setContentType("text/html;charset=UTF-8");
            StringOutput output = new StringOutput();

            Map<String, Object> params = new HashMap<>();
            for (Map.Entry<String, ?> entry : model.entrySet()) {
              params.put(entry.getKey(), entry.getValue());
            }

            templateEngine.render(viewName + ".jte", params, output);
            response.getWriter().write(output.toString());
          }

          @Override
          public String getContentType() {
            return "text/html";
          }
        };
      }
    };
  }
}