package com.yaroslav.booknetwork.book;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookResponse {

    Integer id;
    String title;
    String authorName;
    String isbn;
    String synopsis;
    String owner;
    byte[] cover;
    double rate;
    boolean archived;
    boolean shareable;
}
