import { Component, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, ModalController, Events } from 'ionic-angular';
import { Api, ContenType, Method, AppConstant } from '../../../../utils/appConstant';
import { AppService } from "../../../../services/App.service";
import { Utils } from '../../../../services/Utils';
import { NativeService } from "../../../../services/NativeService"
import { SkuLotValResultModel, SkuLot } from '../../../../models/SkuLotValResultModel';
import { Storage } from '@ionic/storage';
import { LoginUserInfoModel } from '../../../../models/SystemFramework.Model';
import { BarcodeService, BarcodeType } from '../../../../services/BarCodeService';
import { AppGlobal } from '../../../../services/AppGlobal';
/**
 * Generated class for the CyclecoutrantaskinfoPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-cyclecoutrantaskinfo',
  templateUrl: 'cyclecoutrantaskinfo.html',
})
export class CyclecoutrantaskinfoPage {
  public IsShow: number;
  public Batch: number;
  public serialNumber: number;
  public serialListNum: number;
  public BatchList: number;
  public result: any;

  //页面实体
  public buttonEnt: string;//提交按钮

  public skuCode: string;//物品编号

  public skuName: string;//物品名称

  public skuSpec: string;//物品规格

  public lpnCode: string;//容器编码

  public scanQtynum: string;//实际数量

  public locCode: string;//库位编码

  public skuPackage: any;//选中包装

  public skuPackageDetailVOList: any;//包装列表

  public skuPackageDetailVOItem: any;//包装选项

  public resultSkuItem: any;//物品详情

  public skuLot: any;//批属性信息

  public skuLotVal: any;//批属性验证

  public countQty: number;//盘点数量

  public serialNumberList: string[] = [];//序列号已扫描集合
  public serialNumberModel: string;//序列号实体

  public ScanNumberLength: number;//扫描的序列号数量

  public stockList: any;//多批次实体

  public stockItem: any;//批次选项

  public disabled: boolean = true;//批属显示隐藏

  public modelNum: number;

  public whName: string;//仓库名称

  public lotNumber: number;//批属性显示数量

  public boxCode: string;//箱号

  public skuConfigArray: SkuLotValResultModel[] = [];

  public scanView: string;//箱码

  public sysparms: any = [];//系统参数

  public countBillNo: string;
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public appService: AppService,
    public nativeService: NativeService,
    public modalCtrl: ModalController,
    public storage: Storage,
    public ngZone: NgZone,
    public events: Events,
    public barcodeService: BarcodeService
  ) {
    this.storage.get('sysParam').then(data => {
      this.sysparms = data.filter(x => x.paramKey == 'system::lpnEnable')[0];
      this.lotNumber = data.filter(x => x.paramKey == 'account:lotCount')[0].paramValue;
    });
    if (Utils.isNotEmpty(this.navParams.get('result'))) {
      this.result = this.navParams.get('result');
      this.locCode = this.result.locCode;
      this.whName = this.result.whName;
    };
  }
  /**
  * 数据格式化
  */
  clearMode() {
    this.skuCode = '';//物品编号
    this.skuName = '';//物品名称
    this.skuSpec = '';//物品规格
    this.lpnCode = '';//容器编码
    this.scanQtynum = '';//实际数量
    // this.locCode = '';//库位编码
    this.skuPackage = null;//选中包装
    this.skuPackageDetailVOList = null;//包装列表
    this.skuPackageDetailVOItem = null;//包装选项
    this.resultSkuItem = null;//物品详情
    this.skuLot = null;//批属性信息
    this.skuLotVal = null;//批属性验证
    this.countQty = 0;//盘点数量
    this.serialNumberList = [];//序列号已扫描集合
    this.serialNumberModel = "";//序列号实体
    this.ScanNumberLength = 0;//扫描的序列号数量
    this.stockList = null;//多批次实体
    this.stockItem = null;//批次选项
    this.disabled = true;//批属显示隐藏
    this.IsShow = this.Batch;
  }
  ionViewWillEnter() {
    this.Batch = AppConstant.Batch;
    this.serialNumber = AppConstant.serialNumber;
    this.serialListNum = AppConstant.serialNumberListNum;
    this.BatchList = AppConstant.BatchList;
    this.IsShow = this.Batch;
    this.buttonEnt = AppConstant.buttonAffirm;
    //事件注册（扫描结果接收）
    this.events.subscribe('barcode:scan', (barcode, time) => {
      let bt = this.barcodeService.GetBarcodeType(barcode);
      if (bt == BarcodeType.SKU) {
        this.ngZone.run(() => {
          this.skuCode = barcode;
          this.scanWmsSku(barcode);
        });
      } else if (bt == BarcodeType.TuoPan) {
        this.ngZone.run(() => {
          this.lpnCode = barcode;
        });
      } else {
        this.ngZone.run(() => {
          this.barcodeService.ScanSkuBarcode(this.scanView).subscribe(val => {
            this.scanWmsSku(val.skucode);
          });
        });
      }
    });
  }
  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
  }
  /**
  * sku回车事件
  */
  skuCode_KeyDown(event) {
    if (event && event.keyCode == 13) {
      this.scanWmsSku(this.skuCode);
    }
  }
  /**
 * 箱码扫描
 * @param event 
 */
  scanModelChange(event) {
    if (event && event.keyCode == 13) {
      if (Utils.isNotEmpty(this.scanView)) {
        this.barcodeService.ScanSkuBarcode(this.scanView).subscribe(val => {
          this.skuCode = val.skucode;
          this.scanWmsSku(val.skucode);
        });
      } else {
        this.nativeService.showToast('请扫描箱码');
      }
    }
  }
  /**
  * 容器回车事件
  */
  lpnCode_KeyDown(event) {
    if (event && event.keyCode == 13) {
      //获取最近使用的库位
      if (Utils.isEmpty(this.lpnCode)) {
        this.nativeService.showToast('请输入容器编码');
      }
      let params = 'lpnCode=' + this.lpnCode;
      this.nativeService.showLoading();
      this.appService.httpRequest(Api.api + Api.stockGetLocByLpnCode, Method.Post, params, ContenType.Form, result => {
        this.nativeService.hideLoading();
        this.locCode = result.data.locCode;
        console.log(result);
      })
    }
  }
  /*物品扫描*/
  scanWmsSku(skuCode) {
    if (Utils.isEmpty(skuCode)) {
      this.nativeService.showToast('物品编码不能为空');
    }
    let params = {
      skuCode: skuCode
    }
    //获取物品
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.api + Api.SkuList, Method.Post, params, ContenType.Json, result => {
      //判断是否多个物品
      this.nativeService.hideLoading();
      console.log(result);
      if (result.data.length > 1) {
        //选择物品
        this.openAsnRecordModal(result.data);
      } else {
        this.resultSkuItem = result.data[0];
        if (Utils.isNotEmpty(this.resultSkuItem)) {
          this.InValAssignment();
        } else {
          this.nativeService.showToast('没有查询到物品');
        }
      }
    });
  }

  /**
   *查看物品明细
  */
  openAsnRecordModal(skuList) {
    let myModal = this.modalCtrl.create('SkuListModal', {
      skuList: skuList,
    });
    myModal.onDidDismiss(data => {
      if (Utils.isNotEmpty(data)) {
        this.resultSkuItem = data;
        this.InValAssignment();
      }
    });
    myModal.present();
  }
  /**
  *页面赋值
  */
  InValAssignment() {
    this.skuName = this.resultSkuItem.skuName;
    this.skuSpec = this.resultSkuItem.spec;
    this.skuPackage = this.resultSkuItem.skuPackage;
    this.skuPackageDetailVOList = this.resultSkuItem.skuPackage.skuPackageDetailVOList;
    this.skuLot = this.resultSkuItem.skuLot;
    this.skuLotVal = this.resultSkuItem.skuLotVal;
    let minPackage = this.skuPackageDetailVOList.filter(x => x.skuLevel == 1);
    if (Utils.isNotEmpty(minPackage))
      this.skuPackageDetailVOItem = minPackage[0];

    this.skuConfigArray = [];
    for (let index = 1; index <= this.lotNumber; index++) {
      let skuConfig = new SkuLotValResultModel();
      if (Utils.isNotEmpty(this.skuLot['skuLotLabel' + index])) {
        skuConfig.skuLotLabel = this.skuLot['skuLotLabel' + index];
      } else {
        skuConfig.skuLotLabel = '批属性' + index;
      }
      skuConfig.skuLotMixMask = this.skuLotVal['skuLotMixMask' + index];

      skuConfig.getLotEditVal();
      this.skuConfigArray.push(skuConfig);
    }
    //isSn 1为序列号 0为批次号
    if (this.resultSkuItem.isSn == 1) {
      this.IsShow = this.serialNumber;
    }
  }
  /**
   * 提交
   */
  btnOk() {
    if (this.IsShow == AppConstant.Batch) {//批次
      this.randomCountSubmit();
    } else if (this.IsShow == AppConstant.BatchList) {//批次列表提交
      if (this.stockItem == '新增批次号') {
        for (let index = 0; index < this.skuConfigArray.length; index++) {
          if (this.stockList[index]) {
            let count = [];
            for (let i = 1; i <= this.skuConfigArray.length; i++) {
              let a = 'skuLot' + i;
              if (this.stockList[index][a] != this.skuConfigArray[i - 1].LotValue) {
                count.push(true);
              }
            }
            if (count.length == 0) {
              this.nativeService.showToast('新增批次值与' + this.stockList[index].lotNumber + '重复');
              return;
            }
          }
        }
      }
      this.randomCountSubmit();
    } else {
      this.randomCountSubmit();
    }
  }
  //盘点提交
  async randomCountSubmit() {
    let wspdId;
    let userInfo: LoginUserInfoModel = await this.storage.get('userInfo');
    if (this.sysparms.paramValue == 0) {
      if (Utils.isEmpty(this.lpnCode)) {
        this.nativeService.showToast('容器为空,请扫描容器');
        return;
      }
    } else {
      if (Utils.isNotEmpty(this.stockItem))
        this.lpnCode = this.stockItem.lpnCode
    }
    if (Utils.isEmpty(this.locCode)) {
      this.nativeService.showToast('库位为空,请扫描库位');
      return;
    }
    if (Utils.isEmpty(this.resultSkuItem)) {
      this.nativeService.showToast('物品信息没有扫描到,请扫描后在提交');
      return;
    }
    if (this.resultSkuItem.isSn == 1) {//序列号物品
      if (Utils.isEmpty(this.serialNumberList)) {
        this.nativeService.showToast('序列号为空');
        return;
      }
      this.countQty = this.ScanNumberLength;
      if (Utils.isEmpty(this.skuPackageDetailVOList)) {
        this.nativeService.showToast('当前物品选中的包装没有包装单位,请在PC绑定');
        return;
      }
      let sku = this.skuPackageDetailVOList.filter(x => x.skuLevel == 1);
      if (Utils.isEmpty(sku)) {
        this.nativeService.showToast('当前物品没有基础计量单位的,请绑定');
        return;
      }
      wspdId = sku[0].wspdId;//序列号获取包装
    } else {//批次号物品
      if (Utils.isEmpty(this.skuPackageDetailVOItem)) {
        this.nativeService.showToast('请选择包装');
        return;
      }
      wspdId = this.skuPackageDetailVOItem.wspdId;//批次获取包装
      if (Utils.isEmpty(this.countQty)) {
        this.nativeService.showToast('数量为空,请输入数量');
        return;
      }
    }
    if (this.IsShow == this.BatchList) {
      if (Utils.isEmpty(this.stockItem)) {
        this.nativeService.showToast('请选择批次号');
        return;
      }
    }
    //获取批次号
    let lotNumber = '';
    if (Utils.isNotEmpty(this.stockItem) && this.stockItem != '新增批次号') {
      lotNumber = this.stockItem.lotNumber;
    }
    if (Utils.isNotEmpty(this.stockItem)) {
      this.boxCode = this.stockItem.boxCode;
    }
    let skuLot = new SkuLot();
    for (let index = 0; index < this.skuConfigArray.length; index++) {
      skuLot['skuLot' + (index + 1)] = this.skuConfigArray[index].LotValue
    }

    //盘点提交
    let params = {
      lpnCode: this.lpnCode,
      locCode: this.locCode,
      skuId: this.resultSkuItem.skuId,
      lotNumber: lotNumber,
      countQty: this.countQty,
      wspdId: wspdId,
      boxCode: this.boxCode,
      userId: userInfo.tenant_id,
      serialNumberList: this.serialNumberList,
      // skuLot1: this.skuConfigArray[0].LotValue,
      // skuLot2: this.skuConfigArray[1].LotValue,
      // skuLot3: this.skuConfigArray[2].LotValue,
      // skuLot4: this.skuConfigArray[3].LotValue,
      // skuLot5: this.skuConfigArray[4].LotValue,
      // skuLot6: this.skuConfigArray[5].LotValue,
      // skuLot7: this.skuConfigArray[6].LotValue,
      countBillNo: this.result.countBillNo,
      taskId: this.result.taskId,
      ...skuLot
    }
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.StockCountPDA + Api.billCountSubmit, Method.Post, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      if (result.data.stockList.length > 1) {
        this.stockList = result.data.stockList;
        console.log(this.stockList);
        //选择批次
        if (this.IsShow == this.Batch) {
          this.modelNum = this.countQty;
        } else {
          this.modelNum = this.ScanNumberLength;
        }
        this.IsShow = this.BatchList;
      } if (result.data.stockList.length == 0) {
        this.nativeService.showToast('提交成功');
        this.navCtrl.pop();
      } else {
        //是否进行清除和页面跳转
        this.nativeService.showToast('提交成功');
        this.clearMode();
      }
    })
  }
  /**
  * 批次号切换
  */
  toggle() {
    if (this.stockItem != '新增批次号') {
      for (let index = 0; index < this.skuConfigArray.length; index++) {
        this.skuConfigArray[index].LotValue = this.stockItem['skuLot' + (index + 1)];
      }
    } else {
      for (let index = 0; index < this.skuConfigArray.length; index++) {
        this.skuConfigArray[index].LotValue = "";
        this.disabled = false;
      }
    }
  }
  /**
  * 序列号回车事件
  */
  serialNumber_KeyDown(event) {
    if (event && event.keyCode == 13) {
      if (Utils.isNotEmpty(this.serialNumberModel)) {
        let flag = true;
        this.serialNumberList.forEach(element => {
          if (element.toUpperCase() == this.serialNumberModel.toUpperCase()) {
            flag = false;
          }
        });
        if (flag) {
          this.serialNumberList.push(this.serialNumberModel);
          this.serialNumberModel = '';
        }
        this.ScanNumberLength = this.serialNumberList.length;
      }
    }
  }
  /**
  * 查看序列号
  */
  numclick() {
    this.IsShow = this.serialListNum;
  }
  /**
 * 删除扫描序列号
 * @param item 
 */
  removeItem(item) {
    this.serialNumberList = this.serialNumberList.filter(x => x != item);
    //更新长度
    this.ScanNumberLength = this.serialNumberList.length;
  }
  compareFn(e1, e2): boolean {
    return e1 && e2 ? e1.wspdId === e2.wspdId : e1 === e2;
  }
  togglePage() {
    if (Utils.isNotEmpty(this.skuPackageDetailVOItem) && Utils.isNotEmpty(this.skuPackageDetailVOItem.convertQty)) {
      this.skuSpec = '1-' + this.skuPackageDetailVOItem.convertQty
    } else {
      this.skuSpec = '';
    }
  }
  /**
   * 返回
   */
  exit() {
    if (this.IsShow == AppConstant.Batch) {
      this.navCtrl.pop();
    } else if (this.IsShow == AppConstant.serialNumber) {
      this.IsShow = this.Batch;
    } else if (this.IsShow == AppConstant.serialNumberListNum) {
      this.IsShow = this.serialNumber;
    } else if (this.IsShow == AppConstant.BatchList) {
      if (this.resultSkuItem.isSn == 0) {
        this.IsShow = this.Batch;
      } else {
        this.IsShow = this.serialNumber;
      }
    }
  }
  ionViewDidLoad() {
    // console.log('ionViewDidLoad CyclecoutrantaskinfoPage');
  }

}
