import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { PrintingPage } from './printing';

@NgModule({
  declarations: [
    PrintingPage,
  ],
  imports: [
    IonicPageModule.forChild(PrintingPage),
  ],
})
export class PrintingPageModule {}
