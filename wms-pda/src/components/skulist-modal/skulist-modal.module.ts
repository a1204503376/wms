import { NgModule } from '@angular/core';
import { SkuListModal } from './skulist-modal'
import { IonicPageModule } from 'ionic-angular'

@NgModule({
  declarations: [SkuListModal],
  imports: [IonicPageModule.forChild(SkuListModal),
  ],
  exports: [SkuListModal]
})
export class AsnBillnoModalModule { }