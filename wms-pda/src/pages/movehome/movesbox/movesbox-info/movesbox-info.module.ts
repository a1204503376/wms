import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { MovesboxInfoPage } from './movesbox-info';
import { CrumbsModule } from '../../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    MovesboxInfoPage,
  ],
  imports: [
    IonicPageModule.forChild(MovesboxInfoPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class MovesboxInfoPageModule {}
