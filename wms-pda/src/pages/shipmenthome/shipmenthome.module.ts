import { NgModule,NO_ERRORS_SCHEMA } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { ShipmenthomePage } from './shipmenthome';
import { CrumbsModule } from '../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    ShipmenthomePage,
  ],
  imports: [
    IonicPageModule.forChild(ShipmenthomePage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class ShipmenthomePageModule {}
