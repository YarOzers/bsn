import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {NgIf} from "@angular/common";

interface Article {
  id: number;
  title: string;
  content: string;
}
@Component({
  selector: 'app-article-detail',
  standalone: true,
  imports: [
    NgIf
  ],
  templateUrl: './article-detail.component.html',
  styleUrl: './article-detail.component.scss'
})
export class ArticleDetailComponent implements OnInit {

  article: Article | undefined;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')!;
    this.getArticle(id);
  }

  getArticle(id: number): void {
    this.http.get<Article>(`URL_TO_YOUR_API/articles/${id}`)
      .subscribe(response => {
        this.article = response;
      });
  }
}
