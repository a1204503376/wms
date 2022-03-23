import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { TakeawayinPage } from './takeawayin';
import { CrumbsModule } from '../../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    TakeawayinPage,
  ],
  imports: [
    IonicPageModule.forChild(TakeawayinPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class TakeawayinPageModule {}
