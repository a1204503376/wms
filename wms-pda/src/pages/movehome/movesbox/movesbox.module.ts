import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { MovesboxPage } from './movesbox';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    MovesboxPage,
  ],
  imports: [
    IonicPageModule.forChild(MovesboxPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class MovesboxPageModule {}
