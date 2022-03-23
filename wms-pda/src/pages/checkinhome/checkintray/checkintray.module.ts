import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CheckInTrayPage } from './checkintray';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    CheckInTrayPage,
  ],
  imports: [
    IonicPageModule.forChild(CheckInTrayPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class CheckInTrayPageModule {}
