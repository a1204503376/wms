import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { TakeawaySearchPage } from './takeawaysearch';
import { CrumbsModule } from '../../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    TakeawaySearchPage,
  ],
  imports: [
    IonicPageModule.forChild(TakeawaySearchPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class TakeawaySearchPageModule {}
