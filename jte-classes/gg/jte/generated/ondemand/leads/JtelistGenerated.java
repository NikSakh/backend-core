package gg.jte.generated.ondemand.leads;
import ru.mentee.power.crm.model.LeadDto;
import ru.mentee.power.crm.model.LeadStatus;
import gg.jte.Content;
@SuppressWarnings("unchecked")
public final class JtelistGenerated {
	public static final String JTE_NAME = "leads/list.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,4,4,4,4,7,7,7,7,13,13,13,13,17,17,17,17,21,21,21,21,25,25,25,25,30,30,31,31,31,32,32,43,43,45,45,45,46,46,46,49,49,49,53,53,57,57,57,57,57,4,5,5,5,5};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.List<ru.mentee.power.crm.model.LeadDto> leads, ru.mentee.power.crm.model.LeadStatus currentFilter) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtemainGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <div class=\"bg-white rounded-lg shadow-md p-6\">\r\n        <h2 class=\"text-2xl font-bold mb-4\">Lead List</h2>\r\n\r\n        <div class=\"mb-4 flex gap-2\">\r\n            <a href=\"/leads\"\r\n               class=\"");
				jteOutput.setContext("a", "class");
				jteOutput.writeUserContent(currentFilter == null ? "bg-blue-500 text-white" : "bg-gray-200");
				jteOutput.setContext("a", null);
				jteOutput.writeContent(" px-4 py-2 rounded\">\r\n                Все\r\n            </a>\r\n            <a href=\"/leads?status=NEW\"\r\n               class=\"");
				jteOutput.setContext("a", "class");
				jteOutput.writeUserContent(currentFilter == LeadStatus.NEW ? "bg-blue-500 text-white" : "bg-gray-200");
				jteOutput.setContext("a", null);
				jteOutput.writeContent(" px-4 py-2 rounded\">\r\n                NEW\r\n            </a>\r\n            <a href=\"/leads?status=CONTACTED\"\r\n               class=\"");
				jteOutput.setContext("a", "class");
				jteOutput.writeUserContent(currentFilter == LeadStatus.CONTACTED ? "bg-blue-500 text-white" : "bg-gray-200");
				jteOutput.setContext("a", null);
				jteOutput.writeContent(" px-4 py-2 rounded\">\r\n                CONTACTED\r\n            </a>\r\n            <a href=\"/leads?status=QUALIFIED\"\r\n               class=\"");
				jteOutput.setContext("a", "class");
				jteOutput.writeUserContent(currentFilter == LeadStatus.QUALIFIED ? "bg-blue-500 text-white" : "bg-gray-200");
				jteOutput.setContext("a", null);
				jteOutput.writeContent(" px-4 py-2 rounded\">\r\n                QUALIFIED\r\n            </a>\r\n        </div>\r\n\r\n        ");
				if (currentFilter != null) {
					jteOutput.writeContent("\r\n            <p class=\"text-sm text-gray-600 mb-2\">Показаны лиды со статусом: ");
					jteOutput.setContext("p", null);
					jteOutput.writeUserContent(currentFilter);
					jteOutput.writeContent("</p>\r\n        ");
				}
				jteOutput.writeContent("\r\n\r\n        <table class=\"min-w-full bg-white border border-gray-200\">\r\n            <thead class=\"bg-gray-100\">\r\n            <tr>\r\n                <th class=\"px-4 py-2 text-left\">Email</th>\r\n                <th class=\"px-4 py-2 text-left\">Company</th>\r\n                <th class=\"px-4 py-2 text-left\">Status</th>\r\n            </tr>\r\n            </thead>\r\n            <tbody>\r\n            ");
				for (var lead : leads) {
					jteOutput.writeContent("\r\n                <tr class=\"border-t hover:bg-gray-50\">\r\n                    <td class=\"px-4 py-2\">");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(lead.email());
					jteOutput.writeContent("</td>\r\n                    <td class=\"px-4 py-2\">");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(lead.company());
					jteOutput.writeContent("</td>\r\n                    <td class=\"px-4 py-2\">\r\n                            <span class=\"px-2 py-1 rounded text-sm bg-green-100 text-green-800\">\r\n                                ");
					jteOutput.setContext("span", null);
					jteOutput.writeUserContent(lead.status());
					jteOutput.writeContent("\r\n                            </span>\r\n                    </td>\r\n                </tr>\r\n            ");
				}
				jteOutput.writeContent("\r\n            </tbody>\r\n        </table>\r\n    </div>\r\n");
			}
		});
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		java.util.List<ru.mentee.power.crm.model.LeadDto> leads = (java.util.List<ru.mentee.power.crm.model.LeadDto>)params.get("leads");
		ru.mentee.power.crm.model.LeadStatus currentFilter = (ru.mentee.power.crm.model.LeadStatus)params.get("currentFilter");
		render(jteOutput, jteHtmlInterceptor, leads, currentFilter);
	}
}
