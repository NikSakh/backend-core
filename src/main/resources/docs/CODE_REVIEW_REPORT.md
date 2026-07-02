# Code Review Report: InviteeController

**Автор ревью:** [Твоё имя]
**Дата:** 2026-07-02
**Версия кода:** InviteeController.java (problematic)

## Резюме

| Категория | CRITICAL | MAJOR | MINOR | Всего |
|-----------|----------|-------|-------|-------|
| API Design | 2 | 2 | 0 | 4 |
| Security | 3 | 0 | 0 | 3 |
| Error Handling | 0 | 3 | 0 | 3 |
| Code Quality | 0 | 2 | 1 | 3 |
| **Итого** | **5** | **7** | **1** | **13** |

---

## Категория 1: API Design

### Issue #1: POST используется для чтения данных

**Категория:** API Design
**Приоритет:** CRITICAL
**Местоположение:** InviteeController.java, строка 23, метод `getInvitees()`

**Что плохо:**
```java
@PostMapping("/getInvitees")
public List<Invitee> getInvitees() {
    return repository.findAll();
}
Почему плохо:
Согласно RFC 7231 раздел 4.3.3, POST предназначен для создания ресурсов и операций не являющихся стандартными CRUD. Использование POST для чтения данных нарушает HTTP семантику и REST constraints. GET должен быть безопасным и идемпотентным, что позволяет кэширование.

Как исправить:

java
@GetMapping("/invitees")
public ResponseEntity<List<InviteeResponse>> getInvitees() {
    List<InviteeResponse> invitees = service.getAll();
    return ResponseEntity.ok(invitees);
}
Issue #2: Глагол в URL (RPC стиль)
Категория: API Design
Приоритет: MAJOR
Местоположение: InviteeController.java, строка 23

Что плохо:

java
@PostMapping("/getInvitees")
Почему плохо:
REST API использует существительные для обозначения ресурсов и HTTP методы для действий. Глагол "get" в URL — это RPC стиль, который нарушает REST architectural constraints. Правильный RESTful URL: /invitees для коллекции.

Как исправить:

java
@GetMapping("/invitees")
Issue #3: Возврат Entity вместо DTO
Категория: API Design / Security
Приоритет: CRITICAL
Местоположение: Все методы контроллера

Что плохо:

java
public Invitee getById(@PathVariable UUID id) {
    return repository.findById(id).orElse(null);
}
Почему плохо:
JPA Entity содержит технические поля, которые не должны уходить клиентам. Это создаёт coupling между API контрактом и схемой БД. Также security risk — exposure внутренних полей (id, createdAt, возможно audit поля).

Как исправить:

java
public record InviteeResponse(UUID id, String email, String firstName, String status) {}

@GetMapping("/invitees/{id}")
public ResponseEntity<InviteeResponse> getById(@PathVariable UUID id) {
    Invitee invitee = service.getById(id);
    return ResponseEntity.ok(mapper.toResponse(invitee));
}
Issue #4: Нет пагинации для спискового endpoint
Категория: API Design
Приоритет: MAJOR
Местоположение: метод getInvitees()

Что плохо:

java
public List<Invitee> getInvitees() {
    return repository.findAll(); // Может вернуть все записи
}
Почему плохо:
При большом количестве записей метод загрузит все данные в память, что может привести к OutOfMemoryError. Spring Data JPA предоставляет Pageable для решения этой проблемы.

Как исправить:

java
@GetMapping("/invitees")
public ResponseEntity<Page<InviteeResponse>> getInvitees(@PageableDefault(size = 20) Pageable pageable) {
    Page<Invitee> page = repository.findAll(pageable);
    return ResponseEntity.ok(page.map(mapper::toResponse));
}
Категория 2: Security
Issue #5: SQL Injection через конкатенацию строк
Категория: Security
Приоритет: CRITICAL
Местоположение: InviteeController.java, строка 36, метод create()

Что плохо:

java
String email = (String) params.get("email");
String sql = "SELECT COUNT(*) FROM invitees WHERE email = '" + email + "'";
Почему плохо:
Прямая конкатенация пользовательского ввода в SQL запрос — OWASP Top 10 уязвимость (SQL Injection). Атакующий может передать email вида ' OR '1'='1 и получить доступ ко всем записям, или '; DROP TABLE invitees; -- для удаления таблицы.

Как исправить:

java
// Spring Data JPA
public interface InviteeRepository extends JpaRepository<Invitee, UUID> {
    boolean existsByEmail(String email);
}
Issue #6: Нет валидации входных данных
Категория: Security
Приоритет: CRITICAL
Местоположение: InviteeController.java, строки 34-35, метод create()

Что плохо:

java
@PostMapping("/invitees")
public Invitee create(@RequestBody Map<String, Object> params) {
    String email = (String) params.get("email");
    String firstName = (String) params.get("firstName");
Почему плохо:
Принимается Map<String, Object> без типизации и валидации. Отсутствует проверка на null, пустые строки, некорректный email, длину полей. Это позволяет сохранить в БД некорректные данные.

Как исправить:

java
public record CreateInviteeRequest(
    @NotBlank @Email String email,
    @NotBlank @Size(min = 2, max = 50) String firstName
) {}

@PostMapping("/invitees")
public ResponseEntity<InviteeResponse> create(@Valid @RequestBody CreateInviteeRequest request) {
    InviteeResponse created = service.create(request);
    URI location = URI.create("/api/invitees/" + created.id());
    return ResponseEntity.created(location).body(created);
}
Issue #7: Field injection вместо constructor injection
Категория: Security / Code Quality
Приоритет: MAJOR
Местоположение: InviteeController.java, строки 20-21

Что плохо:

java
@Autowired
InviteeRepository repository;
Почему плохо:
Field injection делает зависимости неявными, усложняет тестирование (нельзя передать mock через конструктор в unit тестах). Spring Framework Reference рекомендует constructor injection.

Как исправить:

java
private final InviteeService service;

public InviteeController(InviteeService service) {
    this.service = service;
}
Категория 3: Error Handling
Issue #8: Пустой catch блок с return null
Категория: Error Handling
Приоритет: MAJOR
Местоположение: InviteeController.java, строки 64-66, метод updateStatus()

Что плохо:

java
} catch (Exception e) {
    return null;
}
Почему плохо:
Пустой catch блок скрывает ошибки — exception проглатывается без логирования, клиент получает null (200 OK с пустым телом) и не знает что пошло не так.

Как исправить:
Убрать try-catch, позволить исключениям пробрасываться в GlobalExceptionHandler:

java
@ExceptionHandler(EntityNotFoundException.class)
public ResponseEntity<ProblemDetail> handleNotFound(EntityNotFoundException ex) {
    return ResponseEntity.status(404).body(...);
}
Issue #9: 500 на бизнес-ошибки вместо 4xx
Категория: Error Handling
Приоритет: MAJOR
Местоположение: InviteeController.java, строка 58, метод updateStatus()

Что плохо:

java
throw new RuntimeException("Invalid status");
Почему плохо:
RuntimeException возвращает 500 Internal Server Error. Но невалидный статус — это ошибка клиента (4xx). Клиент должен получить 400 Bad Request.

Как исправить:

java
throw new InvalidStatusException("Invalid status: " + status + ". Allowed: ACTIVE, INACTIVE");
// GlobalExceptionHandler маппит на 400 Bad Request
Issue #10: return null вместо 404
Категория: Error Handling
Приоритет: MAJOR
Местоположение: InviteeController.java, строка 28, метод getById()

Что плохо:

java
return repository.findById(id).orElse(null);
Почему плохо:
Когда ресурс не найден, метод возвращает null с HTTP статусом 200 OK. Клиент не может отличить успешный ответ от отсутствия данных. Согласно RFC 7231, должен возвращаться 404 Not Found.

Как исправить:

java
return repository.findById(id)
    .map(mapper::toResponse)
    .map(ResponseEntity::ok)
    .orElse(ResponseEntity.notFound().build());
Категория 4: Code Quality
Issue #11: Бизнес-логика в контроллере
Категория: Code Quality
Приоритет: MAJOR
Местоположение: InviteeController.java, строки 52-58, метод updateStatus()

Что плохо:

java
if (status.equals("ACTIVE") || status.equals("INACTIVE")) {
    invitee.setStatus(status);
} else {
    throw new RuntimeException("Invalid status");
}
Почему плохо:
Контроллер нарушает Single Responsibility Principle — должен заниматься только HTTP layer. Бизнес-логика не может быть переиспользована другими компонентами.

Как исправить:
Вынести валидацию статуса в InviteeService:

java
@PutMapping("/invitees/{id}/status")
public ResponseEntity<InviteeResponse> updateStatus(@PathVariable UUID id, @Valid @RequestBody UpdateStatusRequest request) {
    InviteeResponse updated = service.updateStatus(id, request.status());
    return ResponseEntity.ok(updated);
}
Issue #12: Дублирование обработки ошибок
Категория: Code Quality
Приоритет: MAJOR
Местоположение: Несколько методов контроллера

Что плохо:
Каждый метод должен содержать try-catch для обработки ошибок (в текущей версии catch только в updateStatus, но паттерн требует дублирования).

Почему плохо:
Дублирование error handling кода нарушает DRY принцип.

Как исправить:
Централизованный @RestControllerAdvice с @ExceptionHandler обрабатывает все исключения из всех контроллеров.

Issue #13: Hardcoded значения
Категория: Code Quality
Приоритет: MINOR
Местоположение: InviteeController.java, строка 53, метод updateStatus()

Что плохо:

java
if (status.equals("ACTIVE") || status.equals("INACTIVE")) {
Почему плохо:
Строковые литералы для допустимых статусов дублируются. Лучше использовать Enum.

Как исправить:

java
public enum InviteeStatus {
    ACTIVE, INACTIVE
}