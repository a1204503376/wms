import { Component, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, Events } from 'ionic-angular';
import { AppService } from "../../../services/App.service";
import { Utils } from '../../../services/Utils';
import { NativeService } from "../../../services/NativeService"
import { Api, ContenType, Method } from '../../../utils/appConstant';
import { BarcodeService, BarcodeType } from '../../../services/BarCodeService';
import { AppGlobal } from '../../../services/AppGlobal';

/**
 * Generated class for the TakeawaypieceboxPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-takeawaytray',
  templateUrl: 'takeawaytray.html',
})
export class TakeawayTrayPage {

  asnBillNo: string;//订单编号
  orderNo:string;
  public current: number = 1;//当前页
  public size: number = 10;//数量
  public records: any[] = [];
  public PickInfoResult: any;//任务数据实体
  public infiniteScroll:any;
  public pickPlansList: any;//任务物品集合
  public pickPlansModel: any;//当前选中任务
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
    this.getHeadInfo(record.billNo);
  }
  getContainerList() {
    if(Utils.isEmpty(this.orderNo)){
      this.nativeService.showToast("单据编号不能为空！");
      return;
    }
    this.appService.httpRequest(Api.api + Api.takewayList, Method.Post, 
      'current='+this.current+'&'+
      'size='+this.size+'&'+
      'orderNo='+this.orderNo
      , ContenType.Form, result => {
      if(result.data.records.length==1){
        this.getHeadInfo(result.data.records[0].billNo);
      }else{
        this.records = this.records.concat(result.data.records);
        if(Utils.isEmpty(this.records)){
          if(this.infiniteScroll){ 
            this.infiniteScroll.enable(false);
            this.infiniteScroll.complete(); 
          }else{
            this.nativeService.showToast("该单据没有查到拣货任务");
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
  resetPage(){
    this.records = [];
    if(this.infiniteScroll){
      this.infiniteScroll.enable(true);
    }
    this.size = 10;
    this.current = 1;
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
  getHeadInfo(wellenNo) {
    let params = {
      wellenNo: wellenNo
    }
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.Outstock + Api.getPickInfoByBox, Method.Get, params, ContenType.Form, result => {
      this.nativeService.hideLoading();
      AppGlobal.scanFlag = true;
      this.PickInfoResult = result.data;
      this.pickPlansList = this.PickInfoResult.pickPlans;
      if (Utils.isEmpty(this.pickPlansList)) {
        this.nativeService.showToast('当前任务下没有物品');
        return;
      }
      //默认取第一条
      this.pickPlansModel = this.pickPlansList[0];
      if (Utils.isNotEmpty(this.pickPlansModel)) {
        this.navCtrl.push('TakeawayinTrayPage', { pickPlansModel: this.pickPlansModel, PickInfoResult: this.PickInfoResult, pickPlansList: this.pickPlansList, wellenNo: wellenNo });
      }
    });
  }
}
