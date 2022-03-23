import { Component, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, ViewController, Events } from 'ionic-angular';
import { Utils } from '../../../../services/Utils';
import { AppService } from "../../../../services/App.service";
import { AppConstant, Api, ContenType, Method, BaseCodeConstant } from '../../../../utils/appConstant';
import { NativeService } from "../../../../services/NativeService";
import { Storage } from '@ionic/storage';
import { BarcodeService , BarcodeType} from '../../../../services/BarCodeService';
import { AppGlobal } from '../../../../services/AppGlobal';
/**
 * Generated class for the MovesboxInfoPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-movesbox-info',
  templateUrl: 'movesbox-info.html',
})
export class MovesboxInfoPage {

  skuStockList: any[] = [];//移动数据列表

  targetLocCode: string;//目标库位

  targetLpnCode: string;//目标容器

  public sysparms: any = [];//系统参数

  sourceLocCode:string;

  sourceLpnCode:string;

  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public appService: AppService,
    public nativeService: NativeService,
    public viewCtrl: ViewController,
    public storage: Storage,
    public events: Events,
    public ngZone:NgZone,
    public barcodeService: BarcodeService,
  ) {

    if (Utils.isNotEmpty(this.navParams.get('skuStockList'))) {//订单编号输入
      this.skuStockList = this.navParams.get('skuStockList');
    }
    if (Utils.isNotEmpty(this.navParams.get('sourceLocCode'))) {//原库位
      this.sourceLocCode = this.navParams.get('sourceLocCode');
    }
    if (Utils.isNotEmpty(this.navParams.get('sourceLpnCode'))) {//原容器
      this.sourceLpnCode = this.navParams.get('sourceLpnCode');
    }
    this.storage.get('sysParam').then(data => {
      this.sysparms = data.filter(x => x.paramKey == 'system::lpnEnable')[0];
    });

  }
  ionViewWillEnter() {
    //事件注册（扫描结果接收）
    this.events.subscribe('barcode:scan', (barcode, time) => {
      let bt = this.barcodeService.GetBarcodeType(barcode);
      if (bt ==  BarcodeType.HuoWei) {
        this.ngZone.run(()=>{
          this.targetLocCode = barcode;
        });
      }
    });
  }
  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
  }
  ionViewDidLoad() {
    console.log('ionViewDidLoad MovesboxInfoPage');
  }
  /**
   * 库位转移提交
   */
  btnOk() {
    if (Utils.isEmpty(this.targetLocCode)) {
      this.nativeService.showToast('请填写目标库位');
      return;
    }
    for (let index = 0; index < this.skuStockList.length; index++) {
      this.skuStockList[index]['targetLocCode'] = this.targetLocCode;
      this.skuStockList[index]['targetLpnCode'] = this.targetLpnCode;
    }
    this.appService.httpRequest(Api.stockInner + Api.submitStockForMoveByBox,
      Method.Post, this.skuStockList, ContenType.Json, result => {
        this.nativeService.showToast('提交成功');
        this.viewCtrl.dismiss(true);
      })
  }

  exit() {
    this.viewCtrl.dismiss(false);
  }
}
