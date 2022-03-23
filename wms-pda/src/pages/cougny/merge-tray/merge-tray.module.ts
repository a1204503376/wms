import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { MergeTrayPage } from './merge-tray';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    MergeTrayPage,
  ],
  imports: [
    IonicPageModule.forChild(MergeTrayPage),
    CrumbsModule
  ],
  schemas: [NO_ERRORS_SCHEMA]
})
export class MergeTrayPageModule { }
