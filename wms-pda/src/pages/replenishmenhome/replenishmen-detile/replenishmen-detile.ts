import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ViewController } from 'ionic-angular';
import { Utils } from '../../../services/Utils';
import { IfObservable } from 'rxjs/observable/IfObservable';
/**
 * Generated class for the ReplenishmenDetilePage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-replenishmen-detile',
  templateUrl: 'replenishmen-detile.html',
})
export class ReplenishmenDetilePage {

  result: any = {};//补货数据集

  replenIsFalg: boolean;//标识

  selectItem: any = {};//当前选中项
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public viewCtrl: ViewController
  ) {
    if (Utils.isNotEmpty(this.navParams.get('result'))) {
      this.result = this.navParams.get('result');
    };
    if (Utils.isNotEmpty(this.navParams.get('replenIsFalg'))) {
      this.replenIsFalg = this.navParams.get('replenIsFalg');
    };
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad ReplenishmenDetilePage');
  }
  /**
   * 物品列表选中
   */
  headSelected(item) {
    this.selectItem = item;
  }
  exit() {
    this.viewCtrl.dismiss();
  }
  btnOk() {
    if (Utils.isNotEmpty(this.selectItem)) {
      this.viewCtrl.dismiss(this.selectItem);
    }
  }
}
