import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { ShipmentCarOrderPage } from './shipment-car-order';

@NgModule({
  declarations: [
    ShipmentCarOrderPage,
  ],
  imports: [
    IonicPageModule.forChild(ShipmentCarOrderPage),
  ],
})
export class ShipmentCarOrderPageModule {}
