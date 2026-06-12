@import ru.mentee.power.crm.model.LeadDto
@import ru.mentee.power.crm.model.LeadStatus
@import gg.jte.Content

@param java.util.List<ru.mentee.power.crm.model.LeadDto> leads
@param ru.mentee.power.crm.model.LeadStatus currentFilter
@param String search
@param String statusFilter

@template.layout.main(content = @`
    <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-2xl font-bold mb-4">Lead List</h2>

        <form method="get" action="/leads" class="bg-gray-50 p-4 rounded-lg mb-6 flex gap-4 items-end flex-wrap">
            <div>
                <label for="search" class="block text-sm font-medium text-gray-700 mb-1">Поиск</label>
                <input type="text" id="search" name="search" value="${search}"
                       placeholder="Имя или email..."
                       class="px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500">
            </div>
            <div>
                <label for="status" class="block text-sm font-medium text-gray-700 mb-1">Статус</label>
                <select id="status" name="status" class="px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 bg-white">
                    <option value="">Все статусы</option>
                    @if(statusFilter == "NEW")
                        <option value="NEW" selected>Новый</option>
                    @else
                        <option value="NEW">Новый</option>
                    @endif
                    @if(statusFilter == "CONTACTED")
                        <option value="CONTACTED" selected>В контакте</option>
                    @else
                        <option value="CONTACTED">В контакте</option>
                    @endif
                    @if(statusFilter == "QUALIFIED")
                        <option value="QUALIFIED" selected>Квалифицирован</option>
                    @else
                        <option value="QUALIFIED">Квалифицирован</option>
                    @endif
                    @if(statusFilter == "CONVERTED")
                        <option value="CONVERTED" selected>Конвертирован</option>
                    @else
                        <option value="CONVERTED">Конвертирован</option>
                    @endif
                    @if(statusFilter == "LOST")
                        <option value="LOST" selected>Потерян</option>
                    @else
                        <option value="LOST">Потерян</option>
                    @endif
                </select>
            </div>
            <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">Найти</button>
            <a href="/leads" class="bg-gray-300 text-gray-700 px-4 py-2 rounded hover:bg-gray-400">Сбросить</a>
        </form>

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

        @if(!search.isEmpty() || !statusFilter.isEmpty())
            <p class="text-sm text-gray-600 mb-2">
                Результаты поиска:
                @if(!search.isEmpty()) по запросу "${search}" @endif
                @if(!statusFilter.isEmpty() && !search.isEmpty()) и @endif
                @if(!statusFilter.isEmpty()) статус "${statusFilter}" @endif
            </p>
        @endif

        <table class="min-w-full bg-white border border-gray-200">
            <thead class="bg-gray-100">
            <tr>
                <th class="px-4 py-2 text-left">Email</th>
                <th class="px-4 py-2 text-left">Company</th>
                <th class="px-4 py-2 text-left">Status</th>
                <th class="px-4 py-2 text-left">Причина отказа</th>
                <th class="px-4 py-2 text-left">Действия</th>
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
                    <td class="px-4 py-2 text-sm">
                        @if(lead.rejectionReasonName() != null)
                            <span class="text-red-600">${lead.rejectionReasonName()}</span>
                        @else
                            —
                        @endif
                    </td>
                    <td class="px-4 py-2">
                        <div class="flex gap-2">
                            <a href="/leads/${lead.id()}/edit" class="text-blue-600 hover:underline">
                                Редактировать
                            </a>
                            <form method="post" action="/leads/${lead.id()}/delete" class="inline"
                                  onclick="return confirm('Удалить лида ${lead.email()}?')">
                                <button type="submit" class="bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700 text-sm">
                                    Удалить
                                </button>
                            </form>
                        </div>
                    </td>
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
`)