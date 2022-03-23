import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { PackageTaskListPage } from './package-task-list';
import { CheckInBoxLabelTypePageModule } from '../../../shared/check-in-box-label-type/check-in-box-label-type.module';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';

@NgModule({
  declarations: [
    PackageTaskListPage,
  ],
  imports: [
    CheckInBoxLabelTypePageModule,
    IonicPageModule.forChild(PackageTaskListPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class PackageTaskListPageModule { }
