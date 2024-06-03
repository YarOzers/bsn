import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MainComponent} from "./pages/main/main.component";
import {MyBooksComponent} from "./pages/my-books/my-books.component";
import {BookListComponent} from "./pages/book-list/book-list.component";
import {ManageBookComponent} from "./pages/manage-book/manage-book.component";
import {BorrowedBookListComponent} from "./pages/borrowed-book-list/borrowed-book-list.component";
import {ReturnBooksComponent} from "./pages/return-books/return-books.component";
import {authGuard} from "../../services/guard/auth.guard";
import {WaitingListComponent} from "./pages/waiting-list/waiting-list.component";
import {CreateArticleComponent} from "./pages/create-article/create-article.component";
import {ArticleListComponent} from "./pages/article-list/article-list.component";
import {ArticleDetailComponent} from "./pages/article-detail/article-detail.component";

const routes: Routes = [
  {
    path: '',
    component: MainComponent,
    canActivate: [authGuard],
    children: [
      {
        path: '',
        component: BookListComponent,
        canActivate: [authGuard]
      },
      {
        path: 'my-books',
        component: MyBooksComponent,
        canActivate: [authGuard]
      },
      {
        path: 'my-books/manage',
        component: ManageBookComponent,
        canActivate: [authGuard]
      },
      {
        path: "waiting-list",
        component: WaitingListComponent,
        canActivate: [authGuard]
      },
      {
        path: 'my-borrowed-books',
        component: BorrowedBookListComponent,
        canActivate: [authGuard]
      },
      {
        path: 'my-books/manage/:bookId',
        component: ManageBookComponent,
        canActivate: [authGuard]
      },
      {
        path: "my-returned-books",
        component: ReturnBooksComponent,
        canActivate: [authGuard]
      },
      {
        path: "articles",
        component: ArticleListComponent,
        canActivate: [authGuard]
      },
      {
        path: 'articles/:id',
        component: ArticleDetailComponent,
        canActivate: [authGuard]
      },
      {
        path: "create-article",
        component: CreateArticleComponent,
        canActivate: [authGuard]
      }, {path: 'article-list', component: ArticleListComponent},
      {path: 'create-article', component: CreateArticleComponent},
      {path: '', redirectTo: '/article-list', pathMatch: 'full'},
      {
        path: '**', redirectTo: '',
        component: BookListComponent,
        canActivate: [authGuard]
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BookRoutingModule {
}
