import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { RandomCheckTaskPage } from './randomchecktask';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    RandomCheckTaskPage,
  ],
  imports: [
    IonicPageModule.forChild(RandomCheckTaskPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class RandomCheckTaskPageModule {}
