import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { TakeawaypieceboxPage } from './takeawaypiecebox';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    TakeawaypieceboxPage,
  ],
  imports: [
    IonicPageModule.forChild(TakeawaypieceboxPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class TakeawaypieceboxPageModule {}
