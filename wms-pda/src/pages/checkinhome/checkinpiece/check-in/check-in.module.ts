import { NgModule,NO_ERRORS_SCHEMA } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CheckInPage } from './check-in';
import { CrumbsModule } from '../../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    CheckInPage,
  ],
  imports: [
    IonicPageModule.forChild(CheckInPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class CheckInPageModule {}
