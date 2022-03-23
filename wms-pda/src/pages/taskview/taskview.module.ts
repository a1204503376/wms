import { NgModule,NO_ERRORS_SCHEMA } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { TaskviewPage } from './taskview';
import { CrumbsModule } from '../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    TaskviewPage,
   
  ],
  imports: [
    IonicPageModule.forChild(TaskviewPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]

})
export class TaskviewPageModule{}
