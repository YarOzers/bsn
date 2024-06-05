package com.yaroslav.booknetwork.book;

// Импорт необходимых классов и интерфейсов из различных пакетов

import com.yaroslav.booknetwork.common.PageResponse;
import com.yaroslav.booknetwork.exception.OperationNotPermittedException;
import com.yaroslav.booknetwork.file.FileStorageService;
import com.yaroslav.booknetwork.history.BookTransactionHistory;
import com.yaroslav.booknetwork.history.BookTransactionHistoryRepository;
import com.yaroslav.booknetwork.user.User; // Класс User представляет пользователя системы
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor; // Аннотация, создающая конструктор, принимающий обязательные параметры
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication; // Интерфейс Authentication используется для получения информации о текущем аутентифицированном пользователе
import org.springframework.stereotype.Service; // Аннотация Service используется для обозначения сервисных классов в Spring
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

/**
 * Сервис для работы с книгами. Предоставляет методы для сохранения и обработки данных, связанных с книгами.
 */
@Service
@RequiredArgsConstructor // Lombok автоматически создает конструктор, принимающий final-поля
public class BookService {

    // Репозиторий для взаимодействия с базой данных, касающейся объектов Book
    private final BookRepository bookRepository;

    // Маппер для преобразования данных из запросов в объекты Book
    private final BookMapper bookMapper;

    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final FileStorageService fileStorageService;

    /**
     * Сохраняет новую книгу в репозитории, связывая ее с владельцем.
     *
     * @param request       запрос, содержащий данные о новой книге
     * @param connectedUser текущий аутентифицированный пользователь, который будет считаться владельцем книги
     * @return идентификатор сохраненной книги
     */
    public Integer save(BookRequest request, Authentication connectedUser) {
        // Получение информации о текущем пользователе из аутентификации
//        User user = ((User) connectedUser.getPrincipal());

        // Преобразование данных запроса в объект Book с помощью маппера
        Book book = bookMapper.toBook(request);

        // Установка владельца книги на текущего пользователя
//        book.setOwner(user);

        // Сохранение книги в репозитории и возврат идентификатора сохраненной книги
        return bookRepository.save(book).getId();
    }

    /**
     * Находит книгу по идентификатору и возвращает объект BookResponse.
     *
     * @param bookId Идентификатор книги, которую нужно найти
     * @return Объект BookResponse, представляющий найденную книгу
     * @throws EntityNotFoundException Если книга с данным идентификатором не найдена
     */
    public BookResponse findById(Integer bookId) { // Метод, который принимает идентификатор книги и возвращает объект BookResponse
        // Ищет книгу по идентификатору в репозитории
        return bookRepository.findById(bookId) // Возвращает Optional<Book>
                .map(bookMapper::toBookResponse) // Преобразует найденный объект Book в объект BookResponse
                .orElseThrow(() -> new EntityNotFoundException( // Если Optional пустой, выбрасывает исключение
                        "No book found with the ID:: " + bookId)); // Сообщение об ошибке, если книга не найдена
    }

    /**
     * Метод для поиска всех книг с поддержкой постраничного отображения.
     *
     * @param page          Номер страницы для запроса (начиная с 0).
     * @param size          Размер страницы (количество элементов на странице).
     * @param connectedUser Аутентифицированный пользователь, который запрашивает книги.
     * @return Объект PageResponse<BookResponse>, содержащий информацию о найденных книгах.
     */
    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        // Получение объекта пользователя из текущей аутентификации
//        User user = ((User) connectedUser.getPrincipal());

        // Определение параметров постраничного запроса: номер страницы, размер страницы и сортировка по дате создания по убыванию
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        // Выполнение запроса для получения всех отображаемых книг с учетом постраничного запроса и идентификатора пользователя
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, connectedUser.getName());

        // Преобразование страницы книг в список объектов BookResponse с использованием метода map
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse) // Использует метод bookMapper::toBookResponse для преобразования каждой книги
                .toList(); // Преобразует поток в список

        // Возвращает новый объект PageResponse, содержащий информацию о странице книг
        return new PageResponse<>(
                bookResponses, // Список преобразованных ответов на запрос книг
                books.getNumber(), // Номер текущей страницы
                books.getSize(), // Размер страницы
                books.getTotalElements(), // Общее количество элементов
                books.getTotalPages(), // Общее количество страниц
                books.isFirst(), // Флаг, указывающий, является ли эта страница первой
                books.isLast() // Флаг, указывающий, является ли эта страница последней
        );
    }

    //    используется для поиска всех книг, принадлежащих определенному владельцу
    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        // Получение объекта аутентификации и извлечение идентификатора владельца из объекта Principal
