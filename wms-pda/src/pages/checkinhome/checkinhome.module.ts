import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CheckinhomePage } from './checkinhome';
import { CrumbsModule } from '../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    CheckinhomePage, 
  ],
  imports: [
    IonicPageModule.forChild(CheckinhomePage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class CheckinhomePageModule {}
