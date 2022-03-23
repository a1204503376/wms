import { NgModule } from '@angular/core';
import { SkuBillModal } from './skuBill-modal'
import { IonicPageModule } from 'ionic-angular'

@NgModule({
  declarations: [SkuBillModal],
  imports: [IonicPageModule.forChild(SkuBillModal),
  ],
  exports: [SkuBillModal]
})
export class AsnBillnoModalModule { }