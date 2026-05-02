@import ru.mentee.power.crm.model.LeadDto
@import ru.mentee.power.crm.model.LeadStatus
@import gg.jte.Content

@param java.util.List<ru.mentee.power.crm.model.LeadDto> leads
@param ru.mentee.power.crm.model.LeadStatus currentFilter

@template.layout.main(content = @`
    <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-2xl font-bold mb-4">Lead List</h2>

        <!-- Фильтры и кнопка Добавить -->
        <div class="mb-4 flex justify-between items-center">
            <div class="flex gap-2">
                <a href="/leads"
                   class="${currentFilter == null ? "bg-blue-500 text-white" : "bg-gray-200"} px-4 py-2 rounded">
                    Все
                </a>
                <a href="/leads?status=NEW"
                   class="${currentFilter == LeadStatus.NEW ? "bg-blue-500 text-white" : "bg-gray-200"} px-4 py-2 rounded">
                    NEW
                </a>
                <a href="/leads?status=CONTACTED"
                   class="${currentFilter == LeadStatus.CONTACTED ? "bg-blue-500 text-white" : "bg-gray-200"} px-4 py-2 rounded">
                    CONTACTED
                </a>
                <a href="/leads?status=QUALIFIED"
                   class="${currentFilter == LeadStatus.QUALIFIED ? "bg-blue-500 text-white" : "bg-gray-200"} px-4 py-2 rounded">
                    QUALIFIED
                </a>
            </div>

            <a href="/leads/new"
               class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 transition-colors">
                + Добавить лида
            </a>
        </div>

        @if(currentFilter != null)
            <p class="text-sm text-gray-600 mb-2">Показаны лиды со статусом: ${currentFilter}</p>
        @endif

        <table class="min-w-full bg-white border border-gray-200">
            <thead class="bg-gray-100">
            <tr>
                <th class="px-4 py-2 text-left">Email</th>
                <th class="px-4 py-2 text-left">Company</th>
                <th class="px-4 py-2 text-left">Status</th>
            </tr>
            </thead>
            <tbody>
            @for(var lead : leads)
                <tr class="border-t hover:bg-gray-50">
                    <td class="px-4 py-2">${lead.email()}</td>
                    <td class="px-4 py-2">${lead.company()}</td>
                    <td class="px-4 py-2">
                            <span class="px-2 py-1 rounded text-sm bg-green-100 text-green-800">
                                ${lead.status()}
                            </span>
                    </td>
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
`)