import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { TakeawayhomePage } from './takeawayhome';
import { CrumbsModule } from '../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    TakeawayhomePage,
  ],
  imports: [
    IonicPageModule.forChild(TakeawayhomePage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class TakeawayhomePageModule {}
