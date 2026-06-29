package gg.jte.generated.ondemand.leads;
import ru.mentee.power.crm.model.LeadDto;
import ru.mentee.power.crm.model.LeadStatus;
import gg.jte.Content;
@SuppressWarnings("unchecked")
public final class JtelistGenerated {
	public static final String JTE_NAME = "leads/list.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,4,4,4,4,9,9,9,9,16,16,16,16,16,16,16,16,16,24,24,26,26,28,28,29,29,31,31,33,33,34,34,36,36,38,38,39,39,41,41,43,43,44,44,46,46,48,48,58,58,58,58,62,62,62,62,66,66,66,66,70,70,70,70,81,81,82,82,82,83,83,85,85,88,88,88,88,88,88,88,89,89,89,89,90,90,90,90,90,90,90,92,92,105,105,107,107,107,108,108,108,111,111,111,115,115,117,117,119,119,123,123,123,123,126,126,126,126,127,127,127,127,135,135,139,139,139,139,139,4,5,6,7,7,7,7};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.List<ru.mentee.power.crm.model.LeadDto> leads, ru.mentee.power.crm.model.LeadStatus currentFilter, String search, String statusFilter) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtemainGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <div class=\"bg-white rounded-lg shadow-md p-6\">\r\n        <h2 class=\"text-2xl font-bold mb-4\">Lead List</h2>\r\n\r\n        <form method=\"get\" action=\"/leads\" class=\"bg-gray-50 p-4 rounded-lg mb-6 flex gap-4 items-end flex-wrap\">\r\n            <div>\r\n                <label for=\"search\" class=\"block text-sm font-medium text-gray-700 mb-1\">Поиск</label>\r\n                <input type=\"text\" id=\"search\" name=\"search\"");
				var __jte_html_attribute_0 = search;
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_0);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent("\r\n                       placeholder=\"Имя или email...\"\r\n                       class=\"px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500\">\r\n            </div>\r\n            <div>\r\n                <label for=\"status\" class=\"block text-sm font-medium text-gray-700 mb-1\">Статус</label>\r\n                <select id=\"status\" name=\"status\" class=\"px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 bg-white\">\r\n                    <option value=\"\">Все статусы</option>\r\n                    ");
				if (statusFilter == "NEW") {
					jteOutput.writeContent("\r\n                        <option value=\"NEW\" selected>Новый</option>\r\n                    ");
				} else {
					jteOutput.writeContent("\r\n                        <option value=\"NEW\">Новый</option>\r\n                    ");
				}
				jteOutput.writeContent("\r\n                    ");
				if (statusFilter == "CONTACTED") {
					jteOutput.writeContent("\r\n                        <option value=\"CONTACTED\" selected>В контакте</option>\r\n                    ");
				} else {
					jteOutput.writeContent("\r\n                        <option value=\"CONTACTED\">В контакте</option>\r\n                    ");
				}
				jteOutput.writeContent("\r\n                    ");
				if (statusFilter == "QUALIFIED") {
					jteOutput.writeContent("\r\n                        <option value=\"QUALIFIED\" selected>Квалифицирован</option>\r\n                    ");
				} else {
					jteOutput.writeContent("\r\n                        <option value=\"QUALIFIED\">Квалифицирован</option>\r\n                    ");
				}
				jteOutput.writeContent("\r\n                    ");
				if (statusFilter == "CONVERTED") {
					jteOutput.writeContent("\r\n                        <option value=\"CONVERTED\" selected>Конвертирован</option>\r\n                    ");
				} else {
					jteOutput.writeContent("\r\n                        <option value=\"CONVERTED\">Конвертирован</option>\r\n                    ");
				}
				jteOutput.writeContent("\r\n                    ");
				if (statusFilter == "LOST") {
					jteOutput.writeContent("\r\n                        <option value=\"LOST\" selected>Потерян</option>\r\n                    ");
				} else {
					jteOutput.writeContent("\r\n                        <option value=\"LOST\">Потерян</option>\r\n                    ");
				}
				jteOutput.writeContent("\r\n                </select>\r\n            </div>\r\n            <button type=\"submit\" class=\"bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600\">Найти</button>\r\n            <a href=\"/leads\" class=\"bg-gray-300 text-gray-700 px-4 py-2 rounded hover:bg-gray-400\">Сбросить</a>\r\n        </form>\r\n\r\n        <div class=\"mb-4 flex justify-between items-center\">\r\n            <div class=\"flex gap-2\">\r\n                <a href=\"/leads\"\r\n                   class=\"");
				jteOutput.setContext("a", "class");
				jteOutput.writeUserContent(currentFilter == null ? "bg-blue-500 text-white" : "bg-gray-200");
				jteOutput.setContext("a", null);
				jteOutput.writeContent(" px-4 py-2 rounded\">\r\n                    Все\r\n                </a>\r\n                <a href=\"/leads?status=NEW\"\r\n                   class=\"");
				jteOutput.setContext("a", "class");
				jteOutput.writeUserContent(currentFilter == LeadStatus.NEW ? "bg-blue-500 text-white" : "bg-gray-200");
				jteOutput.setContext("a", null);
				jteOutput.writeContent(" px-4 py-2 rounded\">\r\n                    NEW\r\n                </a>\r\n                <a href=\"/leads?status=CONTACTED\"\r\n                   class=\"");
				jteOutput.setContext("a", "class");
				jteOutput.writeUserContent(currentFilter == LeadStatus.CONTACTED ? "bg-blue-500 text-white" : "bg-gray-200");
				jteOutput.setContext("a", null);
				jteOutput.writeContent(" px-4 py-2 rounded\">\r\n                    CONTACTED\r\n                </a>\r\n                <a href=\"/leads?status=QUALIFIED\"\r\n                   class=\"");
				jteOutput.setContext("a", "class");
				jteOutput.writeUserContent(currentFilter == LeadStatus.QUALIFIED ? "bg-blue-500 text-white" : "bg-gray-200");
				jteOutput.setContext("a", null);
				jteOutput.writeContent(" px-4 py-2 rounded\">\r\n                    QUALIFIED\r\n                </a>\r\n            </div>\r\n\r\n            <a href=\"/leads/new\"\r\n               class=\"bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 transition-colors\">\r\n                + Добавить лида\r\n            </a>\r\n        </div>\r\n\r\n        ");
				if (currentFilter != null) {
					jteOutput.writeContent("\r\n            <p class=\"text-sm text-gray-600 mb-2\">Показаны лиды со статусом: ");
					jteOutput.setContext("p", null);
					jteOutput.writeUserContent(currentFilter);
					jteOutput.writeContent("</p>\r\n        ");
				}
				jteOutput.writeContent("\r\n\r\n        ");
				if (!search.isEmpty() || !statusFilter.isEmpty()) {
					jteOutput.writeContent("\r\n            <p class=\"text-sm text-gray-600 mb-2\">\r\n                Результаты поиска:\r\n                ");
					if (!search.isEmpty()) {
						jteOutput.writeContent(" по запросу \"");
						jteOutput.setContext("p", null);
						jteOutput.writeUserContent(search);
						jteOutput.writeContent("\" ");
					}
					jteOutput.writeContent("\r\n                ");
					if (!statusFilter.isEmpty() && !search.isEmpty()) {
						jteOutput.writeContent(" и ");
					}
					jteOutput.writeContent("\r\n                ");
					if (!statusFilter.isEmpty()) {
						jteOutput.writeContent(" статус \"");
						jteOutput.setContext("p", null);
						jteOutput.writeUserContent(statusFilter);
						jteOutput.writeContent("\" ");
					}
					jteOutput.writeContent("\r\n            </p>\r\n        ");
				}
				jteOutput.writeContent("\r\n\r\n        <table class=\"min-w-full bg-white border border-gray-200\">\r\n            <thead class=\"bg-gray-100\">\r\n            <tr>\r\n                <th class=\"px-4 py-2 text-left\">Email</th>\r\n                <th class=\"px-4 py-2 text-left\">Company</th>\r\n                <th class=\"px-4 py-2 text-left\">Status</th>\r\n                <th class=\"px-4 py-2 text-left\">Причина отказа</th>\r\n                <th class=\"px-4 py-2 text-left\">Действия</th>\r\n            </tr>\r\n            </thead>\r\n            <tbody>\r\n            ");
				for (var lead : leads) {
					jteOutput.writeContent("\r\n                <tr class=\"border-t hover:bg-gray-50\">\r\n                    <td class=\"px-4 py-2\">");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(lead.email());
					jteOutput.writeContent("</td>\r\n                    <td class=\"px-4 py-2\">");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(lead.company());
					jteOutput.writeContent("</td>\r\n                    <td class=\"px-4 py-2\">\r\n                        <span class=\"px-2 py-1 rounded text-sm bg-green-100 text-green-800\">\r\n                            ");
					jteOutput.setContext("span", null);
					jteOutput.writeUserContent(lead.status());
					jteOutput.writeContent("\r\n                        </span>\r\n                    </td>\r\n                    <td class=\"px-4 py-2 text-sm text-gray-600\">\r\n                        ");
					if (lead.rejectionReasonId() != null) {
						jteOutput.writeContent("\r\n                            <span class=\"text-red-600\">Есть</span>\r\n                        ");
					} else {
						jteOutput.writeContent("\r\n                            —\r\n                        ");
					}
					jteOutput.writeContent("\r\n                    </td>\r\n                    <td class=\"px-4 py-2\">\r\n                        <div class=\"flex gap-2\">\r\n                            <a href=\"/leads/");
					jteOutput.setContext("a", "href");
					jteOutput.writeUserContent(lead.id());
					jteOutput.setContext("a", null);
					jteOutput.writeContent("/edit\" class=\"text-blue-600 hover:underline\">\r\n                                Редактировать\r\n                            </a>\r\n                            <form method=\"post\" action=\"/leads/");
					jteOutput.setContext("form", "action");
					jteOutput.writeUserContent(lead.id());
					jteOutput.setContext("form", null);
					jteOutput.writeContent("/delete\" class=\"inline\"\r\n                                  onclick=\"return confirm('Удалить лида ");
					jteOutput.setContext("form", "onclick");
					jteOutput.writeUserContent(lead.email());
					jteOutput.setContext("form", null);
					jteOutput.writeContent("?')\">\r\n                                <button type=\"submit\" class=\"bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700 text-sm\">\r\n                                    Удалить\r\n                                </button>\r\n                            </form>\r\n                        </div>\r\n                    </td>\r\n                </tr>\r\n            ");
				}
				jteOutput.writeContent("\r\n            </tbody>\r\n        </table>\r\n    </div>\r\n");
			}
		});
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		java.util.List<ru.mentee.power.crm.model.LeadDto> leads = (java.util.List<ru.mentee.power.crm.model.LeadDto>)params.get("leads");
		ru.mentee.power.crm.model.LeadStatus currentFilter = (ru.mentee.power.crm.model.LeadStatus)params.get("currentFilter");
		String search = (String)params.get("search");
		String statusFilter = (String)params.get("statusFilter");
		render(jteOutput, jteHtmlInterceptor, leads, currentFilter, search, statusFilter);
	}
}
