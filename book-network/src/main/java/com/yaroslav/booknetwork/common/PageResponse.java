package com.yaroslav.booknetwork.common;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse<T> {

    List<T> content;
    int number;
    int size;
    long totalElements;
    int totalPages;
    boolean first;
    boolean last;
}
