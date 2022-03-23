import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { PutawayPage } from './putaway';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    PutawayPage,
  ],
  imports: [
    IonicPageModule.forChild(PutawayPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class PutawayPageModule {}
