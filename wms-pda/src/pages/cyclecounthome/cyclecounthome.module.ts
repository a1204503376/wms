import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CyclecounthomePage } from './cyclecounthome';
import { CrumbsModule } from '../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    CyclecounthomePage,
  ],
  imports: [
    IonicPageModule.forChild(CyclecounthomePage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class CyclecounthomePageModule {}
