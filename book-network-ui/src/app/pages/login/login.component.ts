import {Component} from '@angular/core';
import {AuthenticationRequest} from "../../services/models/authentication-request";
import {FormsModule} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";
import {HttpClientModule, HttpErrorResponse} from "@angular/common/http";
import {CommonModule} from "@angular/common";
import {TokenService} from "../../services/token/token.service";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    HttpClientModule,
    CommonModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  authRequest: AuthenticationRequest = {email: '', password: ''};
  errorMsg: any[] = [];
  token: string | null = null;

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private tokenService: TokenService
  ) {
  }

  login() {
    // Убедимся, что очищаем массив ошибок перед новым запросом
    this.errorMsg = [];

    this.authService.authenticate({body: this.authRequest}).subscribe({
      next: (response) => {
        if (response?.token) {
          this.tokenService.token = response.token; // Сохраняем токен
          this.router.navigate(['books']); // Переходим к другому маршруту
        } else {
          console.warn('Token not found');
        }
      },
      error: (error: HttpErrorResponse) => {
        if (Array.isArray(error.error.validationErrors)) {
          // Добавляем все элементы массива в errorMsg
          this.errorMsg.push(...error.error.validationErrors);
        } else {
          this.errorMsg.push(error.error.error); // В случае не массива, добавляем тело ошибки
        }

        console.error('Authentication error:', error.error); // Логируем ошибку
      },
      complete: () => {
        console.log('Authentication request completed'); // Уведомление о завершении запроса
      },
    });
  }

  register() {
    console.log('registerClick')
    this.router.navigate(['register'])
  }
}
