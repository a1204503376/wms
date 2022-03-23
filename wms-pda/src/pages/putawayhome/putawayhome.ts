import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { Utils } from '../../services/Utils';
/**
 * Generated class for the PutawayhomePage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-putawayhome',
  templateUrl: 'putawayhome.html',
})
export class PutawayhomePage {
  public menuRole: any = [];
  constructor(public navCtrl: NavController, public navParams: NavParams) {
  }

  ionViewWillEnter() {
    if (Utils.isNotEmpty(this.navParams.get('menuRole'))) {
      this.menuRole = this.navParams.get('menuRole');
      if (this.menuRole.length < 9) {
        let count = 9 - this.menuRole.length;
        for (let index = 0; index < count; index++) {
          this.menuRole.push({ source: '' });
        }
      }
    } else {
      this.navCtrl.pop();
    }
  }
  expression(event) {
    this.navCtrl.push(event.path);
  }

}
