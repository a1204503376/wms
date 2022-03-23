import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { PackageTaskInfoPage } from './package-task-info';
import { CrumbsModule } from '../../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    PackageTaskInfoPage,
  ],
  imports: [
    IonicPageModule.forChild(PackageTaskInfoPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class PackageTaskInfoPageModule {}
