import { Component, ViewChild, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, ModalController, Events } from 'ionic-angular';
import { Utils } from '../../../../services/Utils';
import { AppService } from "../../../../services/App.service";
import { NativeService } from "../../../../services/NativeService"
import { BarcodeService, BarcodeType } from '../../../../services/BarCodeService';
import { Api, ContenType, Method } from '../../../../utils/appConstant';
import {Storage} from '@ionic/storage';

/**
 * Generated class for the CheckinBoxInfoPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-takeawayinorder',
  templateUrl: 'takeawayinorder.html',
})

export class TakeawayinOrderPage {
  public wellenNo: string;//波次编码
  public result: any = {};
  public soBillNo: string;
  public lpnCode: string;//容器编码
  public isPop: boolean;
  public sysparms: any = [];//系统参数
  public pickPlans:any = [];
  public planQty:number = 0;
  public scanQty:number = 0;
  public unPlanQty:number = 0;
  public locCode: string = 'PICK'; 
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
    this.wellenNo = this.navParams.get('wellenNo');
    this.soBillNo = this.navParams.get('soBillNo');
    this.getPickInfoByOrder();
  }

  getPickInfoByOrder(){
    let params = {
      wellenNo: this.wellenNo,
      soBillNo:this.soBillNo
    }
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.Outstock + Api.getPickInfoByOrder, Method.Get, params, ContenType.Form, result => {
      this.nativeService.hideLoading();
      this.pickPlans = result.data;
      if (Utils.isEmpty(this.pickPlans)) {
        this.nativeService.showToast('当前任务下没有物品');
        this.navCtrl.pop();
        return;
      }
      let pickPlanQtyTotal = this.pickPlans.map((val:any) =>val.pickPlanQty)
      this.planQty = pickPlanQtyTotal.reduce(function(prev, cur, index, arr) {
          return prev + cur;
      })
      let pickRealQtyTotal = this.pickPlans.map((val:any) =>val.pickRealQty)
      this.scanQty = pickRealQtyTotal.reduce(function(prev, cur, index, arr) {
          return prev + cur;
      })
      this.pickPlans.forEach(element => {
        this.caleUnPlanQty(element);
      });
    });
  }
  caleUnPlanQty(item){
    item.unPlanQty = item.pickPlanQty - item.pickRealQty;
    return item.unPlanQty;
  }
  ionViewWillEnter() {
    if (this.isPop) {
      this.isPop = false;
    }
    //事件注册（扫描结果接收）
    this.events.subscribe('barcode:scan', (barcode, time) => {
        let bt = this.barcodeService.GetBarcodeType(barcode);
        if (bt == BarcodeType.HuoWei) {
          this.ngZone.run(() => {
            this.locCode = barcode;
          });
        }
    });
  }
  ionViewWillLeave() {

  }
  ionViewDidLoad() {

  }

  itemClick(item) {
    this.navCtrl.push('UpdateDetailOrderPage', {
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
      wellenNo: this.wellenNo,
      pickPlans: this.pickPlans,
      targetLocCode: this.locCode,
      soBillNo: this.soBillNo
  };
  this.nativeService.showLoading();
  this.appService.httpRequest(Api.Outstock + Api.submitPickInfoWithOrder, Method.Post, params, ContenType.Json, result => {
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
