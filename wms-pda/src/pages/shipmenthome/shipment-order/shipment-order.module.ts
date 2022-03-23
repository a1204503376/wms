import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { ShipmentOrderPage } from './shipment-order';

@NgModule({
  declarations: [
    ShipmentOrderPage,
  ],
  imports: [
    IonicPageModule.forChild(ShipmentOrderPage),
  ],
})
export class ShipmentOrderPageModule {}
