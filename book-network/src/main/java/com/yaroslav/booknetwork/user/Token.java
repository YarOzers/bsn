package com.yaroslav.booknetwork.user;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Entity
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token {

    @Id
    @GeneratedValue
    Integer id;

    String token;

    Date createdAt;

    Date expiresAt;

    Date validatedAt;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

}
