import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { RandomCheckPage } from './randomcheck';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    RandomCheckPage,
  ],
  imports: [
    IonicPageModule.forChild(RandomCheckPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class RandomCheckPageModule {}
