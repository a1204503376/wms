import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { ClockInPage } from './clock-in';

@NgModule({
  declarations: [
    ClockInPage,
  ],
  imports: [
    IonicPageModule.forChild(ClockInPage),
  ],
})
export class ClockInPageModule {}
