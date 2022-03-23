import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { TakeawayPage } from './takeaway';
import { CrumbsModule } from '../../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    TakeawayPage,
  ],
  imports: [
    IonicPageModule.forChild(TakeawayPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class TakeawayPageModule {}
