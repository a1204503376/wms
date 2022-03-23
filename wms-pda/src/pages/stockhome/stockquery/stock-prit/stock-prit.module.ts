import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { StockPritPage } from './stock-prit';
import { CrumbsModule } from '../../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    StockPritPage,
  ],
  imports: [
    IonicPageModule.forChild(StockPritPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class StockPritPageModule {}
