import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { TaskwayMultPage } from './taskway-mult';
import { CheckInBoxLabelTypePageModule } from '../../../shared/check-in-box-label-type/check-in-box-label-type.module';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    TaskwayMultPage,
  ],
  imports: [
    CheckInBoxLabelTypePageModule,
    IonicPageModule.forChild(TaskwayMultPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class TaskwayMultPageModule { }
