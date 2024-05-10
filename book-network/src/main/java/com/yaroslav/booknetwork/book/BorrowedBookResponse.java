package com.yaroslav.booknetwork.book;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BorrowedBookResponse {

    Integer id;
    String title;
    String authorName;
    String isbn;
    double rate;
    boolean returned;
    boolean returnApproved;
}
