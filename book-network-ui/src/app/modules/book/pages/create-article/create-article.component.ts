import { Component } from '@angular/core';
import {FormsModule} from "@angular/forms";
import {QuillEditorComponent} from "ngx-quill";
import {Article} from "../../../../services/models/article";
import {ArticleService} from "../../../../services/services/article.service";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";

@Component({
  selector: 'app-create-article',
  templateUrl: './create-article.component.html',
  standalone: true,
  imports: [
    FormsModule,
    QuillEditorComponent,
    RouterLink
  ],
  styleUrls: ['./create-article.component.scss']
})
export class CreateArticleComponent {
  article: Article = {
    id: 0,
    title: '',
    content: '',
    themeId: 0
  };
  themeId: number | null = null;
  required: boolean = true;

  constructor(private route: ActivatedRoute, private articleService: ArticleService) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params: { [key: string]: any }) => {
      this.themeId = +params['themeId'] || null;
    });
  }

  onSubmit(): void {
    if (this.themeId) {
      this.article.themeId = this.themeId;
      this.articleService.createArticle(this.article).subscribe(response => {
        // Обработка успешного ответа
      });
    }
  }
}
