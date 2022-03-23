import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { ReplenishmenDetilePage } from './replenishmen-detile';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    ReplenishmenDetilePage,
  ],
  imports: [
    IonicPageModule.forChild(ReplenishmenDetilePage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class ReplenishmenDetilePageModule {}
