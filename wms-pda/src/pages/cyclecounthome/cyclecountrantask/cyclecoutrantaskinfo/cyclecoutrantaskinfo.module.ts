import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CyclecoutrantaskinfoPage } from './cyclecoutrantaskinfo';
import { CrumbsModule } from '../../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    CyclecoutrantaskinfoPage,
  ],
  imports: [
    IonicPageModule.forChild(CyclecoutrantaskinfoPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class CyclecoutrantaskinfoPageModule {}
