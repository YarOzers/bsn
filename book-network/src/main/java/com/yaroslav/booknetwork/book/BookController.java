package com.yaroslav.booknetwork.book;

// Импорт необходимых классов и аннотаций

import com.yaroslav.booknetwork.common.PageResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag; // Аннотация Tag используется для добавления метаданных в OpenAPI (Swagger)
import jakarta.validation.Valid; // Аннотация Valid применяется для валидации входящих данных
import lombok.RequiredArgsConstructor; // Автоматически создает конструктор, принимающий final-поля
import org.springframework.http.ResponseEntity; // Используется для представления HTTP-ответов
import org.springframework.security.core.Authentication; // Интерфейс Authentication для получения информации о текущем аутентифицированном пользователе
import org.springframework.web.bind.annotation.*; // Аннотации Spring Web, используемые для управления HTTP-запросами
import org.springframework.web.multipart.MultipartFile;

/**
 * Контроллер для операций с книгами. Обрабатывает HTTP-запросы, связанные с книгами,
 * и использует сервис BookService для выполнения бизнес-логики.
 */
@RestController // Указывает, что этот класс является контроллером Spring, который обрабатывает HTTP-запросы
@RequestMapping("books") // Устанавливает базовый URL для всех запросов, обрабатываемых этим контроллером
@RequiredArgsConstructor // Lombok создает конструктор, принимающий final-поля (для внедрения зависимостей)
@Tag(name = "Book") // Описывает контроллер в OpenAPI (Swagger) с меткой "Book"
public class BookController {

    // Сервис, используемый для операций с книгами
    private final BookService service;

    /**
     * Обрабатывает HTTP-запрос POST для сохранения новой книги.
     *
     * @param request       объект запроса, содержащий данные о новой книге
     * @param connectedUser текущий аутентифицированный пользователь
     * @return ResponseEntity с идентификатором сохраненной книги
     */
    @PostMapping // Аннотация, указывающая, что метод обрабатывает HTTP-запросы POST
    public ResponseEntity<Integer> saveBook(
            @Valid @RequestBody BookRequest request, // Обрабатывает тело запроса и применяет валидацию
            Authentication connectedUser // Получает информацию о текущем аутентифицированном пользователе
    ) {
        // Сохраняет книгу с помощью сервиса и возвращает HTTP-ответ с идентификатором сохраненной книги
        return ResponseEntity.ok(service.save(request, connectedUser)); // Возвращает успешный HTTP-ответ
    }

    /**
     * Метод для поиска книги по ее ID.
     *
     * @param bookId Идентификатор книги, передаваемый в URL.
     * @return HTTP-ответ с найденной книгой или с ошибкой, если книга не найдена.
     */
    @GetMapping("{book-id}") // Обработка GET-запроса с параметром в пути
    public ResponseEntity<BookResponse> findBookById(
            @PathVariable("book-id") Integer bookId // Извлечение bookId из пути URL
    ) {
        // Вызов метода из сервиса для поиска книги по ID
        BookResponse bookResponse = service.findById(bookId);

        // Возвращает успешный ответ с объектом BookResponse
        return ResponseEntity.ok(bookResponse);
    }

    /**
     * Метод для поиска всех книг с поддержкой постраничного отображения.
     *
     * @param page          Номер страницы, передаваемый в запросе.
     * @param size          Размер страницы, передаваемый в запросе.
     * @param connectedUser Аутентифицированный пользователь, который запрашивает книги.
     * @return HTTP-ответ со списком книг с поддержкой постраничного отображения.
     */
    @GetMapping // Обработка GET-запроса для получения всех книг
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page, // Извлечение параметра "page"
            @RequestParam(name = "size", defaultValue = "10", required = false) int size, // Извлечение параметра "size"
            Authentication connectedUser // Объект аутентификации для текущего пользователя
    ) {
        // Вызов метода из сервиса для получения всех книг с поддержкой постраничного отображения
        PageResponse<BookResponse> pageResponse = service.findAllBooks(page, size, connectedUser);

        // Возвращает успешный ответ с объектом PageResponse<BookResponse>
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page, // Извлечение параметра "page"
            @RequestParam(name = "size", defaultValue = "10", required = false) int size, // Извлечение параметра "size"
            Authentication connectedUser // Объект аутентификации для текущего пользователя
    ) {
        PageResponse<BookResponse> pageResponse = service.findAllBooksByOwner(page, size, connectedUser);
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllBorrowedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page, // Извлечение параметра "page"
            @RequestParam(name = "size", defaultValue = "10", required = false) int size, // Извлечение параметра "size"
            Authentication connectedUser // Объект аутентификации для текущего пользователя
    ) {
        PageResponse<BorrowedBookResponse> pageResponse = service.findAllBorrowedBooks(page, size, connectedUser);
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllReturnedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page, // Извлечение параметра "page"
            @RequestParam(name = "size", defaultValue = "10", required = false) int size, // Извлечение параметра "size"
            Authentication connectedUser // Объект аутентификации для текущего пользователя
    ) {
        PageResponse<BorrowedBookResponse> pageResponse = service.findAllReturnedBooks(page, size, connectedUser);
        return ResponseEntity.ok(pageResponse);
    }

    @PatchMapping("/shareable/{book-id}")
    public ResponseEntity<Integer> updateShareableStatus(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.updateShareableStatus(bookId, connectedUser));

    }

    @PatchMapping("/archived/{book-id}")
    public ResponseEntity<Integer> updateArchivedStatus(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.updateArchivedStatus(bookId, connectedUser));
    }

    @PostMapping("/borrow/{book-id}")
    public ResponseEntity<Integer> borrowBook(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(service.borrowBook(bookId, connectedUser));
    }

    @PatchMapping("/borrow/return/{book-id}")
    public ResponseEntity<Integer> returnBorrowBook(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(service.returnBorrowedBook(bookId,connectedUser));
    }

    @PatchMapping("/borrow/return/approve/{book-id}")
    public ResponseEntity<Integer> approveReturnBorrowBook(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(service.approveReturnBorrowedBook(bookId,connectedUser));
    }

    @PostMapping(value = "/cover/{book-id}", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadBookCoverPicture(
            @PathVariable("book-id") Integer bookId,
            @Parameter()
            @RequestParam("file") MultipartFile file,
            Authentication connectedUser
    ){
        service.uploadBookCoverPicture(file,connectedUser,bookId);
        return ResponseEntity.accepted().build();
    }
}
