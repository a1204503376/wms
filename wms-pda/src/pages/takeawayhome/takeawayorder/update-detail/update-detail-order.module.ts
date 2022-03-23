import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { UpdateDetailOrderPage } from './update-detail-order';
import { CheckInBoxLabelTypePageModule } from '../../../../shared/check-in-box-label-type/check-in-box-label-type.module';
import { CrumbsModule } from '../../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    UpdateDetailOrderPage,
  ],
  imports: [
    CheckInBoxLabelTypePageModule,
    IonicPageModule.forChild(UpdateDetailOrderPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class UpdateDetailOrderPageModule { }
