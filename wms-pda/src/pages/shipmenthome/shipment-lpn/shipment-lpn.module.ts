import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { ShipmentLpnPage } from './shipment-lpn';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    ShipmentLpnPage,
  ],
  imports: [
    IonicPageModule.forChild(ShipmentLpnPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class ShipmentLpnPageModule {}
