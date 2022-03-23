import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CheckInTrayInfoPage } from './checkin-tray-info';
import { ComponentsModule } from '../../../../components/components.module';
import { CheckInBoxLabelTypePageModule } from '../../../../shared/check-in-box-label-type/check-in-box-label-type.module';
import { CrumbsModule } from '../../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    CheckInTrayInfoPage,
  ],
  imports: [
    ComponentsModule,
    CheckInBoxLabelTypePageModule,
    IonicPageModule.forChild(CheckInTrayInfoPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class CheckInTrayInfoPageModule { }
