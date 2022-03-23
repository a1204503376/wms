import { NgModule } from '@angular/core';
import { StockSelectModal } from './stockselect-modal'
import { IonicPageModule } from 'ionic-angular'

@NgModule({
  declarations: [StockSelectModal],
  imports: [IonicPageModule.forChild(StockSelectModal),
  ],
  exports: [StockSelectModal]
})
export class StockSelectModalModule { }