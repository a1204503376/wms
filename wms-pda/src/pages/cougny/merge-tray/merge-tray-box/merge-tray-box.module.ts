import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { MergeTrayBoxPage } from './merge-tray-box';
// import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    MergeTrayBoxPage,
  ],
  imports: [
    IonicPageModule.forChild(MergeTrayBoxPage),
    // CrumbsModule
  ],
  schemas: [NO_ERRORS_SCHEMA]
})
export class MergeTrayBoxPageModule { }
