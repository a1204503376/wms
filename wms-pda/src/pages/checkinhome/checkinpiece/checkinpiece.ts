import { Component, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, Events  } from 'ionic-angular';
import { AppService } from "../../../services/App.service";
import { Utils } from '../../../services/Utils';
import { NativeService } from "../../../services/NativeService"
import { Api, ContenType, Method } from '../../../utils/appConstant';
import { AppGlobal } from '../../../services/AppGlobal';

/**
 * Generated class for the CheckinpiecePage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-checkinpiece',
  templateUrl: 'checkinpiece.html',
})
export class CheckinpiecePage {
  public current: number = 1;//当前页
  public size: number = 10;//数量
  asnBillNo: string;//订单编号
  orderNo:string;
  public records: any[] = [];
  public infiniteScroll:any;
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public appService: AppService,
    public nativeService: NativeService,
    private events: Events,
    public ngZone: NgZone
  ) {

  }

  itemInfo() {
    this.navCtrl.push('CheckInPage');
  }

  ionViewWillEnter() {
    //事件注册（扫描结果接收）
    if (AppGlobal.scanFlag) {
      AppGlobal.scanFlag = false;
      this.events.subscribe('barcode:scan', (barcode, time) => {
        this.analysisDate(barcode);
      });
    }
    if(this.navParams.get('flag')|| false){
      this.resetPage();
    }
  }
  resetPage(){
    this.records = [];
    if(this.infiniteScroll){
      this.infiniteScroll.enable(true);
    }
    this.size = 10;
    this.current = 1;
  }
  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
  }
  ionViewDidEnter() {
    this.asnBillNo = '';
  }
  /**
 * 数据解析
 */
  analysisDate(barcode) {
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
  onClick(record){
    this.asnHeaderList(record.asnBillNo);
  }
  asnHeaderList(asnBillNo) {
    this.nativeService.showLoading();
    if (Utils.isEmpty(asnBillNo)) {
      this.nativeService.showToast('请输入订单编号');
      AppGlobal.scanFlag = false;
      return;
    }
    let params = {
      asnBillNo: asnBillNo
    }
    this.appService.httpRequest(Api.Instock + Api.AsnHeaderList, Method.Post, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      AppGlobal.scanFlag = false;
      if (Utils.isEmpty(result.data)) {
        this.nativeService.showToast('该单据已经完成或不存在');
      } else {
        if (Utils.isNotEmpty(result.data[0].asnDetailMinVO)) {
          this.asnBillNo = '';
          this.navCtrl.push('CheckInPage', { result: result.data[0] });
        }
      }
    });
  }


  getContainerList() {
    if(Utils.isEmpty(this.orderNo)){
      this.nativeService.showToast("单据编号不能为空！");
      return;
    }
    this.appService.httpRequest(Api.Instock + Api.getAsnHeaderList,Method.Post,
      'current='+this.current+'&'+
      'size='+this.size+'&'+
      'orderNo='+this.orderNo
      , ContenType.Form,result => {
      if(result.data.records.length==1){
        let asnBillNo = result.data.records[0].asnBillNo;
        this.asnHeaderList(asnBillNo);
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
  loadData(infiniteScroll) {
    this.infiniteScroll = infiniteScroll;
    this.getContainerList();
  }
}
