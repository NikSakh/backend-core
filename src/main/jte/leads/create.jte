@import ru.mentee.power.crm.model.LeadDto
@import ru.mentee.power.crm.model.LeadStatus

@param ru.mentee.power.crm.model.LeadDto lead
@param org.springframework.validation.BindingResult errors = null

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Добавить лида</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
<header class="bg-blue-600 text-white p-4 shadow-md">
    <h1 class="text-2xl font-bold">CRM System</h1>
</header>
<main class="container mx-auto p-6">
    <div class="max-w-md mx-auto mt-8">
        <h1 class="text-2xl font-bold mb-6">Добавить нового лида</h1>

        @if(errors != null && errors.hasGlobalErrors())
            <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
                <p>Пожалуйста, исправьте ошибки в форме.</p>
            </div>
        @endif

        <form action="/leads" method="post" class="space-y-4">
            <div>
                <label for="email" class="block text-sm font-medium text-gray-700 mb-1">Email</label>
                <input type="email" id="email" name="email" required
                       value="${lead.email()}"
                       class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                       placeholder="example@company.com" />
                @if(errors != null && errors.hasFieldErrors("email"))
                    <p class="text-red-600 text-sm mt-1">Некорректный email</p>
                @endif
            </div>

            <div>
                <label for="phone" class="block text-sm font-medium text-gray-700 mb-1">Телефон</label>
                <input type="tel" id="phone" name="phone"
                       value="${lead.phone()}"
                       class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                       placeholder="+7 (999) 123-45-67" />
                @if(errors != null && errors.hasFieldErrors("phone"))
                    <p class="text-red-600 text-sm mt-1">Телефон не должен превышать 20 символов</p>
                @endif
            </div>

            <div>
                <label for="company" class="block text-sm font-medium text-gray-700 mb-1">Компания</label>
                <input type="text" id="company" name="company" required
                       value="${lead.company()}"
                       class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                       placeholder="Название компании" />
                @if(errors != null && errors.hasFieldErrors("company"))
                    <p class="text-red-600 text-sm mt-1">Название компании обязательно</p>
                @endif
            </div>

            <div>
                <label for="status" class="block text-sm font-medium text-gray-700 mb-1">Статус</label>
                <select id="status" name="status" required
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 bg-white">
                    @if(lead.status() != null && lead.status() == LeadStatus.NEW)
                        <option value="NEW" selected>Новый</option>
                    @else
                        <option value="NEW">Новый</option>
                    @endif
                    @if(lead.status() != null && lead.status() == LeadStatus.CONTACTED)
                        <option value="CONTACTED" selected>В контакте</option>
                    @else
                        <option value="CONTACTED">В контакте</option>
                    @endif
                    @if(lead.status() != null && lead.status() == LeadStatus.QUALIFIED)
                        <option value="QUALIFIED" selected>Квалифицирован</option>
                    @else
                        <option value="QUALIFIED">Квалифицирован</option>
                    @endif
                    @if(lead.status() != null && lead.status() == LeadStatus.CONVERTED)
                        <option value="CONVERTED" selected>Конвертирован</option>
                    @else
                        <option value="CONVERTED">Конвертирован</option>
                    @endif
                    @if(lead.status() != null && lead.status() == LeadStatus.LOST)
                        <option value="LOST" selected>Потерян</option>
                    @else
                        <option value="LOST">Потерян</option>
                    @endif
                </select>
                @if(errors != null && errors.hasFieldErrors("status"))
                    <p class="text-red-600 text-sm mt-1">Выберите статус</p>
                @endif
            </div>

            <button type="submit" class="w-full bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600">
                Создать лида
            </button>
            <a href="/leads" class="block text-center text-sm text-gray-600 hover:text-gray-900">Отмена</a>
        </form>
    </div>
</main>
<footer class="bg-gray-800 text-white p-4 text-center mt-8">
    <p>&copy; 2025 CRM Project</p>
</footer>
</body>
</html>