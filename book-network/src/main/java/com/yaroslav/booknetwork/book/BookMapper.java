package com.yaroslav.booknetwork.book;

// Импорт Spring Framework

import com.yaroslav.booknetwork.file.FileUtils;
import com.yaroslav.booknetwork.history.BookTransactionHistory;
import org.springframework.stereotype.Service; // Аннотация, указывающая, что это компонент службы

/**
 * Класс BookMapper отвечает за преобразование данных между объектами BookRequest и сущностями Book.
 * Аннотация @Service указывает, что этот класс является компонентом сервиса в Spring,
 * который может быть внедрен в другие части приложения.
 */
@Service // Указывает, что этот класс является сервисом, который может быть инжектирован в другие классы
public class BookMapper { // Класс, ответственный за преобразование объектов BookRequest в объекты Book

    /**
     * Преобразует объект BookRequest в объект Book.
     *
     * @param request Объект BookRequest, содержащий данные для создания или обновления книги
     * @return Новый объект Book, созданный на основе данных из BookRequest
     */
    public Book toBook(BookRequest request) {
        // Использует паттерн "строитель" для создания нового объекта Book на основе данных из BookRequest
        return Book.builder() // Используем "строитель", чтобы легко создать объект Book
                .id(request.id()) // Устанавливает идентификатор книги (может быть null для новых объектов)
                .title(request.title()) // Устанавливает название книги
                .authorName(request.authorName()) // Устанавливает имя автора
                .synopsis(request.synopsis()) // Устанавливает краткое описание книги
                .archived(false) // По умолчанию книга не архивирована
                .shareable(request.shareable()) // Устанавливает, можно ли делить или передавать книгу
                .build(); // Завершает создание объекта Book
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder() // Используем шаблон "строитель" для создания объекта BookResponse
                .id(book.getId()) // Устанавливаем идентификатор книги
                .title(book.getTitle()) // Устанавливаем название книги
                .authorName(book.getAuthorName()) // Устанавливаем имя автора
                .isbn(book.getIsbn()) // Устанавливаем ISBN (Международный стандартный книжный номер)
                .synopsis(book.getSynopsis()) // Устанавливаем краткое описание или аннотацию
                .rate(book.getRate()) // Устанавливаем рейтинг книги
                .archived(book.isArchived()) // Устанавливаем флаг, указывающий, что книга архивирована
                .shareable(book.isShareable()) // Устанавливаем флаг, указывающий, что книгу можно делить или передавать
                .owner(book.getOwner().getFullName()) // Устанавливаем полное имя владельца книги
                .cover(FileUtils.readFileFromLocation(book.getBookCover())) // Читаем обложку книги с помощью FileUtils и устанавливаем
                .build(); // Завершаем создание объекта BookResponse и возвращаем его
    }



    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory history) {
        return BorrowedBookResponse.builder()
                .id(history.getBook().getId())
                .title(history.getBook().getTitle())
                .authorName(history.getBook().getAuthorName())
                .isbn(history.getBook().getIsbn())
                .rate(history.getBook().getRate())
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .build();
    }
}

