@import ru.mentee.power.crm.model.LeadDto

@param ru.mentee.power.crm.model.LeadDto lead

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Конвертация в сделку</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
<header class="bg-blue-600 text-white p-4 shadow-md">
    <h1 class="text-2xl font-bold">CRM System</h1>
</header>
<main class="container mx-auto p-6">
    <div class="max-w-md mx-auto mt-8">
        <h1 class="text-2xl font-bold mb-6">Конвертация лида в сделку</h1>
        <p class="mb-4">Лид: <strong>${lead.email()}</strong> (${lead.company()})</p>
        <form action="/deals/convert" method="post" class="space-y-4">
            <input type="hidden" name="leadId" value="${lead.id()}" />
            <div>
                <label for="amount" class="block text-sm font-medium text-gray-700 mb-1">Сумма сделки</label>
                <input type="number" id="amount" name="amount" required step="0.01"
                       class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                       placeholder="150000.00" />
            </div>
            <button type="submit" class="w-full bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600">
                Создать сделку
            </button>
            <a href="/leads" class="block text-center text-sm text-gray-600 hover:text-gray-900">Отмена</a>
        </form>
    </div>
</main>
</body>
</html>