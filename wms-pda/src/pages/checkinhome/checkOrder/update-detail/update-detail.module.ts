import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { UpdateDetailPage } from './update-detail';
import { CheckInBoxLabelTypePageModule } from '../../../../shared/check-in-box-label-type/check-in-box-label-type.module';
import { CrumbsModule } from '../../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    UpdateDetailPage,
  ],
  imports: [
    CheckInBoxLabelTypePageModule,
    IonicPageModule.forChild(UpdateDetailPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class UpdateDetailPageModule { }
