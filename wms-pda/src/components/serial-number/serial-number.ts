import { Component } from '@angular/core';

/**
 * Generated class for the SerialNumberComponent component.
 *
 * See https://angular.io/api/core/Component for more info on Angular
 * Components.
 */
@Component({
  selector: 'serial-number',
  templateUrl: 'serial-number.html'
})
export class SerialNumberComponent {

  text: string;

  NumberListFalag: number = 1;

  isTilebool: true;

  constructor() {
    console.log('Hello SerialNumberComponent Component');
    // this.text = 'Hello World';
  }
  // isTitle(bool) {
  //   this.isTilebool = bool;
  // }
}
