import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { PackageTaskPage } from './package-task';
import { CheckInBoxLabelTypePageModule } from '../../shared/check-in-box-label-type/check-in-box-label-type.module';
import { CrumbsModule } from '../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    PackageTaskPage,
  ],
  imports: [
    CheckInBoxLabelTypePageModule,
    IonicPageModule.forChild(PackageTaskPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class PackageTaskPageModule { }
