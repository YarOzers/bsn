import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {RouterLink} from "@angular/router";
import {ArticleService} from "../../../../services/services/article.service";
import {SafeHtmlPipe} from "../../../../pipes/safe-html.pipe";

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
    RouterLink,
    SafeHtmlPipe,
    NgIf
  ],
  templateUrl: './article-list.component.html',
  styleUrl: './article-list.component.scss'
})
export class ArticleListComponent implements OnInit{
  articles: Article[] = [];
  selectedArticleId: number | null = null;
  expandedArticleIds: Set<number> = new Set<number>();

  constructor(private articleService: ArticleService) {}

  ngOnInit(): void {
    this.articleService.getArticles().subscribe((data) => {
      this.articles = data as Article[];
    });
  }

  toggleContent(articleId: number): void {
    if (this.expandedArticleIds.has(articleId)) {
      this.expandedArticleIds.delete(articleId);
    } else {
      this.expandedArticleIds.clear();
      this.expandedArticleIds.add(articleId);
    }
  }

  isContentExpanded(articleId: number): boolean {
    return this.expandedArticleIds.has(articleId);
  }
}
