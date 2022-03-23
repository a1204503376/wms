import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { LoginPage } from './login';
import { CalendarModule } from 'primeng/calendar';
import { DirectivesModule } from "../../directives/directives.module";
@NgModule({
  declarations: [
    LoginPage,
  ],
  imports: [
    DirectivesModule,
    CalendarModule,
    IonicPageModule.forChild(LoginPage),
  ],
})
export class LoginPageModule {}
