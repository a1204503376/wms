import { NgModule } from '@angular/core';
import { MergeTraySelectModal } from './mergetrayselect-modal'
import { IonicPageModule } from 'ionic-angular'

@NgModule({
  declarations: [MergeTraySelectModal],
  imports: [IonicPageModule.forChild(MergeTraySelectModal),
  ],
  exports: [MergeTraySelectModal]
})
export class MergeTraySelectModule { }