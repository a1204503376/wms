import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, Events } from 'ionic-angular';
import { AppService } from "../../../../services/App.service";
import { Utils } from '../../../../services/Utils';
import { NativeService } from "../../../../services/NativeService"
import { Api, ContenType, Method } from '../../../../utils/appConstant';
import { BarcodeService, BarcodeType } from '../../../../services/BarCodeService';
import { AppGlobal } from '../../../../services/AppGlobal';
/**

/**
 * Generated class for the CyclecountBillPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-cyclecount-bill',
  templateUrl: 'cyclecount-bill.html',
})
export class CyclecountBillPage {

  asnBillNo: string;//订单编号

  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public appService: AppService,
    public nativeService: NativeService,
    public events: Events,
    public barcodeService: BarcodeService

  ) {
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad CyclecountBillPage');
  }
  ionViewWillEnter() {
    //事件注册（扫描结果接收）
    this.events.subscribe('barcode:scan', (barcode, time) => {
      this.asnBillNo = barcode;
      this.cyclecountrantask();
    });
  }
  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
  }
  /**
 * 入库单回车事件
 */
  keyEnter(event) {
    if (event && event.keyCode == 13) {
      this.nativeService.showLoading();
      if (Utils.isEmpty(this.asnBillNo)) {
        this.nativeService.showToast('请输入订单编号');
        return;
      }
      this.cyclecountrantask();
    }
  }
  cyclecountrantask() {
    let params = {
      countBillNo: this.asnBillNo
    }
    this.appService.httpRequest(Api.StockCountPDA + Api.cyclecountrantask, Method.Get, params, ContenType.Content, result => {
      if (Utils.isEmpty(result.data)) {
        this.nativeService.showToast('该单据不存在或已收货完成');
        return;
      } else {
        //进入盘点
        this.navCtrl.push('CyclecountrantaskPage', { result: result.data.countDetailVOList });
      }
    });
  }
}
