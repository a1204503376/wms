import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CollageTaskListPage } from './collage-task-list';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    CollageTaskListPage,
  ],
  imports: [
    IonicPageModule.forChild(CollageTaskListPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class CollageTaskListPageModule {}
