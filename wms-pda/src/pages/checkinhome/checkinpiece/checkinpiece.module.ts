import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CheckinpiecePage } from './checkinpiece';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    CheckinpiecePage,
  ],
  imports: [
    IonicPageModule.forChild(CheckinpiecePage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class CheckinpiecePageModule {}
