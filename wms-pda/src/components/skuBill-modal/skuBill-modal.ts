import { Component } from '@angular/core';
import { NavController, NavParams, ViewController, IonicPage } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import { NativeService } from '../../services/NativeService';
import { AppService } from '../../services/App.service';
import { AsnHeaderInfoModel } from '../../models/Instroe.model';
import { Utils } from '../../services/Utils';
import { SkuModel } from '../../models/SkuModel';


@IonicPage()
@Component({
  selector: 'page-skuBill-modal',
  templateUrl: 'skuBill-modal.html'
})
export class SkuBillModal {
  result: any = [];
  resultInfo: any;
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public viewCtrl: ViewController,
    public storage: Storage,
    public nativeService: NativeService,
    public appService: AppService) {
    this.result = this.navParams.get('result');
    debugger
    this.resultInfo = this.result.stockPdaVOList[0];
  }

  /**
	 *   页面进入
	 */
  ionViewWillEnter() {

  }

  /**
	 *   取到入库单列表
	 */
  getAsnBill(asnType) {

  }

  /**
 * 选择要处理的入库单
 */
  headSelected(item) {
  }
  /**
   * 消失调用
   */
  dismiss() {
    this.viewCtrl.dismiss();
  }

  /**
   * 点击确定
   */
  submitdata() {
  }
}

