import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {FormsModule} from "@angular/forms";
import {QuillEditorComponent} from "ngx-quill";

@Component({
  selector: 'app-create-article',
  templateUrl: './create-article.component.html',
  standalone: true,
  imports: [
    FormsModule,
    QuillEditorComponent
  ],
  styleUrls: ['./create-article.component.scss']
})
export class CreateArticleComponent {
  article = {
    title: '',
    content: ''
  };

  constructor(private http: HttpClient) {}

  onSubmit() {
    this.http.post('URL_TO_YOUR_API/articles', this.article).subscribe(response => {
      console.log('Article saved', response);
    });
  }
}
