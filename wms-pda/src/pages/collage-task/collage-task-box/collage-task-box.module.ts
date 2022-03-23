import { NgModule,NO_ERRORS_SCHEMA } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CollageTaskBoxPage } from './collage-task-box';
// import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    CollageTaskBoxPage,
  ],
  imports: [
    IonicPageModule.forChild(CollageTaskBoxPage),
    // CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class CollageTaskBoxPageModule {}
