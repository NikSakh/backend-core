# REST Controller Code Review Checklist

## Категория 1: API Design (5 проблем)

### 1.1 Неправильные HTTP методы
**Приоритет:** CRITICAL  
**Что искать:** POST используется для чтения данных, GET для модификации

**Плохо:**
```java
@PostMapping("/getInvitees") // Глагол в URL + неправильный метод
public List<Invitee> getInvitees() { ... }
Хорошо:

java
@GetMapping("/invitees") // Существительное + правильный HTTP метод
public ResponseEntity<List<InviteeResponse>> getInvitees() { ... }
1.2 Неправильные статус коды
Приоритет: CRITICAL
Что искать: 200 для всех операций, отсутствие 201/204/404

Плохо:

java
@PostMapping("/invitees")
public Invitee create(@RequestBody Invitee invitee) {
    return service.save(invitee); // Всегда 200 OK
}
Хорошо:

java
@PostMapping("/invitees")
public ResponseEntity<InviteeResponse> create(@Valid @RequestBody CreateInviteeRequest request) {
    InviteeResponse created = service.create(request);
    URI location = URI.create("/api/invitees/" + created.id());
    return ResponseEntity.created(location).body(created); // 201 Created + Location header
}
1.3 Плохой naming: глаголы в URL
Приоритет: MAJOR
Что искать: /getInvitees, /createInvitee, /updateInviteeStatus в URLs

Плохо:

java
@GetMapping("/getInvitees") // RPC стиль
@PostMapping("/createInvitee")
Хорошо:

java
@GetMapping("/invitees") // RESTful стиль
@PostMapping("/invitees")
1.4 Entity вместо DTO в response
Приоритет: CRITICAL (security + coupling)
Что искать: Возврат JPA Entity классов напрямую

Плохо:

java
@GetMapping("/invitees/{id}")
public Invitee getById(@PathVariable UUID id) {
    return repository.findById(id).orElseThrow(); // Entity с JPA annotations, internal fields
}
Хорошо:

java
@GetMapping("/invitees/{id}")
public ResponseEntity<InviteeResponse> getById(@PathVariable UUID id) {
    Invitee invitee = service.getById(id);
    return ResponseEntity.ok(mapper.toResponse(invitee)); // DTO без internal fields
}
1.5 Нет пагинации для списков
Приоритет: MAJOR (performance)
Что искать: GET endpoints возвращающие List без параметров page/size

Плохо:

java
@GetMapping("/invitees")
public List<Invitee> getAll() {
    return repository.findAll(); // Может вернуть 10,000 записей
}
Хорошо:

java
@GetMapping("/invitees")
public ResponseEntity<Page<InviteeResponse>> getAll(
    @PageableDefault(size = 20) Pageable pageable) {
    Page<Invitee> page = repository.findAll(pageable);
    return ResponseEntity.ok(page.map(mapper::toResponse));
}
Категория 2: Security (5 проблем)
2.1 SQL injection через конкатенацию
Приоритет: CRITICAL
Что искать: String concatenation в SQL запросах

Плохо:

java
String sql = "SELECT * FROM invitees WHERE email = '" + email + "'";
// Injection: email = "admin' OR '1'='1"
Хорошо:

java
// Spring Data JPA method
Invitee findByEmail(String email); // Автоматическое экранирование

// Или PreparedStatement
PreparedStatement ps = conn.prepareStatement("SELECT * FROM invitees WHERE email = ?");
ps.setString(1, email);
2.2 Exposure внутренних полей
Приоритет: CRITICAL
Что искать: password, internalId, version, createdBy в response

Плохо:

java
@Data
@Entity
public class User {
    private UUID id;
    private String email;
    private String password; // НИКОГДА не должно попасть в response
    private String internalSystemId; // Internal field
    @Version private Long version; // JPA optimistic locking
}

@GetMapping("/users/{id}")
public User getUser(@PathVariable UUID id) {
    return userRepo.findById(id).orElseThrow(); // Вернёт ВСЕ поля включая password
}
Хорошо:

java
public record UserResponse(UUID id, String email, String firstName) {} // Только публичные поля

@GetMapping("/users/{id}")
public ResponseEntity<UserResponse> getUser(@PathVariable UUID id) {
    User user = userService.getById(id);
    return ResponseEntity.ok(userMapper.toResponse(user)); // password не попадёт в JSON
}
2.3 Нет валидации входных данных
Приоритет: CRITICAL
Что искать: @RequestBody без @Valid, нет Bean Validation аннотаций

Плохо:

java
@PostMapping("/invitees")
public Invitee create(@RequestBody Invitee invitee) { // Нет @Valid
    return service.save(invitee); // Любые данные принимаются
}
Хорошо:

java
public record CreateInviteeRequest(
    @NotBlank @Email String email,
    @NotBlank @Size(min = 2, max = 50) String firstName
) {}

@PostMapping("/invitees")
public ResponseEntity<InviteeResponse> create(@Valid @RequestBody CreateInviteeRequest request) {
    InviteeResponse created = service.create(request);
    return ResponseEntity.created(location).body(created);
}
2.4 Stack trace в error response
Приоритет: CRITICAL
Что искать: Exception.printStackTrace() или дефолтный Spring error response с trace

Плохо:

java
@GetMapping("/invitees/{id}")
public Invitee getById(@PathVariable UUID id) {
    try {
        return repository.findById(id).orElseThrow();
    } catch (Exception e) {
        e.printStackTrace();
        throw e;
    }
}
Хорошо:

java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(EntityNotFoundException ex) {
        logger.error("Entity not found", ex); // Full stack trace в server logs
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND, "Resource not found");
        return ResponseEntity.status(404).body(problem);
    }
}
2.5 Missing authorization checks
Приоритет: CRITICAL
Что искать: Отсутствие проверок @PreAuthorize

Плохо:

java
@DeleteMapping("/invitees/{id}")
public ResponseEntity<Void> delete(@PathVariable UUID id) {
    service.delete(id); // Любой пользователь может удалить
    return ResponseEntity.noContent().build();
}
Хорошо:

java
@DeleteMapping("/invitees/{id}")
@PreAuthorize("hasRole('ADMIN') or @inviteeService.isOwner(#id, authentication.name)")
public ResponseEntity<Void> delete(@PathVariable UUID id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
}
Категория 3: Error Handling (4 проблемы)
3.1 Пустые catch блоки
Приоритет: MAJOR
Что искать: catch (Exception e) {} или catch блоки с только комментарием

Плохо:

java
try {
    return repository.findById(id).orElseThrow();
} catch (Exception e) {
    return null;
}
Хорошо:

java
Invitee invitee = service.getById(id); // Выбросит EntityNotFoundException
return ResponseEntity.ok(mapper.toResponse(invitee));
3.2 500 на бизнес-ошибки вместо 4xx
Приоритет: MAJOR
Что искать: Бизнес-exceptions возвращают 500

Плохо:

java
if (repository.existsByEmail(invitee.getEmail())) {
    throw new RuntimeException("Email exists"); // 500 Internal Server Error
}
Хорошо:

java
throw new EmailAlreadyExistsException(request.email());
// GlobalExceptionHandler маппит на 409 Conflict
3.3 Generic error messages без деталей
Приоритет: MINOR
Что искать: "Error occurred", "Something went wrong" без context

Плохо:

json
{"error": "Error occurred"}
Хорошо:

json
{
  "type": "/errors/validation",
  "title": "Validation Error",
  "status": 400,
  "detail": "Request validation failed for 2 fields",
  "errors": {
    "email": "Email is required",
    "firstName": "First name must be between 2 and 50 characters"
  }
}
3.4 Нет логирования ошибок
Приоритет: MAJOR
Что искать: Exceptions обрабатываются но не логируются

Плохо:

java
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
    return ResponseEntity.status(500).body(new ErrorResponse("Internal error"));
}
Хорошо:

java
@ExceptionHandler(Exception.class)
public ResponseEntity<ProblemDetail> handleGeneric(Exception ex, HttpServletRequest request) {
    logger.error("Unexpected error for request: {} {}", request.getMethod(), request.getRequestURI(), ex);
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(
        HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    return ResponseEntity.status(500).body(problem);
}
Категория 4: Code Quality (4 проблемы)
4.1 Бизнес-логика в контроллере
Приоритет: MAJOR (violates SRP)
Что искать: if/else бизнес-правила, расчёты, обращения к нескольким repositories

Плохо:

java
@PostMapping("/invitees")
public ResponseEntity<Invitee> create(@RequestBody Invitee invitee) {
    if (repository.existsByEmail(invitee.getEmail())) {
        throw new EmailAlreadyExistsException(invitee.getEmail());
    }
    invitee.setCreatedAt(Instant.now());
    Invitee saved = repository.save(invitee);
    return ResponseEntity.created(location).body(saved);
}
Хорошо:

java
@PostMapping("/invitees")
public ResponseEntity<InviteeResponse> create(@Valid @RequestBody CreateInviteeRequest request) {
    InviteeResponse created = inviteeService.create(request);
    URI location = URI.create("/api/invitees/" + created.id());
    return ResponseEntity.created(location).body(created);
}
4.2 Дублирование кода
Приоритет: MAJOR (violates DRY)
Что искать: Одинаковый try-catch в каждом методе

Плохо: Одинаковые try-catch блоки в каждом методе контроллера.

Хорошо: GlobalExceptionHandler обрабатывает все исключения централизованно.

4.3 God Controller: слишком много методов
Приоритет: MINOR (violates Cohesion)
Что искать: Контроллер с 20+ методами для несвязанных операций

Плохо: Один контроллер на CRUD + конверсию + уведомления + отчёты.

Хорошо: Разделение на InviteeController, InviteeConversionController, InviteeNotificationController.

4.4 Hardcoded values
Приоритет: MINOR
Что искать: Magic numbers, hardcoded URLs, roles в коде

Плохо:

java
@GetMapping("/invitees")
public List<Invitee> getAll(@RequestParam(defaultValue = "20") int size) { }
@PreAuthorize("hasRole('ROLE_ADMIN')")
Хорошо:

java
@GetMapping("/invitees")
public ResponseEntity<Page<InviteeResponse>> getAll(@PageableDefault(size = 20) Pageable pageable) { }
@PreAuthorize("hasRole(@appProperties.roles().admin())")