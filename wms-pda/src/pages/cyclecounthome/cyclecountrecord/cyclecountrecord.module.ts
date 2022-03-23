import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CyclecountRecordPage } from './cyclecountrecord';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    CyclecountRecordPage,
  ],
  imports: [
    IonicPageModule.forChild(CyclecountRecordPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class CyclecountPageModule {}
