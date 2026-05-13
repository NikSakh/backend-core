@import ru.mentee.power.crm.model.DealDto
@import ru.mentee.power.crm.domain.DealStatus

@param java.util.Map<DealStatus, java.util.List<DealDto>> dealsByStatus
@param DealStatus[] allStatuses

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Воронка продаж</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
<header class="bg-blue-600 text-white p-4 shadow-md">
    <h1 class="text-2xl font-bold">CRM System</h1>
</header>
<main class="container mx-auto p-6">
    <div class="flex justify-between items-center mb-6">
        <h1 class="text-3xl font-bold">Воронка продаж (Kanban)</h1>
        <a href="/deals" class="bg-blue-500 text-white px-4 py-2 rounded">Список сделок</a>
    </div>
    <div class="grid grid-cols-6 gap-4">
        @for(DealStatus status : allStatuses)
            <div class="bg-white rounded shadow-md p-4">
                <h2 class="font-bold mb-4 text-center">${status}</h2>
                @for(DealDto deal : dealsByStatus.getOrDefault(status, java.util.List.of()))
                    <div class="bg-gray-50 p-3 mb-2 rounded border">
                        <p class="font-semibold">Сумма: ${deal.amount()}</p>
                        <p class="text-sm text-gray-600">Lead: ${deal.leadId()}</p>
                        <form method="post" action="/deals/${deal.id()}/transition" class="mt-2">
                            <select name="newStatus" class="text-sm border rounded px-1 py-0">
                                @for(DealStatus s : allStatuses)
                                    @if(s != deal.status())
                                        <option value="${s}">${s}</option>
                                    @endif
                                @endfor
                            </select>
                            <button type="submit" class="text-sm bg-green-500 text-white px-2 py-0 rounded">→</button>
                        </form>
                    </div>
                @endfor
            </div>
        @endfor
    </div>
</main>
</body>
</html>