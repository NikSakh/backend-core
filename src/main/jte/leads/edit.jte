@import ru.mentee.power.crm.model.LeadDto
@import ru.mentee.power.crm.model.LeadStatus
@import ru.mentee.power.crm.domain.jpa.RejectionReasons

@param ru.mentee.power.crm.model.LeadDto lead
@param java.util.List<RejectionReasons> rejectionReasons

@template.layout.main(content = @`
    <div class="max-w-md mx-auto mt-8">
        <h1 class="text-2xl font-bold mb-6">Редактирование лида</h1>

        <form action="/leads/${lead.id()}" method="post" class="space-y-4">
            <input type="hidden" name="id" value="${lead.id()}">

            <!-- Email -->
            <div>
                <label for="email" class="block text-sm font-medium text-gray-700 mb-1">Email</label>
                <input type="email" id="email" name="email" required
                       value="${lead.email()}"
                       class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500" />
            </div>

            <!-- Phone -->
            <div>
                <label for="phone" class="block text-sm font-medium text-gray-700 mb-1">Телефон</label>
                <input type="tel" id="phone" name="phone"
                       value="${lead.phone()}"
                       class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500" />
            </div>

            <!-- Company -->
            <div>
                <label for="company" class="block text-sm font-medium text-gray-700 mb-1">Компания</label>
                <input type="text" id="company" name="company" required
                       value="${lead.company()}"
                       class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500" />
            </div>

            <!-- Status -->
            <div>
                <label for="status" class="block text-sm font-medium text-gray-700 mb-1">Статус</label>
                <select id="status" name="status" required
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 bg-white">
                    @if(lead.status() == LeadStatus.NEW)
                        <option value="NEW" selected>Новый</option>
                    @else
                        <option value="NEW">Новый</option>
                    @endif
                    @if(lead.status() == LeadStatus.CONTACTED)
                        <option value="CONTACTED" selected>В контакте</option>
                    @else
                        <option value="CONTACTED">В контакте</option>
                    @endif
                    @if(lead.status() == LeadStatus.QUALIFIED)
                        <option value="QUALIFIED" selected>Квалифицирован</option>
                    @else
                        <option value="QUALIFIED">Квалифицирован</option>
                    @endif
                    @if(lead.status() == LeadStatus.CONVERTED)
                        <option value="CONVERTED" selected>Конвертирован</option>
                    @else
                        <option value="CONVERTED">Конвертирован</option>
                    @endif
                    @if(lead.status() == LeadStatus.LOST)
                        <option value="LOST" selected>Потерян</option>
                    @else
                        <option value="LOST">Потерян</option>
                    @endif
                </select>
            </div>

            <!-- Rejection Reason -->
            <div>
                <label for="rejectionReasonId" class="block text-sm font-medium text-gray-700 mb-1">Причина отказа</label>
                <select id="rejectionReasonId" name="rejectionReasonId"
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 bg-white">
                    <option value="">— Не выбрана —</option>
                    @for(RejectionReasons reason : rejectionReasons)
                        <option value="${String.valueOf(reason.getId())}">${reason.getName()}</option>
                    @endfor
                </select>
            </div>

            <button type="submit" class="w-full bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600">
                Сохранить изменения
            </button>
            <a href="/leads" class="block text-center text-sm text-gray-600 hover:text-gray-900">Отмена</a>
        </form>
    </div>
`)