import { Component, ViewChild, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, Events } from 'ionic-angular';
import { AppService } from "../../../../services/App.service";
import { Utils } from '../../../../services/Utils';
import { NativeService } from "../../../../services/NativeService"
import { AppConstant, Api, ContenType, Method, BaseCodeConstant } from '../../../../utils/appConstant';
import { SkuLotModel } from '../../../../models/SkuLotModel';
import { BarcodeService, BarcodeType } from '../../../../services/BarCodeService';
import { ScanModel } from '../../../../models/DataBase.Model';
import { AppGlobal } from '../../../../services/AppGlobal';
import { LpnItem } from './LpnItem';

/**
 * Generated class for the CheckinBoxInfoPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-takeawayintray',
  templateUrl: 'takeawayintray.html',
})

export class TakeawayinTrayPage {

  public result: any;//单据信息
  public asnBillNo: string;//单据编号
  public lpnCode: string;//箱号
  public scanModelFlag: boolean = true;//二维码状态
  public sname: string;//供应商名称 
  public planCountQty: string;//计划总数量
  public scanCountQty: string;//实际总数量
  public finish: string; //明细单完成数
  public count: string//明细单总数

  public ruleCode: number;//规则编号

  public labelType: any;//标签类型
  public lpnItems:any = [];
  EidtFlag: boolean = true;//箱码是否可编辑
  isEidtFlag: boolean = true;//箱码是否显示


  public planQtyName: string;//单据明细计划名称
  public scanQtyName: string;//单据明细已完成名称

  public planQty: string;//单据明细计划
  public scanQty: string;//单据明细已完成
  public skuCode: string;//物品编号
  public skuName: string;//物品名称
  public skuSpec: string;//物品规格
  public totalPlanQty: number = 0;
  public totalScanQty: number = 0;
  public detailStatus: string;//接受状态 10:未接收,20:已接收
  public sku: any;//物品信息
  public asnInfo: any;
  public orderNo: string;
  public whId: string;
  public skuLotModel: SkuLotModel = new SkuLotModel();

  public scanModel: ScanModel = new ScanModel();//箱码解析实体
  public scanModelRecords: any = [];
  public scanView: string;//箱码页面实体
  
  public asnDetails: any;//存储单据ID  和明细ID
  public taskId: string;
  public skuPackageDetails: string[] = [];
  public pickPlansModel: any;//当前选中任务
  public wellenNo: string;//波次编码
  @ViewChild('labelTypecom') labelTypecom;
  @ViewChild('lpncom') lpncom;
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public appService: AppService,
    public nativeService: NativeService,
    public barcodeService: BarcodeService,
    private events: Events,
    public ngZone: NgZone
  ) {
    this.taskId = this.navParams.get('taskId');
    this.wellenNo = this.navParams.get('wellenNo');
    this.pickPlansModel = this.navParams.get('pickPlansModel');
  }
  ionViewWillEnter() {
    this.labelTypecom.qtyFlag = true;
    this.labelTypecom.qtyDisabled =false;
    this.labelTypecom.locCode = 'PICK';
    //事件注册（扫描结果接收）
    if (AppGlobal.scanFlag) {
      AppGlobal.scanFlag = false;
      this.events.subscribe('barcode:scan', (barcode, time) => {
        this.analysisDate(barcode);
      });
    }
  }
  lpnCode_KeyDown(event) {
    if (event && event.keyCode == 13) {
      if(this.lpnItems.find((n)=>n.lpnCode==this.lpnCode)){
        this.nativeService.showToast("该托盘已扫描过！");
        return;
      }
      this.getTrayQty();
    }
  }
  getTrayQty(){
    this.nativeService.showLoading();
    let count = 0;
    if(this.lpnItems.length>0){
       count = this.lpnItems.map(x=>Number.parseFloat(x.qty)).reduce((a,b)=>a+b);
    }
    this.appService.httpRequest(Api.Outstock + Api.getInfoByLpnCode, Method.Get, {lpnCode:this.lpnCode, pickPlanId: this.pickPlansModel.pickPlanId,qty:count}, '', result => {
      this.nativeService.hideLoading();
      if(result.success){
        result.data['skuLotModel']=new SkuLotModel()
        this.labelTypecom.scanModel = result.data;
        this.lpnItems.push(result.data);
        this.lpncom.setFocus();
      }
    });
  }
  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
  }
  removeItem(item) {
    this.lpnItems = this.lpnItems.filter(x => x != item);
  }
  updateQty(item){
    this.lpnCode = item.lpnCode;
    this.labelTypecom.scanModel = item;
  }
  /**
 * 数据解析
 */
  analysisDate(barcode) {
    let bt = this.barcodeService.GetBarcodeType(barcode);
    // if (bt == BarcodeType.BoxCode) {
    //   this.ngZone.run(() => {
    //     this.scanWmsSku(barcode);
    //   });
    // }
    if (bt == BarcodeType.HuoWei) {
      this.ngZone.run(() => {
        this.labelTypecom.locCode = barcode;
      });
    }
  }
  
  ionViewDidLoad() {
    console.log('ionViewDidLoad CheckinBoxInfoPage');
  }
  // boxCode_KeyDown(event) {
  //   if (event && event.keyCode == 13) {
  //     if (Utils.isNotEmpty(this.boxCode)) {
  //       this.scanWmsSku();
  //     } else {
  //       this.nativeService.showToast('请扫描箱号');
  //     }
  //   }
  // }
  
  runParent(asnInfo: any) {
    this.scanModelFlag = false;

  }
  detailed() {
    this.navCtrl.push('CheckinDetailedPage',
      { asnBillNo: this.asnBillNo });
  }

  /**
   * 收货提交
   */
  btnOk() {
    if (Utils.isEmpty(this.lpnItems)) {
      this.nativeService.showToast('没有扫描托盘信息');
      return;
    }
    let params = {
      targetLocCode: this.labelTypecom.locCode,
      targetLpnCodes: this.lpnItems,
      taskId: this.taskId,
      wellenNo: this.wellenNo,
      pickPlanId: this.pickPlansModel.pickPlanId,
    };
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.Outstock + Api.submitTrayPickInfo, Method.Post, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      this.nativeService.showToast('提交成功');
      this.clearModel();
      // this.finish = result.data.asnDetail.finish;
      // this.count = result.data.asnDetail.count;

      // if (this.finish == this.count) {
      //   this.navCtrl.getPrevious().data.flag = true;
      //   this.navCtrl.pop();
      // }
      // else {
      //   //更新箱号
      //   this.appService.httpRequest(Api.Instock + Api.getBoxCode, Method.Get, '', ContenType.Content, result => {
      //     this.boxCode = result.data;
      //   });
      // }
    });
  }
  /**
   * 数据初始化
   */
  clearModel() {
    this.totalPlanQty = 0;
    this.totalScanQty = 0;
    this.planQty = '';//单据明细计划
    this.scanQty = '';//单据明细已完成
    this.planQtyName = '';
    this.scanQtyName = '';
    this.skuCode = '';//物品编号
    this.skuName = '';//物品名称
    this.skuSpec = '';//物品规格
    this.detailStatus = '';//接受状态 10:未接收,20:已接收
    this.sku = null;//物品信息
    this.labelTypecom.clearModel();
    this.labelTypecom.ComponentFlag = true;
    this.labelTypecom.locCode = 'PICK';
    this.scanModelFlag = true;
    this.scanModel = null;
    this.scanView = '';
    this.scanModelRecords = [];
    this.lpnCode = '';
    this.lpnItems = [];
  }
 
  exit() {
      this.navCtrl.pop();
  }
}
