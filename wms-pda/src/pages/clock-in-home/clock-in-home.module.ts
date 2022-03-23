import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { ClockInHomePage } from './clock-in-home';

@NgModule({
  declarations: [
    ClockInHomePage,
  ],
  imports: [
    IonicPageModule.forChild(ClockInHomePage),
  ],
})
export class ClockInHomePageModule {}
