package com.yaroslav.booknetwork.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    public Article editArticle(Article editedArticle) {
        // Проверка, существует ли статья с таким ID
        Optional<Article> existingArticleOptional = articleRepository.findById(editedArticle.getId());
        if (existingArticleOptional.isPresent()) {
            // Если статья существует, обновляем ее поля
            Article existingArticle = existingArticleOptional.get();
            existingArticle.setTitle(editedArticle.getTitle());
            existingArticle.setContent(editedArticle.getContent());
            // Сохраняем обновленную статью и возвращаем ее
            return articleRepository.save(existingArticle);
        } else {
            // Если статья с таким ID не существует, можете выбрать соответствующее действие,
            // например, бросить исключение или вернуть null
            throw new IllegalArgumentException("Article with ID " + editedArticle.getId() + " not found");
        }
    }
}
