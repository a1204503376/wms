import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CollageTaskPage } from './collage-task';
import { CrumbsModule } from '../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    CollageTaskPage,
  ],
  imports: [
    IonicPageModule.forChild(CollageTaskPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class CollageTaskPageModule {}
