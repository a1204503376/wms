import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CyclecountBillPage } from './cyclecount-bill';
import { CrumbsModule } from '../../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    CyclecountBillPage,
  ],
  imports: [
    IonicPageModule.forChild(CyclecountBillPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class CyclecountBillPageModule {}
