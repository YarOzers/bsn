import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import {Observable} from "rxjs";
import {Article} from "../models/article";
import { Theme } from "../models/theme";

@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  private apiUrl = 'http://localhost:9090/api/v1'; // Базовый URL вашего API

  constructor(private http: HttpClient) {}

  getThemes(): Observable<Theme[]> {
    return this.http.get<Theme[]>(`${this.apiUrl}/themes`);
  }

  getArticlesByTheme(themeId: number): Observable<Article[]> {
    return this.http.get<Article[]>(`${this.apiUrl}/themes/${themeId}/articles`);
  }

  getArticle(articleId: number): Observable<Article> {
    return this.http.get<Article>(`${this.apiUrl}/articles/${articleId}`);
  }

  createArticle(article: Article): Observable<Article> {
    return this.http.post<Article>(`${this.apiUrl}/articles`, article);
  }

  editArticle(article: Article): Observable<Article> {
    return this.http.put<Article>(`${this.apiUrl}/articles/${article.id}`, article);
  }

  deleteArticle(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/articles/${id}`);
  }

  addTheme(name: string): Observable<Theme> {
    return this.http.post<Theme>(`${this.apiUrl}/themes`, { name });
  }

  editTheme(id: number, name: string): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/themes/${id}`, { name });
  }

  deleteTheme(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/themes/${id}`);
  }
}
