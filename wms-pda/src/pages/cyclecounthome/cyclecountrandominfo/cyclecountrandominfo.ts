import { Component, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, ModalController, Events } from 'ionic-angular';
import { Api, ContenType, Method, AppConstant } from '../../../utils/appConstant';
import { AppService } from "../../../services/App.service";
import { Utils } from '../../../services/Utils';
import { NativeService } from "../../../services/NativeService"
import { SkuLotValResultModel, SkuLot } from '../../../models/SkuLotValResultModel';
import { Storage } from '@ionic/storage';
import { LoginUserInfoModel } from '../../../models/SystemFramework.Model';
import { Keyboard } from '@ionic-native/keyboard';
import { AppGlobal } from '../../../services/AppGlobal';
import { BarcodeService, BarcodeType } from '../../../services/BarCodeService';
/**
 * Generated class for the CyclecountrandominfoPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-cyclecountrandominfo',
  templateUrl: 'cyclecountrandominfo.html',
})
export class CyclecountrandominfoPage {
  public IsShow: number;

  public Batch: number;

  public serialNumber: number;

  public serialListNum: number;

  public BatchList: number;
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

  public sysparms: any = [];//系统参数

  public skuConfigArray: SkuLotValResultModel[] = [];

  public boxCode: string;//箱号

  public lotNumber: number;//批属性显示数量

  public scanView: string;//箱码

  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public appService: AppService,
    public nativeService: NativeService,
    public modalCtrl: ModalController,
    public storage: Storage,
    public barCodeService: BarcodeService,
    public keyboard: Keyboard,
    public events: Events,
    public ngZone: NgZone,
  ) {
    this.storage.get('sysParam').then(data => {
      this.sysparms = data.filter(x => x.paramKey == 'system::lpnEnable')[0];
      this.lotNumber = data.filter(x => x.paramKey == 'account:lotCount')[0].paramValue;
    });
    //获取库房列表
    // this.nativeService.showLoading('正在获取库房，请稍后。。。');
    // this.appService.httpRequest(Api.api + Api.ApiPDA, Method.Post, '', ContenType.Form, result => {
    //   this.nativeService.hideLoading();
    //   if (Utils.isNotEmpty(result.data)) {
    //     this.WarehouserModel = result.data;
    //   }
    // });
  }
  ionViewWillEnter() {
    this.Batch = AppConstant.Batch;
    this.serialNumber = AppConstant.serialNumber;
    this.serialListNum = AppConstant.serialNumberListNum;
    // this.Warehouse = AppConstant.Warehouse;
    this.BatchList = AppConstant.BatchList;
    this.IsShow = this.Batch;
    this.buttonEnt = AppConstant.buttonAffirm;
    //事件注册（扫描结果接收）
    this.events.subscribe('barcode:scan', (barcode, time) => {
      let bt = this.barCodeService.GetBarcodeType(barcode);
      if (bt == BarcodeType.SKU) {
        this.ngZone.run(() => {
          this.skuCode = barcode;
          this.scanWmsSku(barcode);
        });
      } else if (bt == BarcodeType.HuoWei) {
        this.ngZone.run(() => {
          this.locCode = barcode;
        });
      } else if (bt == BarcodeType.TuoPan) {
        this.ngZone.run(() => {
          this.lpnCode = barcode;//容器
        });
      } else {
        this.ngZone.run(() => {
          this.barCodeService.ScanSkuBarcode(barcode).subscribe(val => {
            if(this.IsShow==AppConstant.BatchList){
                for (let index = 0; index < this.skuConfigArray.length; index++) {
                  this.skuConfigArray[index].LotValue = val.skuLotModel[`skulot${index+1}`];
                }
            }else{
              this.skuCode = val.skucode;
              this.scanWmsSku(val.skucode);
            }
          });
        });
      }
    });
  }
  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
  }
  /**
   * 箱码扫描
   * @param event 
   */
  scanModelChange(event) {
    if (event && event.keyCode == 13) {
      if (Utils.isNotEmpty(this.scanView)) {
        this.barCodeService.ScanSkuBarcode(this.scanView).subscribe(val => {
          this.ngZone.run(() => {   
            if(this.IsShow==AppConstant.BatchList){
                for (let index = 0; index < this.skuConfigArray.length; index++) {
                  this.skuConfigArray[index].LotValue = val.skuLotModel[`skulot${index+1}`];
                }
            }else{
              this.skuCode = val.skucode;
              this.scanWmsSku(val.skucode);
            }
          });
        });
      } else {
        this.nativeService.showToast('请扫描箱码');
      }
    }
  }
  /**
   * 数据格式化
   */
  clearMode() {
    // this.WarehouserModel=null;//库房列表
    // this.warhouserItem = null;//选中库房
    this.skuCode = '';//物品编号
    this.skuName = '';//物品名称
    this.skuSpec = '';//物品规格
    this.lpnCode = '';//容器编码
    this.scanQtynum = '';//实际数量
    this.locCode = '';//库位编码
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
  expression() {
    // if (this.recList == 1) {
    //   this.recList = 0;
    // } else {
    //   this.recList = 1;
    // }
  }
  /**
  * sku回车事件
  */
  skuCode_KeyDown(event) {
    if (event && event.keyCode == 13) {
      if (Utils.isEmpty(this.skuCode)) {
        this.nativeService.showToast('物品编码不能为空');
        return;
      }
      this.scanWmsSku(this.skuCode);
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
    //this.skuConfigArray.push({});
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
      //this.randomCountSubmit();
      this.stockListPda();
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
      //this.randomCountSubmit();
      this.stockListPda();
    }
  }
  /**
   * 盘点提交库存验证
   */
  stockListPda() {
    if (!this.checkDataSubmit()) {
      return;
    }
    let params = {
      //  whId: this.warhouserItem.whId
      locCode: this.locCode,
      lpnCode: this.lpnCode,
      skuId: this.resultSkuItem.skuId,
    };
    this.appService.httpRequest(Api.StockCountPDA + Api.stockListPda, Method.Post, params, ContenType.Json, result => {
      this.stockList = result.data;
      if (result.data.length == 0) {
        this.stockItem = "新增批次号";
      } else {
        this.stockItem = result.data[0];
      }
      this.toggle();
      this.IsShow = this.BatchList;
      //选择批次
      if (this.IsShow == this.Batch) {
        this.modelNum = this.ScanNumberLength;

      } else {
        this.modelNum = this.countQty;
      }
    })
  }
  checkDataSubmit() {
    if (this.sysparms.paramValue == 0) {
      if (Utils.isEmpty(this.lpnCode)) {
        this.nativeService.showToast('容器为空,请扫描容器');
        return false;
      }
      if (Utils.isNotEmpty(this.stockItem))
        this.lpnCode = this.stockItem.lpnCode
    }
    if (Utils.isEmpty(this.locCode)) {
      this.nativeService.showToast('库位为空,请扫描库位');
      return false;
    }
    if (Utils.isEmpty(this.resultSkuItem)) {
      this.nativeService.showToast('物品信息没有扫描到,请扫描后在提交');
      return false;
    }
    if (this.resultSkuItem.isSn == 1) {//序列号物品
      if (Utils.isEmpty(this.serialNumberList)) {
        this.nativeService.showToast('序列号为空');
        return false;
      }
      this.countQty = this.ScanNumberLength;
      if (Utils.isEmpty(this.skuPackageDetailVOList)) {
        this.nativeService.showToast('当前物品选中的包装没有包装单位,请在PC绑定');
        return false;
      }
    } else {//批次号物品
      if (Utils.isEmpty(this.skuPackageDetailVOItem)) {
        this.nativeService.showToast('请选择包装');
        return false;
      }
      if (Utils.isEmpty(this.countQty)) {
        this.nativeService.showToast('数量为空,请输入数量');
        return false;
      }
    }
    if (this.IsShow == this.BatchList) {
      if (Utils.isEmpty(this.stockItem)) {
        this.nativeService.showToast('请选择批次号');
        return false;
      }
    }
    return true;
  }
  //盘点提交
  async randomCountSubmit() {
    let wspdId;
    if (this.resultSkuItem.isSn == 1) {//序列号物品
      let sku = this.skuPackageDetailVOList.filter(x => x.skuLevel == 1);
      if (Utils.isEmpty(sku)) {
        this.nativeService.showToast('当前物品没有基础计量单位的,请绑定');
        return false;
      }
      wspdId = sku[0].wspdId;//序列号获取包装
    } else {
      wspdId = this.skuPackageDetailVOItem.wspdId;//批次获取包装
    }
    let userInfo: LoginUserInfoModel = await this.storage.get('userInfo');
    //获取批次号
    let lotNumber = '';
    if (Utils.isNotEmpty(this.stockItem) && this.stockItem != '新增批次号') {
      lotNumber = this.stockItem.lotNumber;
    }

    if (Utils.isNotEmpty(this.stockItem)) {
      this.boxCode = this.stockItem.boxCode;
    }
    //批次号
    let skuLot = new SkuLot();
    this.skuConfigArray.forEach(element => {

    });
    for (let index = 0; index < this.skuConfigArray.length; index++) {
      skuLot['skuLot' + (index + 1)] = this.skuConfigArray[index].LotValue
    }
    //盘点提交
    let params = {
      //whId: this.warhouserItem.whId,
      lpnCode: this.lpnCode,
      locCode: this.locCode,
      skuId: this.resultSkuItem.skuId,
      lotNumber: lotNumber,
      countQty: this.countQty,
      wspdId: wspdId,
      userId: userInfo.tenant_id,
      boxCode: this.boxCode,
      serialNumberList: this.serialNumberList,
      ...skuLot
    }
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.StockCountPDA + Api.randomCountSubmit, Method.Post, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      if (this.IsShow == this.BatchList) {
        //是否进行清除和页面跳转
        this.nativeService.showToast('提交成功');
        this.clearMode();
      } else {
        // if (result.data.stockList.length > 1) {
        //   this.stockList = result.data.stockList;
        //   //选择批次
        //   if (this.IsShow == this.Batch) {
        //     this.modelNum = this.countQty;
        //   } else {
        //     this.modelNum = this.ScanNumberLength;
        //   }

        //   this.IsShow = this.BatchList;

        // } else {
        //   //是否进行清除和页面跳转
        //   this.nativeService.showToast('提交成功');
        //   this.clearMode();
        // }
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
    // if (this.IsShow == AppConstant.Warehouse) {
    //   this.navCtrl.pop();
    // } 
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
}
