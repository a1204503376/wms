import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { BindLpnPage } from './bind-lpn';
import { CheckInBoxLabelTypePageModule } from '../../../../../shared/check-in-box-label-type/check-in-box-label-type.module';
import { CrumbsModule } from '../../../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    BindLpnPage,
  ],
  imports: [
    CheckInBoxLabelTypePageModule,
    IonicPageModule.forChild(BindLpnPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class BindLpnPageModule { }
