package com.yaroslav.booknetwork.book;

import org.springframework.data.jpa.domain.Specification; // Импорт спецификаций JPA

/**
 * Класс, который содержит спецификации для работы с запросами к сущности Book.
 * Спецификации позволяют строить гибкие критерии для выборки данных из базы данных.
 */
public class BookSpecification {

    /**
     * Создает спецификацию для поиска книг по идентификатору владельца (ownerId).
     *
     * @param ownerId Идентификатор владельца книг.
     * @return Спецификация для поиска книг, принадлежащих владельцу с заданным ID.
     */
    public static Specification<Book> withOwnerId(Integer ownerId) {
        // Возвращает спецификацию, которая создает критерий равенства для поля "id" в ассоциации "owner"
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("owner") // Получает связь "owner" из текущей сущности
                                .get("id"),    // Получает поле "id" из ассоциации "owner"
                        ownerId // Сравнивает с заданным идентификатором владельца
                );
    }
}
