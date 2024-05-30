import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {NgForOf} from "@angular/common";
import {RouterLink} from "@angular/router";

interface Article {
  id: number;
  title: string;
  content: string;
}
@Component({
  selector: 'app-article-list',
  standalone: true,
  imports: [
    NgForOf,
    RouterLink
  ],
  templateUrl: './article-list.component.html',
  styleUrl: './article-list.component.scss'
})
export class ArticleListComponent implements OnInit{
  articles: Article[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchArticles();
  }

  fetchArticles(): void {
    this.http.get<Article[]>('URL_TO_YOUR_API/articles')
      .subscribe(response => {
        this.articles = response;
      });
  }
}
