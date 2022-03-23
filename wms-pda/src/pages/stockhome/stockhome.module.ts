import { NgModule,NO_ERRORS_SCHEMA } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { StockhomePage } from './stockhome';
import { CrumbsModule } from '../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    StockhomePage,
  ],
  imports: [
    IonicPageModule.forChild(StockhomePage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class StockhomePageModule {}
