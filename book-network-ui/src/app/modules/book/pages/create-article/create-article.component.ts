import { Component } from '@angular/core';
import {FormsModule} from "@angular/forms";
import {QuillEditorComponent} from "ngx-quill";
import {Article} from "../../../../services/models/article";
import {ArticleService} from "../../../../services/services/article.service";
import {Router, RouterLink} from "@angular/router";

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
  article: Article = { title: '', content: '' };
  required: boolean = true;


  constructor(private articleService: ArticleService, private router: Router) {}

  onSubmit(): void {
    this.articleService.createArticle(this.article).subscribe(() => {
      this.router.navigate(['/books/articles']);
    });
  }
}
