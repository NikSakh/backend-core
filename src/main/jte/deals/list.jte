@import ru.mentee.power.crm.model.DealDto

@param java.util.List<DealDto> deals

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Список сделок</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
<header class="bg-blue-600 text-white p-4 shadow-md">
    <h1 class="text-2xl font-bold">CRM System</h1>
</header>
<main class="container mx-auto p-6">
    <div class="flex justify-between items-center mb-6">
        <h1 class="text-3xl font-bold">Сделки CRM</h1>
        <a href="/deals/kanban" class="bg-blue-500 text-white px-4 py-2 rounded">Воронка продаж</a>
    </div>
    <table class="w-full bg-white shadow-md rounded">
        <thead class="bg-gray-200">
        <tr>
            <th class="p-3 text-left">Сумма</th>
            <th class="p-3 text-left">Статус</th>
            <th class="p-3 text-left">Дата создания</th>
        </tr>
        </thead>
        <tbody>
        @for(DealDto deal : deals)
            <tr class="border-b">
                <td class="p-3">Сумма</td>
                <td class="p-3">Статус</td>
                <td class="p-3">Дата</td>
            </tr>
        @endfor
        </tbody>
    </table>
</main>
</body>
</html>