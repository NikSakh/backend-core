package gg.jte.generated.ondemand.leads;
@SuppressWarnings("unchecked")
public final class JtecreateGenerated {
	public static final String JTE_NAME = "leads/create.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,2,2,4,4,9,19,19,19,19,19,19,19,19,19,25,35,35,35,35,35,35,35,35,35,41,60,68,77,77,77,77,77,0,0,0,0};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, ru.mentee.power.crm.model.LeadDto lead) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtemainGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <div class=\"max-w-md mx-auto mt-8\">\r\n        <h1 class=\"text-2xl font-bold mb-6\">Добавить нового лида</h1>\r\n\r\n        <form action=\"/leads\" method=\"post\" class=\"space-y-4\">\r\n            ");
				jteOutput.writeContent("\r\n            <div>\r\n                <label for=\"email\" class=\"block text-sm font-medium text-gray-700 mb-1\">\r\n                    Email\r\n                </label>\r\n                <input\r\n                        type=\"email\"\r\n                        id=\"email\"\r\n                        name=\"email\"\r\n                        required\r\n                       ");
				var __jte_html_attribute_0 = lead.email();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_0);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent("\r\n                        class=\"w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500\"\r\n                        placeholder=\"example@company.com\"\r\n                />\r\n            </div>\r\n\r\n            ");
				jteOutput.writeContent("\r\n            <div>\r\n                <label for=\"company\" class=\"block text-sm font-medium text-gray-700 mb-1\">\r\n                    Компания\r\n                </label>\r\n                <input\r\n                        type=\"text\"\r\n                        id=\"company\"\r\n                        name=\"company\"\r\n                        required\r\n                       ");
				var __jte_html_attribute_1 = lead.company();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_1);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent("\r\n                        class=\"w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500\"\r\n                        placeholder=\"Название компании\"\r\n                />\r\n            </div>\r\n\r\n            ");
				jteOutput.writeContent("\r\n            <div>\r\n                <label for=\"status\" class=\"block text-sm font-medium text-gray-700 mb-1\">\r\n                    Статус\r\n                </label>\r\n                <select\r\n                        id=\"status\"\r\n                        name=\"status\"\r\n                        required\r\n                        class=\"w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 bg-white\"\r\n                >\r\n                    <option value=\"NEW\" selected>Новый</option>\r\n                    <option value=\"CONTACTED\">В контакте</option>\r\n                    <option value=\"QUALIFIED\">Квалифицирован</option>\r\n                    <option value=\"CONVERTED\">Конвертирован</option>\r\n                    <option value=\"LOST\">Потерян</option>\r\n                </select>\r\n            </div>\r\n\r\n            ");
				jteOutput.writeContent("\r\n            <button\r\n                    type=\"submit\"\r\n                    class=\"w-full bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 focus:ring-2 focus:ring-blue-500\"\r\n            >\r\n                Создать лида\r\n            </button>\r\n\r\n            ");
				jteOutput.writeContent("\r\n            <a\r\n                    href=\"/leads\"\r\n                    class=\"block text-center text-sm text-gray-600 hover:text-gray-900\"\r\n            >\r\n                Отмена\r\n            </a>\r\n        </form>\r\n    </div>\r\n");
			}
		});
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		ru.mentee.power.crm.model.LeadDto lead = (ru.mentee.power.crm.model.LeadDto)params.get("lead");
		render(jteOutput, jteHtmlInterceptor, lead);
	}
}
