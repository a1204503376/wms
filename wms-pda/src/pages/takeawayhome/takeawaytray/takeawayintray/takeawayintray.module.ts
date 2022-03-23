import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { TakeawayinTrayPage } from './takeawayintray';
import { CheckInBoxLabelTypePageModule } from '../../../../shared/check-in-box-label-type/check-in-box-label-type.module';
import { CrumbsModule } from '../../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    TakeawayinTrayPage,
  ],
  imports: [
    CheckInBoxLabelTypePageModule,
    IonicPageModule.forChild(TakeawayinTrayPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class TakeawayinTrayPageModule { }
