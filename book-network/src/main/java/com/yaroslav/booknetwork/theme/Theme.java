package com.yaroslav.booknetwork.theme;

import com.yaroslav.booknetwork.article.Article;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Table(name = "themes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Theme {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "theme", cascade = CascadeType.ALL)
    private List<Article> articles; // Если вы хотите хранить список статей в каждой теме
}
