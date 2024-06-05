package com.yaroslav.booknetwork.history;

import com.yaroslav.booknetwork.book.Book;
import com.yaroslav.booknetwork.common.BaseEntity;
import com.yaroslav.booknetwork.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookTransactionHistory extends BaseEntity {

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    User user;
    @Column(name = "user_id")
    private String userId;
    @ManyToOne
    @JoinColumn(name = "book_id")
    Book book;

    boolean returned;
    boolean returnApproved;

}
