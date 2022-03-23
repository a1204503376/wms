import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { TakeawaypiecePage } from './takeawaypiece';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    TakeawaypiecePage,
  ],
  imports: [
    IonicPageModule.forChild(TakeawaypiecePage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class TakeawaypiecePageModule {}
