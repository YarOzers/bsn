import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {Router, RouterLink, RouterOutlet} from "@angular/router";
import {ArticleService} from "../../../../services/services/article.service";
import {SafeHtmlPipe} from "../../../../pipes/safe-html.pipe";
import {Theme} from "../../../../services/models/theme";
import {Article} from "../../../../services/models/article";
import {FormsModule} from "@angular/forms";
import {ArticleDetailComponent} from "../article-detail/article-detail.component";

@Component({
  selector: 'app-article-list',
  standalone: true,
  imports: [
    NgForOf,
    RouterLink,
    SafeHtmlPipe,
    NgIf,
    FormsModule,
    RouterOutlet,
    ArticleDetailComponent
  ],
  templateUrl: './article-list.component.html',
  styleUrl: './article-list.component.scss'
})
export class ArticleListComponent implements OnInit{
  themes: Theme[] = [];
  selectedTheme: Theme | null = null;
  selectedArticle: Article | null = null;
  newThemeName: string = '';
  editingTheme: Theme | null = null;

  constructor(private articleService: ArticleService, private router: Router) {}

  ngOnInit(): void {
    this.loadThemes();
  }

  loadThemes(): void {
    this.articleService.getThemes().subscribe((data) => {
      this.themes = data as Theme[];
    });
  }

  onThemeSelect(theme: Theme): void {
    this.selectedTheme = theme;
    this.selectedArticle = null; // Сбрасываем выбранную статью при выборе новой темы
  }

  onArticleSelect(article: Article): void {
    this.selectedArticle = article;
  }

  addTheme(): void {
    if (this.newThemeName.trim() === '') {
      return;
    }
    this.articleService.addTheme(this.newThemeName.trim()).subscribe(() => {
      this.loadThemes(); // Перезагружаем список тем после добавления новой темы
      this.newThemeName = ''; // Очищаем поле ввода
    });
  }

  startEditingTheme(theme: Theme): void {
    this.editingTheme = { ...theme };
  }

  saveThemeEdit(theme: Theme): void {
    if (this.editingTheme) {
      this.articleService.editTheme(theme.id, this.editingTheme.name.trim()).subscribe(() => {
        this.loadThemes(); // Перезагружаем список тем после редактирования
        this.editingTheme = null;
      });
    }
  }

  cancelThemeEdit(): void {
    this.editingTheme = null;
  }

  deleteTheme(theme: Theme): void {
    const confirmation = confirm(`Are you sure you want to delete the theme "${theme.name}"?`);
    if (confirmation) {
      this.articleService.deleteTheme(theme.id).subscribe(() => {
        this.loadThemes(); // Перезагружаем список тем после удаления
      });
    }
  }

  startAddingArticleToTheme(theme: Theme): void {
    this.router.navigate(['/create-article'], { queryParams: { themeId: theme.id } }); // Перенаправление на страницу создания статьи с параметром themeId
  }

  editArticle(article: Article): void {
    this.router.navigate(['/articles', article.id, 'edit']); // Перенаправление на страницу редактирования статьи
  }

  deleteArticle(article: Article): void {
    this.articleService.deleteArticle(article.id).subscribe(() => {
      // Перезагружаем список тем и сбрасываем выбранную тему и статью
      this.loadThemes();
      this.selectedTheme = null;
      this.selectedArticle = null;
    });
  }
}
