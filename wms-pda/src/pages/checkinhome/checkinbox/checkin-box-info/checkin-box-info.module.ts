import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CheckinBoxInfoPage } from './checkin-box-info';
import { ComponentsModule } from '../../../../components/components.module';
import { CheckInBoxLabelTypePageModule } from '../../../../shared/check-in-box-label-type/check-in-box-label-type.module';
import { CrumbsModule } from '../../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    CheckinBoxInfoPage,
  ],
  imports: [
    ComponentsModule,
    CheckInBoxLabelTypePageModule,
    IonicPageModule.forChild(CheckinBoxInfoPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class CheckinBoxInfoPageModule { }
