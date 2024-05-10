import { Injectable } from '@angular/core';
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  private _token: string | null = null;

  // Проверяет доступность localStorage
  private isLocalStorageAvailable(): boolean {
    return typeof window !== 'undefined' && typeof localStorage !== 'undefined';
  }

  // Устанавливает токен
  set token(token: string | null) {
    if (this.isLocalStorageAvailable()) {
      if (token) {
        console.log('Saving token:', token);
        localStorage.setItem('token', token); // Сохраняет токен в localStorage
        this._token = token; // Сохраняет в приватное свойство
      } else {
        console.warn('Token is null or undefined. Not saving to localStorage.');
      }
    } else {
      console.error('localStorage is not available. Cannot save token.');
    }
  }

  // Получает токен
  get token(): string | null {
    if (this.isLocalStorageAvailable()) {
      if (this._token === null) {
        this._token = localStorage.getItem('token'); // Извлекает из localStorage
      }
      return this._token;
    } else {
      console.error('localStorage is not available. Returning null for token.');
      return null;
    }
  }

  // Удаляет токен
  clearToken(): void {
    if (this.isLocalStorageAvailable()) {
      console.log('Clearing token from localStorage');
      localStorage.removeItem('token'); // Удаляет токен
      this._token = null; // Обнуляет приватное свойство
    } else {
      console.error('localStorage is not available. Cannot clear token.');
    }
  }

  isTokenNotValid() {
    return !this.isTokenValid();
  }

  private isTokenValid() {
    const token = this.token;
    if(!token){
      return false;
    }
    // decode the token
    const jwtHelper = new JwtHelperService();
    // check expiry date
    const isTokenExpired = jwtHelper.isTokenExpired(token);
    if(isTokenExpired){
      localStorage.clear();
      return false;
    }
    return true;
  }
}

