import {APP_INITIALIZER, ApplicationConfig, Provider} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {provideClientHydration} from '@angular/platform-browser';
import { provideHttpClient, withFetch, withInterceptors } from "@angular/common/http";
import {httpTokenInterceptor} from "./services/interceptor/http-token.interceptor";
import {KeycloakService} from "./services/keycloak/keycloak.service";

export function kcFactory(kcService: KeycloakService){
  return () => kcService.init();
}
const KeycloakInitializerProvider: Provider = {
  provide: APP_INITIALIZER,
  deps: [KeycloakService],
  useFactory: kcFactory,
  multi: true

}
export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes),
    provideClientHydration(),
    provideHttpClient(withFetch()),
    provideHttpClient(withInterceptors([httpTokenInterceptor])),
    KeycloakInitializerProvider,
    KeycloakService,
  ]
};
