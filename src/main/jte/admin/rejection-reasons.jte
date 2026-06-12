@import ru.mentee.power.crm.domain.jpa.RejectionReasons

@param java.util.List<RejectionReasons> reasons

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Причины отказа</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50 p-8">
<div class="max-w-2xl mx-auto">
    <h1 class="text-2xl font-bold mb-6">Справочник причин отказа</h1>

    <form method="post" class="mb-6 flex gap-2">
        <input type="text" name="name" required placeholder="Название причины"
               class="flex-1 px-3 py-2 border rounded-md">
        <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600">
            Добавить
        </button>
    </form>

    <table class="w-full bg-white rounded-lg shadow-md">
        <thead class="bg-gray-100">
        <tr>
            <th class="p-3 text-left">Причина</th>
            <th class="p-3 text-left">Статус</th>
            <th class="p-3 text-left">Действие</th>
        </tr>
        </thead>
        <tbody>
        @for(RejectionReasons reason : reasons)
            <tr class="border-t">
                <td class="p-3">${reason.getName()}</td>
                <td class="p-3">
                    @if(reason.getActive())
                        <span class="text-green-600">Активна</span>
                    @else
                        <span class="text-red-600">Неактивна</span>
                    @endif
                </td>
                <td class="p-3">
                    <form method="post" action="/admin/rejection-reasons/${String.valueOf(reason.getId())}/toggle">
                        <button type="submit" class="text-blue-600 hover:underline">
                            @if(reason.getActive()) Деактивировать @else Активировать @endif
                        </button>
                    </form>
                </td>
            </tr>
        @endfor
        </tbody>
    </table>

    <a href="/leads" class="block mt-4 text-blue-600 hover:underline">← К списку лидов</a>
</div>
</body>
</html>