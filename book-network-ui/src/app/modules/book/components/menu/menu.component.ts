import {AfterViewInit, Component, OnInit} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {TokenService} from "../../../../services/token/token.service";
import jwt_decode from 'jwt-decode';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent implements AfterViewInit, OnInit {
  userName: string = '';
  decodedToken: any;

  constructor(private tokenService: TokenService,
              private router: Router) {
  }

  ngOnInit() {
    const token = localStorage.getItem('token'); // Убедитесь, что это строка
    if (token) {
      this.decodedToken = this.decodeToken(token);
      console.log(this.decodedToken.fullName.firstname);
    }
  }
  decodeToken(token: string): any {
    try {
      return jwt_decode(token); // Убедитесь, что jwt_decode - это функция
    } catch (error) {
      console.error('Ошибка при декодировании токена:', error);
      return null;
    }
  }

  logout() {
    this.tokenService.clearToken();
    this.router.navigate(['login'])


  }

  ngAfterViewInit(): void {
    if (typeof document !== 'undefined') {
      // Найти все элементы DOM, которые имеют класс 'nav-link'.
      const linkColor = document.querySelectorAll('.nav-link');

      // Пройтись по каждому из найденных элементов.
      linkColor.forEach((link) => {
        // Проверить, совпадает ли текущий URL с атрибутом 'href' элемента 'link'.
        // Если совпадает, добавить класс 'active' к этому элементу.
        if (window.location.href.endsWith(link.getAttribute('href') || '')) {
          link.classList.add('active');
        }

        // Добавить обработчик события 'click' к каждому 'nav-link'.
        link.addEventListener('click', () => {
          // При клике удалить класс 'active' у всех 'nav-link'.
          linkColor.forEach(l => l.classList.remove('active'));
          // Добавить класс 'active' к элементу, который был кликнут.
          link.classList.add('active');
        });
      });
    }
  }

}
