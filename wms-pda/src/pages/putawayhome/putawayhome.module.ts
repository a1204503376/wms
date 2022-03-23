import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { PutawayhomePage } from './putawayhome';
import { CrumbsModule } from '../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    PutawayhomePage,
  ],
  imports: [
    IonicPageModule.forChild(PutawayhomePage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class PutawayhomePageModule {}
