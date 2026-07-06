# Refactoring Summary: InviteeController

## Метрики до/после

| Метрика | До рефакторинга | После рефакторинга |
|---------|-----------------|-------------------|
| Строк кода в контроллере | 80 | 42 |
| Количество зависимостей | 1 (field injection) | 1 (constructor injection) |
| Проблем категории CRITICAL | 5 | 0 |
| Проблем категории MAJOR | 7 | 0 |
| Проблем категории MINOR | 1 | 0 |

## Исправленные проблемы

### API Design
- ✅ Issue #1: POST /getInvitees → GET /invitees
- ✅ Issue #2: Глагол в URL убран
- ✅ Issue #3: Entity заменён на InviteeResponse DTO
- ✅ Issue #4: Добавлена пагинация через Pageable

### Security
- ✅ Issue #5: SQL injection устранён (используется Spring Data JPA)
- ✅ Issue #6: Добавлена Bean Validation через @Valid и DTO с аннотациями
- ✅ Issue #7: Field injection заменён на constructor injection

### Error Handling
- ✅ Issue #8: Пустой catch блок убран
- ✅ Issue #9: RuntimeException заменён на типизированное исключение
- ✅ Issue #10: return null заменён на 404 Not Found

### Code Quality
- ✅ Issue #11: Бизнес-логика вынесена в InviteeService
- ✅ Issue #12: Error handling централизован через GlobalExceptionHandler
- ✅ Issue #13: Hardcoded значения заменены на Enum

## Ключевые архитектурные изменения

1. **Введение DTO слоя** — CreateInviteeRequest (input), InviteeResponse (output)
2. **Вынос бизнес-логики в Service** — контроллер только HTTP layer
3. **GlobalExceptionHandler** — централизованная обработка ошибок

## Применение на собеседованиях

Самая критичная проблема: SQL injection через string concatenation — CRITICAL security vulnerability, блокер для merge.