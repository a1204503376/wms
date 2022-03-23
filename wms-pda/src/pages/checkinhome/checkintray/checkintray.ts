import { Component, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, Events } from 'ionic-angular';
import { AppService } from "../../../services/App.service";
import { Utils } from '../../../services/Utils';
import { NativeService } from "../../../services/NativeService"
import { Api, ContenType, Method } from '../../../utils/appConstant';
import { BarcodeService, BarcodeType } from '../../../services/BarCodeService';
import { AppGlobal } from '../../../services/AppGlobal';

/**
 * Generated class for the CheckinboxPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-checkintray',
  templateUrl: 'checkintray.html',
})
export class CheckInTrayPage {

  asnBillNo: string;//订单编号
  orderNo:string;
  public current: number = 1;//当前页
  public size: number = 10;//数量
  public records: any[] = [];
  public infiniteScroll:any;
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public appService: AppService,
    public nativeService: NativeService,
    private events: Events,
    public barCodeService: BarcodeService,
    public ngZone: NgZone
  ) {
  }
  ionViewDidEnter() {
    this.asnBillNo = '';
  }
  ionViewWillEnter() {
    //事件注册（扫描结果接收）
    this.events.subscribe('barcode:scan', (barcode, time) => {
      if (AppGlobal.scanFlag) {
        AppGlobal.scanFlag = false;
        this.analysisDate(barcode);
      }
    });
    if(this.navParams.get('flag')|| false){
      this.resetPage();
    }
  }
  onClick(record){
    this.asnBillNo = record.asnBillNo;
    this.headerInfoForBox();
  }
  getContainerList() {
    if(Utils.isEmpty(this.orderNo)){
      this.nativeService.showToast("单据编号不能为空！");
      return;
    }
    this.appService.httpRequest(Api.Instock + Api.getAsnHeaderList, Method.Post,
      'current='+this.current+'&'+
      'size='+this.size+'&'+
      'orderNo='+this.orderNo
      , ContenType.Form, result => {

      if(result.data.records.length==1){
        this.asnBillNo = result.data.records[0].asnBillNo;
        this.headerInfoForBox();
      }else{
        this.records = this.records.concat(result.data.records);
        if(Utils.isEmpty(this.records)){
          if(this.infiniteScroll){
            this.infiniteScroll.enable(false);
            this.infiniteScroll.complete();
          }else{
            this.nativeService.showToast("单据不存在或者已完成");
          }
          return;
        }
      }
      this.current++;
      if (this.infiniteScroll) {
        this.infiniteScroll.complete();   /*请求完成数据更新页面*/
      }
    });
  }
  resetPage(){
    this.records = [];
    if(this.infiniteScroll){
      this.infiniteScroll.enable(true);
    }
    this.size = 10;
    this.current = 1;
  }
  loadData(infiniteScroll) {
    this.infiniteScroll = infiniteScroll;
    this.getContainerList();
  }
  toggleFun() {
    console.log(this.asnBillNo);
  }
  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
  }
  /**
   * 数据解析
   */
  analysisDate(barcode) {

    // let bt = this.barCodeService.GetBarcodeType(barcode);
    this.ngZone.run(() => {
      this.orderNo = barcode;
      this.resetPage();
      this.getContainerList();
    });
  }
  /**
  * 入库单回车事件
  */
  keyEnter(event) {
    if (event && event.keyCode == 13) {
      this.resetPage();
      this.getContainerList();
    }
  }
  /**
   * 获取入库单详情
   */
  headerInfoForBox() {
    this.nativeService.showLoading();
    if (Utils.isEmpty(this.asnBillNo)) {
      this.nativeService.showToast('请输入订单编号');
      AppGlobal.scanFlag = true;
      return;
    }
    let params = {
      asnBillNo: this.asnBillNo
    }
    this.appService.httpRequest(Api.Instock + Api.getAsnHeaderInfoForBox, Method.Get, params, ContenType.Form, result => {
      this.nativeService.hideLoading();
      AppGlobal.scanFlag = true;
      if (Utils.isEmpty(result.data)) {
        this.nativeService.showToast('该单据已经完成或不存在');
      } else {
        if (Utils.isNotEmpty(result.data)) {
          this.navCtrl.push('CheckInTrayInfoPage', { result: result.data });
        }
      }
    });
  }

}
