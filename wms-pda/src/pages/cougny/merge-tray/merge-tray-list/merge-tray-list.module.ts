import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { MergeTrayListPage } from './merge-tray-list';
import { CrumbsModule } from '../../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    MergeTrayListPage,
  ],
  imports: [
    IonicPageModule.forChild(MergeTrayListPage),
    CrumbsModule
  ],
  schemas: [NO_ERRORS_SCHEMA]
})
export class MergeTrayListPageModule { }
