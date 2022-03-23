import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { StockFrozenPage } from './stock-frozen';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    StockFrozenPage,
  ],
  imports: [
    IonicPageModule.forChild(StockFrozenPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class StockFrozenPageModule { }
