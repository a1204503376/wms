import { Component, ViewChild, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, ModalController, Events } from 'ionic-angular';
import { Utils } from '../../../services/Utils';
import { AppService } from "../../../services/App.service";
import { NativeService } from "../../../services/NativeService"
import { BarcodeService, BarcodeType } from '../../../services/BarCodeService';
import { Api, ContenType, Method } from '../../../utils/appConstant';
import {Storage} from '@ionic/storage';
@IonicPage()
@Component({
  selector: 'page-check-order',
  templateUrl: 'check-order.html',
})
export class CheckOrderPage {
  public result: any = {};
  public asnBillNo: string;
  public lpnCode: string;//容器编码
  public isPop: boolean;
  public sysparms: any = [];//系统参数
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public modalController: ModalController,
    public appService: AppService,
    private storage: Storage,
    public nativeService: NativeService,
    public barcodeService: BarcodeService,
    public events: Events,
    public ngZone: NgZone
  ) {
    if (Utils.isNotEmpty(this.navParams.get('result'))) {//订单编号输入
      this.result = this.navParams.get('result');
    }
    this.storage.get('sysParam').then(data => {
      this.sysparms = data.filter(x => x.paramKey == 'system::lpnEnable')[0];
  });
  }

  ionViewWillEnter() {
    if (this.isPop) {
      this.isPop = false;
    }
    //事件注册（扫描结果接收）
    this.events.subscribe('barcode:scan', (barcode, time) => {
        let bt = this.barcodeService.GetBarcodeType(barcode);
       if (bt == BarcodeType.TuoPan) {
            this.ngZone.run(() => {
                this.lpnCode = barcode;//容器
            });
        }
    });
  }
  ionViewWillLeave() {

  }
  ionViewDidLoad() {

  }

  itemClick(item) {
    item['locCode'] = "STAGE";
    this.navCtrl.push('UpdateDetailPage', {
      result: item, callback: data => {
        this.isPop = data.isPop;
        if (data.result) {
          Object.assign(item, data.result);
        }
      }
    });
  }
  backClick() {
    this.navCtrl.pop();
  }
  submitClick(){
    let params = {
      asnBillNo: this.result.asnBillNo,
      asnDetailMap: this.result.asnDetailList,
      locCode: 'STAGE',
      lpnCode: this.lpnCode,
      whId: this.result.whId
  };
  this.nativeService.showLoading();
  this.appService.httpRequest(Api.Instock + Api.submitAsnHeaderWithOrder, Method.Post, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      if(result.success){
        this.nativeService.showToast('提交成功');
        this.navCtrl.pop();
      }else{
        this.nativeService.showToast(result.msg);
      }
  });
  }
}
