import {Injectable} from '@angular/core';
import Keycloak from 'keycloak-js';
import {UserProfile} from "./user-profile";

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {
  private _keycloak: Keycloak | undefined;
  private _profile: UserProfile | undefined;

  get keycloak() {
    if (!this._keycloak && this.isBrowser()) {
      this._keycloak = new Keycloak({
        url: 'http://localhost:9999',
        realm: 'book-social-network',
        clientId: 'bsn'
      });
    }
    return this._keycloak;
  }

  get profile(): UserProfile | undefined {
    return this._profile;
  }

  constructor() {
  }

  async init() {
    if (this.isBrowser()) {
      console.log('Authenticating the user...');
      const authenticated = await this.keycloak?.init({
        onLoad: 'login-required',
        checkLoginIframe: false
      });
      if (authenticated) {
        this._profile = (await this.keycloak?.loadUserProfile()) as UserProfile;
        this._profile.token = this.keycloak?.token;
      } else {
        console.error('User not authenticated');
      }
    } else {
      console.log('Keycloak initialization skipped on the server side.');
    }
  }

  login() {
    return this.keycloak?.login();
  }


  private isBrowser(): boolean {
    return typeof window !== 'undefined' && typeof document !== 'undefined';
  }

   getUserName(){
    return this._profile?.firstName;
  }

}
