import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CyclecountrandominfoPage } from './cyclecountrandominfo';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    CyclecountrandominfoPage,
  ],
  imports: [
    IonicPageModule.forChild(CyclecountrandominfoPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class CyclecountrandominfoPageModule {}
