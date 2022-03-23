import { NgModule } from '@angular/core';
import { CrumbsComponent } from './crumbs'
import { IonicPageModule } from 'ionic-angular'

@NgModule({
  declarations: [CrumbsComponent],
  imports: [IonicPageModule.forChild(CrumbsComponent),
  ],
  exports: [CrumbsComponent]
 
})
export class CrumbsModule {
  

 }