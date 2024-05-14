import {ApplicationConfig, Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {HttpClientModule} from "@angular/common/http";
import {CommonModule} from "@angular/common";
import {LoginComponent} from "./pages/login/login.component";
import {ApiModule} from "./services/api.module";
import {ApiConfiguration} from "./services/api-configuration";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    HttpClientModule,
    CommonModule,
    LoginComponent,
    ApiModule.forRoot({rootUrl:'http://95.31.234.122:1111'}).ngModule

  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'book-network-ui';
}
