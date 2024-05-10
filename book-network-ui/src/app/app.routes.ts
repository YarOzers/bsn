import {Routes} from '@angular/router';
import {LoginComponent} from "./pages/login/login.component";
import {RegisterComponent} from "./pages/register/register.component";
import {ActivateAccountComponent} from "./pages/activate-account/activate-account.component";
import {PageNotFoundComponent} from "./pages/page-not-found/page-not-found.component";
import {authGuard} from "./services/guard/auth.guard";

export const routes: Routes = [
  {
    path: '', redirectTo: '/login',
    pathMatch: 'full'
  },
  {
    path: 'login', title: 'Login',
    component: LoginComponent
  },
  {
    path: 'register', title: 'Register',
    component: RegisterComponent
  },
  {
    path: 'activate-account', title: 'Activate-account',
    component: ActivateAccountComponent
  },
  {
    path: 'books',
    loadChildren: () => import('./modules/book/book.module').then(m => m.BookModule),
    canActivate: [authGuard]
  },
  {
    path: '**', title: '404',
    component: PageNotFoundComponent
  }
];
