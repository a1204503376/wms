import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CheckOrderPage } from './check-order';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    CheckOrderPage,
  ],
  imports: [
    IonicPageModule.forChild(CheckOrderPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class CheckOrderPageModule { }
