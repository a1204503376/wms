import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CheckinDetailedPage } from './checkin-detailed';
import { CrumbsModule } from '../../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    CheckinDetailedPage,
  ],
  imports: [
    IonicPageModule.forChild(CheckinDetailedPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class CheckinDetailedPageModule {}
