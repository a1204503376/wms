import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { StockqueryPage } from './stockquery';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    StockqueryPage,
  ],
  imports: [
    IonicPageModule.forChild(StockqueryPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class StockqueryPageModule {}
