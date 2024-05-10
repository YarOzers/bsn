package com.yaroslav.booknetwork.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yaroslav.booknetwork.book.Book;
import com.yaroslav.booknetwork.history.BookTransactionHistory;
import com.yaroslav.booknetwork.role.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter // генерирует геттеры для всех полей класса
@Setter // генерирует сеттеры для всех полей класса
@Builder // генерирует метод для создания объектов класса с помощью builder-паттерна
@AllArgsConstructor // генерирует конструктор с параметрами для всех полей класса
@NoArgsConstructor // генерирует пустой конструктор для класса
@Entity // указывает, что класс является сущностью для работы с базой данных
@Table(name = "_user") // указывает имя таблицы в базе данных для данной сущности
@EntityListeners(AuditingEntityListener.class)
// указывает, что для данной сущности нужно использовать класс EntityListener для обработки событий изменения данных
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class User implements UserDetails, Principal {

    @Id // указывает, что это поле является первичным ключом сущности
    @GeneratedValue // указывает, что значение поля будет генерироваться автоматически
    Integer id;
    String firstname;
    String lastname;
    LocalDate dateOfBirth;
    @Column(unique = true) // указывает, что значение поля должно быть уникальным в таблице
    String email;
    String password;
    boolean accountLocked;
    boolean enabled;


    // указывает, что это поле будет автоматически заполняться текущей датой и временем при создании объекта
    @Column(nullable = false, updatable = false)
    // указывает на имя столбца в базе данных для данного поля и его свойства
    @CreatedDate
    @JsonIgnore
    LocalDate createdDate;


    // указывает, что это поле будет автоматически заполняться текущей датой и временем при последнем изменении объекта
    @Column(insertable = false) // указывает на имя столбца в базе данных для данного поля и его свойства
    @JsonIgnore
    @LastModifiedDate
    LocalDate lastModifiedDate;


    @ManyToMany(fetch = FetchType.EAGER) // указывает на многие-ко-многим отношение между классами User и Role и способ загрузки связанных объектов
    List<Role> roles;

    @OneToMany(mappedBy = "owner")
    List<Book> books;

    @OneToMany(mappedBy = "user")
    List<BookTransactionHistory> histories;


    @Override
    public String getName() {
        return email;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<? extends GrantedAuthority> rol = this.roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return rol;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getFullName() {
        return getFirstname() + " " + getLastname();
    }
}
