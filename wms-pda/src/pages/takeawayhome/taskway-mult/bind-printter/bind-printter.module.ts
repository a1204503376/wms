import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { BindPrintterPage } from './bind-printter';
import { CrumbsModule } from '../../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    BindPrintterPage,
  ],
  imports: [
    IonicPageModule.forChild(BindPrintterPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class BindPrintterPageModule { }
