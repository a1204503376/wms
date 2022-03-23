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

/**
 * Generated class for the CheckinBoxInfoPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-checkin-box-info',
  templateUrl: 'checkin-box-info.html',
})

export class CheckinBoxInfoPage {

  public result: any;//单据信息
  public asnBillNo: string;//单据编号
  public boxCode: string;//箱号
  public scanModelFlag: boolean = true;//二维码状态
  public sname: string;//供应商名称 
  public planCountQty: string;//计划总数量
  public scanCountQty: string;//实际总数量
  public finish: string; //明细单完成数
  public count: string//明细单总数

  public ruleCode: number;//规则编号

  public labelType: any;//标签类型

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
  public skuLotModel: SkuLotModel = new SkuLotModel();

  public scanModel: ScanModel = new ScanModel();//箱码解析实体
  public scanModelRecords: any = [];
  public scanView: string;//箱码页面实体

  public asnDetails: any;//存储单据ID  和明细ID
  public taskId: string;
  public skuPackageDetails: string[] = [];
  public boxItems: string[] = [];
  @ViewChild('labelTypecom') labelTypecom;

  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public appService: AppService,
    public nativeService: NativeService,
    public barcodeService: BarcodeService,
    private events: Events,
    public ngZone: NgZone
  ) {
    if (Utils.isNotEmpty(this.navParams.get('result'))) {//订单编号输入
      this.result = this.navParams.get('result');
      this.dataFilling();
    }
    if (Utils.isNotEmpty(this.navParams.get('taskId'))) {//订单编号输入
      this.taskId = this.navParams.get('taskId');
    }
  }
  boxCode_KeyDown(event) {
    if (event && event.keyCode == 13) {
      if(this.boxItems.find((n)=>n==this.boxCode)){
        this.nativeService.showToast("该箱号已扫描过！");
        return;
      }
      this.boxItems.push(this.boxCode);
    }
  }
  removeItem(item) {
    this.boxItems = this.boxItems.filter(x => x != item);
  }
  ionViewWillEnter() {
    this.labelTypecom.qtyDisabled = false;
    this.labelTypecom.qtyFlag = false;
    //事件注册（扫描结果接收）
    if (AppGlobal.scanFlag) {
      AppGlobal.scanFlag = false;
      this.events.subscribe('barcode:scan', (barcode, time) => {
        this.analysisDate(barcode);
      });
    }
  }
  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
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
  dataFilling() {
    this.orderNo = this.result.orderNo;
    this.asnBillNo = this.result.asnBillNo;
    this.sname = this.result.sname;
    this.planCountQty = this.result.planCountQty;
    this.scanCountQty = this.result.scanCountQty;
    this.finish = this.result.finish;
    this.count = this.result.count;
    this.ruleCode = this.result.ruleCode;
    this.boxCode = this.result.boxCode;
    //箱码状态
    if (this.ruleCode == AppConstant.ruleCode_00) {
      this.EidtFlag = false;
    } else if (this.ruleCode == AppConstant.ruleCode_01) {
      this.EidtFlag = true;
    } else {
      this.isEidtFlag = false;
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
  /**
   * 检测物品信息
   * @param scanView 
   */
  scanWmsSku() {
    //箱码解析
    // this.barcodeService.ScanSkuBarcode(scanView).subscribe(val => {
    //   if (Utils.isEmpty(val.skucode)) {
    //     this.nativeService.showToast('物品编码不能为空');
    //   }
    let params = {
      boxCode: this.boxCode
    }
    //获取物品
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.api + Api.SkuList, Method.Post, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      // AppGlobal.scanFlag = true;
      //判断是否多个物品
      if (result.data.length >= 1) {
        //基础计量单位
        let levelUm = result.data[0].skuPackage.skuPackageDetailVOList.find(x => x.skuLevel == 1);
        if (Utils.isEmpty(levelUm)) {
          this.nativeService.showToast('没有检测到当前物品的基础计量单位');
          return;
        }
        // this.getAsnHeaderDetailForBox(result.data[0], val, levelUm);
      } else {
        this.nativeService.showToast('没有检测到物品信息');
      }
    });
    // })
  }
  /**
   *根据箱码数据获取数据详情 
   */
  getAsnHeaderDetailForBox(data, model, levelUm) {
    this.scanModel = model;
    if (Utils.isNotEmpty(model)) {
      let skuLotModel = this.barcodeService.convertLotModel(model);
      let param = {
        asnBillNo: this.asnBillNo,
        skuCode: model.skucode,
        skuName: model.skuname,
        wsuName: levelUm.wsuName,
        skuLots: skuLotModel
      }
      this.appService.httpRequest(Api.Instock + Api.getAsnHeaderDetailForBox, Method.Post, param, ContenType.Json, result => {
        this.nativeService.hideLoading();
        if (!result.success) {
          this.nativeService.showToast(result.msg);
        } else {
          this.asnDetails = result.data.asnDetails[0];
          this.asnInfo = result.data.asnDetail;
          this.sku = result.data.sku;
          this.totalPlanQty = result.data.totalPlanQty;
          this.totalScanQty = result.data.totalScanQty;
          this.planQty = this.asnInfo.planCountQty;
          this.scanQty = this.asnInfo.scanCountQty;
          this.planQtyName = this.asnInfo.planQtyName;
          this.scanQtyName = this.asnInfo.scanQtyName;;
          this.skuName = this.asnInfo.skuName;
          this.scanQty = this.asnInfo.scanCountQty;
          this.detailStatus = this.asnInfo.detailStatusName;
          this.skuPackageDetails = result.data.skuPackageDetails;
          //绑定批属性
          result.data.skuConfig.forEach(element => {
            model.skuLotModel[`skuLotName${element.skuLotIndex}`] = element.skuLotLabel;
          })
          model.um = levelUm.wsuName;
          this.skuSpec = levelUm.skuSpec;
          if (Utils.isEmpty(this.scanModelRecords)) {
            this.scanModelRecords.push(model);
          } else {
            for (let item of this.scanModelRecords) {
              if (Utils.isObjectValueEqual1(model, item, ['skuname', 'qty', 'setLotModel', 'setLotValue'])) {
                item.qty = Number.parseInt(item.qty) + Number.parseInt(model.qty);
                model = item;
              } else {
                if (!Utils.contains(this.scanModelRecords, model, ['skuname', 'qty', 'setLotModel', 'setLotValue'])) {
                  this.scanModelRecords.push(model);
                  break;
                }
              }
            }
          }
          this.labelTypecom.changeRun(model);

        }
      });
    }

  }
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
    // if (Utils.isEmpty(this.asnInfo)) {
    //   this.nativeService.showToast('没有扫描物品信息');
    //   return;
    // }
    if(this.boxItems.length==0){
      this.nativeService.showToast('请扫描箱号！');
        return;
    }
    let params = {
      asnBillNo: this.asnBillNo,
      // whId: this.sku.whId,
      // asnBillId: this.asnDetails.asnBillId,
      // asnDetailId: this.asnDetails.asnDetailId,
      // skuCode: this.labelTypecom.scanModel.skucode,
      // scanQty: this.labelTypecom.scanModel.qty,
      // wsuName: this.labelTypecom.scanModel.um,
      locCode: this.labelTypecom.locCode,
      // skuLots: this.barcodeService.convertLotModel(this.labelTypecom.scanModel),
      boxCodes: this.boxItems
      // lpnCode: " ",
      // taskId: this.taskId
    };
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.Instock + Api.submitAsnHeaderForBox, Method.Post, params, ContenType.Json, result => {
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
    this.scanModelFlag = true;
    this.scanModel = null;
    this.scanView = '';
    this.scanModelRecords = [];
    this.boxCode = '';
    this.boxItems = [];
  }
  /**
   * 退出
   */
  exit() {
    if (this.labelTypecom.ComponentFlag) {
      this.labelTypecom.ComponentFlag = false;
      this.clearModel();
    } else {
      this.navCtrl.pop();

    }
  }
}
