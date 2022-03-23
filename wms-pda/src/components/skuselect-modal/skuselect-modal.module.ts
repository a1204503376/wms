import { NgModule } from '@angular/core';
import { SkuSelectModal } from './skuselect-modal'
import { IonicPageModule } from 'ionic-angular'

@NgModule({
  declarations: [SkuSelectModal],
  imports: [IonicPageModule.forChild(SkuSelectModal),
  ],
  exports: [SkuSelectModal]
})
export class StockSelectModalModule { }