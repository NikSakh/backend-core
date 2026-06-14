package gg.jte.generated.ondemand.leads;
import ru.mentee.power.crm.model.LeadDto;
import ru.mentee.power.crm.model.LeadStatus;
@SuppressWarnings("unchecked")
public final class JtecreateGenerated {
	public static final String JTE_NAME = "leads/create.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,3,22,22,22,26,26,32,32,32,32,32,32,32,32,32,35,35,37,37,43,43,43,43,43,43,43,43,43,46,46,48,48,54,54,54,54,54,54,54,54,54,57,57,59,59,66,66,68,68,70,70,71,71,73,73,75,75,76,76,78,78,80,80,81,81,83,83,85,85,86,86,88,88,90,90,92,92,94,94,108,108,108,3,4,4,4,4};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, ru.mentee.power.crm.model.LeadDto lead, org.springframework.validation.BindingResult errors) {
		jteOutput.writeContent("\r\n<!DOCTYPE html>\r\n<html lang=\"ru\">\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n    <title>Добавить лида</title>\r\n    <script src=\"https://cdn.tailwindcss.com\"></script>\r\n</head>\r\n<body class=\"bg-gray-50\">\r\n<header class=\"bg-blue-600 text-white p-4 shadow-md\">\r\n    <h1 class=\"text-2xl font-bold\">CRM System</h1>\r\n</header>\r\n<main class=\"container mx-auto p-6\">\r\n    <div class=\"max-w-md mx-auto mt-8\">\r\n        <h1 class=\"text-2xl font-bold mb-6\">Добавить нового лида</h1>\r\n\r\n        ");
		if (errors != null && errors.hasGlobalErrors()) {
			jteOutput.writeContent("\r\n            <div class=\"bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4\">\r\n                <p>Пожалуйста, исправьте ошибки в форме.</p>\r\n            </div>\r\n        ");
		}
		jteOutput.writeContent("\r\n\r\n        <form action=\"/leads\" method=\"post\" class=\"space-y-4\">\r\n            <div>\r\n                <label for=\"email\" class=\"block text-sm font-medium text-gray-700 mb-1\">Email</label>\r\n                <input type=\"email\" id=\"email\" name=\"email\" required\r\n                      ");
		var __jte_html_attribute_0 = lead.email();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_0);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent("\r\n                       class=\"w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500\"\r\n                       placeholder=\"example@company.com\" />\r\n                ");
		if (errors != null && errors.hasFieldErrors("email")) {
			jteOutput.writeContent("\r\n                    <p class=\"text-red-600 text-sm mt-1\">Некорректный email</p>\r\n                ");
		}
		jteOutput.writeContent("\r\n            </div>\r\n\r\n            <div>\r\n                <label for=\"phone\" class=\"block text-sm font-medium text-gray-700 mb-1\">Телефон</label>\r\n                <input type=\"tel\" id=\"phone\" name=\"phone\"\r\n                      ");
		var __jte_html_attribute_1 = lead.phone();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_1);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent("\r\n                       class=\"w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500\"\r\n                       placeholder=\"+7 (999) 123-45-67\" />\r\n                ");
		if (errors != null && errors.hasFieldErrors("phone")) {
			jteOutput.writeContent("\r\n                    <p class=\"text-red-600 text-sm mt-1\">Телефон не должен превышать 20 символов</p>\r\n                ");
		}
		jteOutput.writeContent("\r\n            </div>\r\n\r\n            <div>\r\n                <label for=\"company\" class=\"block text-sm font-medium text-gray-700 mb-1\">Компания</label>\r\n                <input type=\"text\" id=\"company\" name=\"company\" required\r\n                      ");
		var __jte_html_attribute_2 = lead.company();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_2);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent("\r\n                       class=\"w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500\"\r\n                       placeholder=\"Название компании\" />\r\n                ");
		if (errors != null && errors.hasFieldErrors("company")) {
			jteOutput.writeContent("\r\n                    <p class=\"text-red-600 text-sm mt-1\">Название компании обязательно</p>\r\n                ");
		}
		jteOutput.writeContent("\r\n            </div>\r\n\r\n            <div>\r\n                <label for=\"status\" class=\"block text-sm font-medium text-gray-700 mb-1\">Статус</label>\r\n                <select id=\"status\" name=\"status\" required\r\n                        class=\"w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 bg-white\">\r\n                    ");
		if (lead.status() != null && lead.status() == LeadStatus.NEW) {
			jteOutput.writeContent("\r\n                        <option value=\"NEW\" selected>Новый</option>\r\n                    ");
		} else {
			jteOutput.writeContent("\r\n                        <option value=\"NEW\">Новый</option>\r\n                    ");
		}
		jteOutput.writeContent("\r\n                    ");
		if (lead.status() != null && lead.status() == LeadStatus.CONTACTED) {
			jteOutput.writeContent("\r\n                        <option value=\"CONTACTED\" selected>В контакте</option>\r\n                    ");
		} else {
			jteOutput.writeContent("\r\n                        <option value=\"CONTACTED\">В контакте</option>\r\n                    ");
		}
		jteOutput.writeContent("\r\n                    ");
		if (lead.status() != null && lead.status() == LeadStatus.QUALIFIED) {
			jteOutput.writeContent("\r\n                        <option value=\"QUALIFIED\" selected>Квалифицирован</option>\r\n                    ");
		} else {
			jteOutput.writeContent("\r\n                        <option value=\"QUALIFIED\">Квалифицирован</option>\r\n                    ");
		}
		jteOutput.writeContent("\r\n                    ");
		if (lead.status() != null && lead.status() == LeadStatus.CONVERTED) {
			jteOutput.writeContent("\r\n                        <option value=\"CONVERTED\" selected>Конвертирован</option>\r\n                    ");
		} else {
			jteOutput.writeContent("\r\n                        <option value=\"CONVERTED\">Конвертирован</option>\r\n                    ");
		}
		jteOutput.writeContent("\r\n                    ");
		if (lead.status() != null && lead.status() == LeadStatus.LOST) {
			jteOutput.writeContent("\r\n                        <option value=\"LOST\" selected>Потерян</option>\r\n                    ");
		} else {
			jteOutput.writeContent("\r\n                        <option value=\"LOST\">Потерян</option>\r\n                    ");
		}
		jteOutput.writeContent("\r\n                </select>\r\n                ");
		if (errors != null && errors.hasFieldErrors("status")) {
			jteOutput.writeContent("\r\n                    <p class=\"text-red-600 text-sm mt-1\">Выберите статус</p>\r\n                ");
		}
		jteOutput.writeContent("\r\n            </div>\r\n\r\n            <button type=\"submit\" class=\"w-full bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600\">\r\n                Создать лида\r\n            </button>\r\n            <a href=\"/leads\" class=\"block text-center text-sm text-gray-600 hover:text-gray-900\">Отмена</a>\r\n        </form>\r\n    </div>\r\n</main>\r\n<footer class=\"bg-gray-800 text-white p-4 text-center mt-8\">\r\n    <p>&copy; 2025 CRM Project</p>\r\n</footer>\r\n</body>\r\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		ru.mentee.power.crm.model.LeadDto lead = (ru.mentee.power.crm.model.LeadDto)params.get("lead");
		org.springframework.validation.BindingResult errors = (org.springframework.validation.BindingResult)params.getOrDefault("errors", null);
		render(jteOutput, jteHtmlInterceptor, lead, errors);
	}
}
