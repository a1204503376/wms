import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { BindOrderPage } from './bind-order';
import { CrumbsModule } from '../../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    BindOrderPage,
  ],
  imports: [
    IonicPageModule.forChild(BindOrderPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class BindOrderPageModule {}
