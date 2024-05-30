import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {NgIf} from "@angular/common";
import {ArticleService} from "../../../../services/services/article.service";
import {SafeHtmlPipe} from "../../../../pipes/safe-html.pipe";

interface Article {
  id: number;
  title: string;
  content: string;
}
@Component({
  selector: 'app-article-detail',
  standalone: true,
  imports: [
    NgIf,
    SafeHtmlPipe
  ],
  templateUrl: './article-detail.component.html',
  styleUrl: './article-detail.component.scss'
})
export class ArticleDetailComponent implements OnInit {
  article: Article | undefined;

  constructor(private route: ActivatedRoute, private articleService: ArticleService) {}

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')!;
    this.articleService.getArticle(id).subscribe((data) => {
      this.article = data as Article;
    });
  }
}
