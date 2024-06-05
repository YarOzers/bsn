import {ApplicationConfig, Component, importProvidersFrom} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import { provideHttpClient, withInterceptorsFromDi } from "@angular/common/http";
import {CommonModule} from "@angular/common";
import {LoginComponent} from "./pages/login/login.component";
import {ApiModule} from "./services/api.module";

@Component({
  imports: [RouterOutlet,
    CommonModule,
    LoginComponent,
    ApiModule.forRoot({rootUrl: 'http://localhost:9090/api/v1'}).ngModule],
  providers: [],
  selector: 'app-root',
  standalone: true, styleUrl: './app.component.scss',
  templateUrl: './app.component.html'
})
export class AppComponent {
  title = 'book-network-ui';
}