//        User user = ((User) connectedUser.getPrincipal());

        // Создание объекта Pageable для поддержки пагинации и сортировки
        Pageable pageable = PageRequest.of(
                page, // номер страницы
                size, // размер страницы
                Sort.by("createdDate").descending() // сортировка по убыванию даты создания
        );

        // Выполнение запроса для поиска книг, принадлежащих владельцу с идентификатором user.getId()
        Page<Book> books = bookRepository.findAll(BookSpecification.withOwnerId(connectedUser.getName()), pageable);

        // Преобразование страниц книг в список объектов BookResponse
        List<BookResponse> bookResponses = books.stream() // превращаем список в поток
                .map(bookMapper::toBookResponse) // применяем метод bookMapper::toBookResponse для преобразования книги в BookResponse
                .toList(); // возвращаем в виде списка

        // Возвращаем объект PageResponse, который содержит информацию о странице книг и метаданные пагинации
        return new PageResponse<>(
                bookResponses, // список преобразованных ответов
                books.getNumber(), // номер текущей страницы
                books.getSize(), // размер текущей страницы
                books.getTotalElements(), // общее количество элементов
                books.getTotalPages(), // общее количество страниц
                books.isFirst(), // является ли это первая страница
                books.isLast() // является ли это последняя страница
        );
    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
//        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(
                page, // номер страницы
                size, // размер страницы
                Sort.by("createdDate").descending()); // сортировка по убыванию даты создания
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.finAllBorrowedBooks(pageable, connectedUser.getName());
        List<BorrowedBookResponse> bookResponses = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses, // список преобразованных ответов
                allBorrowedBooks.getNumber(), // номер текущей страницы
                allBorrowedBooks.getSize(), // размер текущей страницы
                allBorrowedBooks.getTotalElements(), // общее количество элементов
                allBorrowedBooks.getTotalPages(), // общее количество страниц
                allBorrowedBooks.isFirst(), // является ли это первая страница
                allBorrowedBooks.isLast()
        );// является ли это последняя страница
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
//        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(
                page, // номер страницы
                size, // размер страницы
                Sort.by("createdDate").descending()); // сортировка по убыванию даты создания
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.finAllReturnedBooks(pageable, connectedUser.getName());
        List<BorrowedBookResponse> bookResponses = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses, // список преобразованных ответов
                allBorrowedBooks.getNumber(), // номер текущей страницы
                allBorrowedBooks.getSize(), // размер текущей страницы
                allBorrowedBooks.getTotalElements(), // общее количество элементов
                allBorrowedBooks.getTotalPages(), // общее количество страниц
                allBorrowedBooks.isFirst(), // является ли это первая страница
                allBorrowedBooks.isLast()
        );// является ли это последняя страница
    }

    public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: " + bookId));
//        User user = ((User) connectedUser.getPrincipal());
        if (!Objects.equals(book.getCreatedBy(), connectedUser.getName())) {
            throw new OperationNotPermittedException("You can't update others books shareable status");
        }
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return null;
    }

    public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: " + bookId));
//        User user = ((User) connectedUser.getPrincipal());
        if (!Objects.equals(book.getCreatedBy(),connectedUser.getName())) {
            throw new OperationNotPermittedException("You can't update others books archived status");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return null;
    }


    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: " + bookId));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable");
        }
//        User user = ((User) connectedUser.getPrincipal());
        if (Objects.equals(book.getCreatedBy(), connectedUser.getName())) {
            throw new OperationNotPermittedException("You can't borrow your own book");
        }
        final boolean isAlreadyBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookId, connectedUser.getName());
        if (isAlreadyBorrowed) {
            throw new OperationNotPermittedException("The requested book is already borrowed");
        }
        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .userId(connectedUser.getName())
                .book(book)
                .returnApproved(false)
                .build();
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer returnBorrowedBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: " + bookId));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable");
        }
//        User user = ((User) connectedUser.getPrincipal());
        if (Objects.equals(book.getCreatedBy(), connectedUser.getName())) {
            throw new OperationNotPermittedException("You can't borrow or return your own book");
        }
        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndUserId(bookId, connectedUser.getName())
                .orElseThrow(() -> new EntityNotFoundException("You did not borrow this book"));
        bookTransactionHistory.setReturned(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer approveReturnBorrowedBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: " + bookId));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable");
        }
//        User user = ((User) connectedUser.getPrincipal());
        if (!Objects.equals(book.getCreatedBy(), connectedUser.getName())) {
            throw new OperationNotPermittedException("You can't return a book that you do not own");
        }
        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndOwnerId(bookId, connectedUser.getName())
                .orElseThrow(() -> new OperationNotPermittedException("The book is not returned yet. You cannot approve its return"));
        bookTransactionHistory.setReturnApproved(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }


    /**
     * Загрузка обложки книги
     *
     * @param file          Загружаемый файл, представляющий обложку книги
     * @param connectedUser Аутентифицированный пользователь
     * @param bookId        Идентификатор книги, для которой загружается обложка
     */
    public void uploadBookCoverPicture(MultipartFile file, Authentication connectedUser, Integer bookId) {
        // Поиск книги по идентификатору
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: " + bookId)); // Если книга не найдена, выбрасывается исключение

        // Получение идентификации аутентифицированного пользователя
//        User user = ((User) connectedUser.getPrincipal());

        // Сохранение файла обложки в файловой системе или облачном хранилище
        var bookCover = fileStorageService.saveFile(file, connectedUser.getName()); // Сохраняет файл и возвращает путь к сохраненному файлу

        // Установка сохраненного пути обложки в объект книги
        book.setBookCover(bookCover); // Обновляет обложку в сущности книги

        // Сохранение книги с обновленной обложкой
        bookRepository.save(book); // Сохраняет обновленный объект книги в базе данных
    }
}
