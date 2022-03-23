import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CheckInBoxLabelTypePage } from './check-in-box-label-type';

@NgModule({
  declarations: [
    CheckInBoxLabelTypePage,
  ],
  imports: [
    IonicPageModule.forChild(CheckInBoxLabelTypePage),
  ],
  exports: [
    CheckInBoxLabelTypePage
  ]
})
export class CheckInBoxLabelTypePageModule { }
