import { Component, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, ModalController, Events } from 'ionic-angular';
import { AppService } from "../../../services/App.service";
import { Utils } from '../../../services/Utils';
import { NativeService } from "../../../services/NativeService";
import { Api, ContenType, Method } from '../../../utils/appConstant';
import { Storage } from '@ionic/storage';
import { BarcodeService, BarcodeType } from '../../../services/BarCodeService';
import { AppGlobal } from '../../../services/AppGlobal';
/**
 * Generated class for the moveSku page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-movesku',
  templateUrl: 'movesku.html',
})
export class moveSku {
  //界面标识
  public IsShow = 1;//显隐标识
  // public Iswarehouse: number = 0;//仓库显示
  public IsSku: number = 1;//批次物品显示
  public IsSkuMove: number = 2;//物品移动显示
  public IsSkuBathList: number = 3;//批次号显示
  public IsSkuSerial: number = 4;//序列号物品显示
  public IsSkuSerialInfo: number = 5;//序列号物品明细显示
  public NumberListFalag: number = 1; //扫描标识 1：已扫描 2:已收货
  public IsSn: string;//是否为序列号

  //物品界面实体
  public WarehouserModel: any;//库房列表
  // public warhouserItem: any;//选中库房
  public whName: string;//库房名称
  public sourceLpnCode: string = '';//原容器编码
  public sourceBoxCode: string = '';//原容器编码
  public sourceLocCode: string = '';//原库位编码
  public skuCode: string;//物品编码
  public resultSkuItem: any;//物品详情
  public moveQty: string;//填写数量

  public skuName: string;//物品名称
  public skuSpec: string;//物品规格
  public stockQty: number;//物品库存数量

  public stockQtyPermanent: number;//初始物品库存数量
  public packageDetails: any;//包装明细
  public defaultPackageDetail: any;//默认包装
  public defaultPackageDetailTemp: any;//临时存放计量单位
  public defaultPackageDetailPermanent: any;//默认不变计量单位(用于切换小数点丢失)
  public wsuName: string;//计量单位名称

  public skuStockLotModel: any;//物品库存信息
  public skuStockLotMoveVOs: any;//物品库存信息多批次
  public stockQtyName: string;//物品数量


  //序列号界面
  public serialNumberMode: string;//序列号
  public InNumberLength: number = 0;//已收序列号数量
  public ScanNumberLength: number = 0;//已扫描序列号数量
  public serialNumber: string[] = [];//序列号已扫描集合
  public Sn: any;//序列号已提交
  public isTilebool: boolean = true;
  //转移界面实体
  public targetLpnCode: string;//目标库位
  public targetLocCode: string;//目标容器
  public targetBoxCode: string;//目标箱号
  public sysparms: any = [];//系统参数
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public appService: AppService,
    public nativeService: NativeService,
    public modalCtrl: ModalController,
    private storage: Storage,
    public ngZone: NgZone,
    public events: Events,
    public barcodeService: BarcodeService
  ) {
    //获取库房列表 2020-3-31去掉
    // this.nativeService.showLoading('正在获取库房...');
    // this.appService.httpRequest(Api.api + Api.ApiPDA, Method.Post, '', ContenType.Form, result => {
    //     this.nativeService.hideLoading();
    //     if (Utils.isNotEmpty(result.data)) {
    //         this.WarehouserModel = result.data;
    //     }
    // });
    this.storage.get('sysParam').then(data => {
      this.sysparms = data.filter(x => x.paramKey == 'system::lpnEnable')[0];
    });
  }
  //更改库房状态
  toggle() {
    //this.whName = this.warhouserItem.whName;
  }
  ionViewDidLoad() {
    console.log('ionViewDidLoad moveSku');
  }
  clearModel() {
    this.IsShow = this.IsSku;
    this.wsuName = '';
    this.sourceLpnCode = '';//原容器编码
    this.sourceBoxCode = '';//原容器编码
    this.sourceLocCode = '';//原库位编码
    this.skuCode = '';//物品编码
    this.resultSkuItem = null;//物品详情
    this.moveQty = '';//填写数量
    this.skuName = '';//物品名称
    this.skuSpec = '';//物品规格
    this.stockQty = null;//物品库存数量
    this.packageDetails = null;//包装明细
    this.defaultPackageDetail = null;//默认包装
    this.skuStockLotModel = null;//物品库存信息
    this.targetLpnCode = '';//目标容器
    this.targetBoxCode = '';//目标容器
    this.targetLocCode = '';//目标容器
    this.serialNumber = [];
    this.stockQtyName = '';
  }
  ionViewWillEnter() {
    //事件注册（扫描结果接收）
    this.events.subscribe('barcode:scan', (barcode, time) => {
      let bt = this.barcodeService.GetBarcodeType(barcode);
      if (bt == BarcodeType.SKU) {
        this.ngZone.run(() => {
          this.skuCode = barcode;
          this.scanWmsSku();
        });
      }
      if (bt == BarcodeType.TuoPan) {
        this.ngZone.run(() => {
          if (this.IsShow == this.IsSku) {
            //原容器
            this.sourceLpnCode = barcode;
            this.getLocCode();
          } else {
            //目标容器
            this.targetLpnCode = barcode;
          }
        });
      }
      if (bt == BarcodeType.BoxCode) {
        this.ngZone.run(() => {
          if (this.IsShow == this.IsSku) {
            //原容器
            this.sourceBoxCode = barcode;
            this.getLocCode();
          } else {
            //目标容器
            this.targetBoxCode = barcode;
          }
        });
      }
      if (bt == BarcodeType.HuoWei) {
        this.ngZone.run(() => {
          if (this.IsShow == this.IsSku) {
            //原库位
            this.sourceLocCode = barcode;
          } else {
            //目标库位
            this.targetLocCode = barcode;
          }
        });
      }
    });
  }
  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
  }
  /**
  * 原容器回车事件
  */
  sourceLpnCode_KeyDown(event) {
    if (event && event.keyCode == 13) {
      this.getLocCode();
    }
  }
  /**
   * 根据容器获取库位
   */
  getLocCode() {
    if (Utils.isEmpty(this.sourceLpnCode)) {
      this.nativeService.showToast('容器编码不能为空');
      return;
    }
    let params = {
      sourceLpnCode: this.sourceLpnCode
    }
    //获取库位编码
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.StockMove + Api.getLocCode, Method.Get, params, '', result => {
      this.nativeService.hideLoading();
      if (Utils.isNotEmpty(result.data)) {
        //获取库位
        this.sourceLocCode = result.data.sourceLocCode;
      }
    });
  }
  /**
  * sku回车事件
  */
  skuCode_KeyDown(event) {
    if (event && event.keyCode == 13) {
      this.scanWmsSku();
    }
  }
  /**
  * 序列号回车事件
  */
  SkuSerial_KeyDown(event) {
    if (event && event.keyCode == 13) {
      if (Utils.isNotEmpty(this.serialNumberMode)) {
        let flag = true;
        this.serialNumber.forEach(element => {
          if (element.toUpperCase() == this.serialNumberMode.toUpperCase()) {
            flag = false;
          }
        });
        // if (Utils.isNotEmpty(this.Sn)) {
        //     this.Sn.forEach(element => {
        //         if (element.snDetailCode == this.serialNumberMode) {
        //             flag = false;
        //         }
        //     });
        // }
        let body = {
          lpnCode: this.sourceLpnCode,
          skuId: this.resultSkuItem.skuId,
          serialNumber: this.serialNumberMode
        }
        this.appService.httpRequest(Api.StockMove + Api.StockIsHasSerial, Method.Get, body, '', result => {
          if (flag) {
            this.serialNumber.push(this.serialNumberMode);
            this.serialNumberMode = '';
          }
          this.ScanNumberLength = this.serialNumber.length;
        });
      };
    }
  }
  isTitle(event) {
    this.isTilebool = event;
    if (event) {
      this.NumberListFalag = 1;
    } else {
      this.NumberListFalag = 2;
    }
  }
  /**
  * 查看序列号
  */
  numclick() {
    this.IsShow = this.IsSkuSerialInfo;
  }
  /*物品扫描*/
  scanWmsSku() {
    if (Utils.isEmpty(this.skuCode)) {
      this.nativeService.showToast('物品编码不能为空');
    }
    let params = {
      skuCode: this.skuCode
    }
    //获取物品
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.api + Api.SkuList, Method.Post, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      // this.clearModel();
      this.serialNumber = [];
      this.ScanNumberLength = 0;
      //判断是否多个物品
      console.log(result);
      if (result.data.length > 1) {
        //选择物品
        this.openAsnRecordModal(result.data);
      } else if (Utils.isEmpty(result.data)) {
        this.nativeService.showToast('没有查询到物品');
      } else {
        this.resultSkuItem = result.data[0];
        this.getSkuInfo();
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
        this.getSkuInfo();
      }
    });
    myModal.present();
  }
  /**
   * 获取物品库存接口
   */
  getSkuInfo() {
    let skuId = '';
    if (Utils.isNotEmpty(this.resultSkuItem)) {
      this.skuName = this.resultSkuItem.skuName;
      skuId = this.resultSkuItem.skuId;
    }
    if (Utils.isEmpty(skuId)) {
     this.nativeService.showToast('请扫描物品！');
      return;
    }
    if (Utils.isEmpty(this.sourceLocCode)) {
      this.nativeService.showToast('原库位不能为空！');
       return;
     }
    let params = {
      skuId: skuId,
      sourceBoxCode: this.sourceBoxCode,
      sourceLpnCode: this.sourceLpnCode,
      sourceLocCode: this.sourceLocCode
    }
    this.IsSn = this.resultSkuItem.isSn;
    //判断是否为序列号
    if (this.resultSkuItem.isSn == 1) {
      this.IsShow = this.IsSkuSerial;
    } else {
      this.IsShow = this.IsSku;
    }
    this.appService.httpRequest(Api.StockMove + Api.getSkuInfo, Method.Get, params, '', result => {
      let resultData1 = result.data;
      // this.skuName = resultData1.skuName;
      this.stockQtyName = resultData1.stockQtyName;
      this.skuSpec = resultData1.skuSpec;
      this.stockQty = Number(resultData1.stockQty);
      this.packageDetails = resultData1.packageDetails;
      this.defaultPackageDetail = resultData1.defaultPackageDetail;//默认包装单位
      this.defaultPackageDetailTemp = resultData1.defaultPackageDetail;//临时存放计量单位
      this.defaultPackageDetailPermanent = resultData1.defaultPackageDetail;//默认不变计量单位(用于切换小数点丢失)
      this.stockQtyPermanent = this.stockQty;
      //获取物品已扫描序列号
      //this.Sn=resultData1;
      //this.wsuName = this.defaultPackageDetail.wsuName;
    });
    //构造测试数据
    // let resultData1 = {
    //     skuName: '测试物品名称',
    //     skuSpec: '1-10-001',
    //     stockQty: '500',
    //     packageDetails: [
    //         { wspdId: '1', wsuName: '个', wsuCode: '001', convertQty: 1, skuLevel: 1 },
    //         { wspdId: '2', wsuName: '包', wsuCode: '000', convertQty: 10, skuLevel: 2 },
    //         { wspdId: '3', wsuName: '箱', wsuCode: '000', convertQty: 100, skuLevel: 3 }
    //     ],
    //     defaultPackageDetail: { wspdId: '1', wsuName: '个', wsuCode: '001', skuLevel: '1', convertQty: '10' }
    // }
    // this.skuName = resultData1.skuName;
    // this.skuSpec = resultData1.skuSpec;
    // this.stockQty = Number(resultData1.stockQty);
    // this.packageDetails = resultData1.packageDetails;
    // this.defaultPackageDetail = resultData1.defaultPackageDetail;//默认包装单位
    // this.defaultPackageDetailTemp = resultData1.defaultPackageDetail;//临时存放计量单位
    // this.defaultPackageDetailPermanent = resultData1.defaultPackageDetail;//默认不变计量单位(用于切换小数点丢失)
    // this.stockQtyPermanent = this.stockQty;
    // this.wsuName = this.defaultPackageDetail.wsuName;
  }
  /**
   * 切换包装
   */
  togglePage() {
    if (Utils.isNotEmpty(this.defaultPackageDetail.convertQty)) {
      this.skuSpec = '1-' + this.defaultPackageDetail.convertQty
    } else {
      this.skuSpec = '';
    }
    this.scanQtyNameChange();
  }
  /**
* 已接收单位
*/
  scanQtyNameChange() {
    if (this.defaultPackageDetail.wspdId == 0 || this.defaultPackageDetail == 0 || this.defaultPackageDetail.scanQtyName == 0) {
      return;
    }
    let result = "";
    let mainWsuName = "";
    let scanQtynum = this.stockQty;
    if (Utils.isNotEmpty(this.defaultPackageDetail)) {
      this.packageDetails = this.packageDetails.sort(((n1, n2) => n2.convertQty - n1.convertQty));
    }
    if (Utils.isNotEmpty(this.packageDetails)) {
      this.packageDetails.forEach(element => {
        if (element.skuLevel == this.defaultPackageDetail.skuLevel) {
          mainWsuName = element.wsuName;
        }
        if (element.skuLevel <= this.defaultPackageDetail.skuLevel) {
          let tmp = Math.floor(scanQtynum / element.convertQty);
          scanQtynum = scanQtynum - element.convertQty * tmp;
          if (tmp != 0) {
            result += tmp + element.wsuName;
          }
        }
      });
    }
    if (Utils.isEmpty(result)) {
      this.stockQtyName = 0 + mainWsuName;
    } else {
      this.stockQtyName = result;
    }
  }
  /**
   * 提交按钮
   */
  btnOk() {
    debugger
    if (this.IsShow == this.IsSku || this.IsShow == this.IsSkuSerial) {
      this.submitSku();
    } else if (this.IsShow == this.IsSkuMove) {
      this.submitMoveStockSku();
    } else {
      //批次提交
      this.submitLotNumberSku();
      // this.IsShow = this.IsSkuMove;
    }
  }
  /**
   * 批次提交验证数量
   */
  submitLotNumberSku() {
    if (Utils.isEmpty(this.sourceLpnCode)) {
      this.sourceLpnCode = '';
    }
    if (Utils.isEmpty(this.sourceLocCode)) {
      this.sourceLocCode = '';
    }
    let params = {
      //whId: this.warhouserItem.whId,
      skuId: this.resultSkuItem.skuId,
      sourceLpnCode: this.sourceLpnCode,
      sourceLocCode: this.sourceLocCode,
      wspdId: this.defaultPackageDetail.wspdId,
      moveQty: this.moveQty,
      lotNumber: this.skuStockLotModel.lotNumber
    }
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.StockMove + Api.submitLotNumberSku, Method.Post, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      if (result.data.stockQty > 0) {
        this.IsShow = this.IsSkuMove;
      }
    });
  }
  /**
   * 物品移动提交
   */
  submitMoveStockSku() {
    if (Utils.isEmpty(this.skuStockLotModel)) {
      this.nativeService.showToast('库存信息不存在');
      return;
    }
    if (Utils.isEmpty(this.targetLocCode)) {
      this.nativeService.showToast('请输入目标库位');
      return;
    }
    // if (Utils.isEmpty(this.targetLpnCode)) {
    //   this.nativeService.showToast('请输入目标容器');
    //   return;
    // }
    let params = {
      // whId: this.warhouserItem.whId,
      skuId: this.resultSkuItem.skuId,
      sourceLpnCode: this.sourceLpnCode,
      sourceLocCode: this.sourceLocCode,
      //wspdId: this.defaultPackageDetail.wspdId,
      //moveQty: this.moveQty,
      lotNumber: this.skuStockLotModel.lotNumber,
      targetLocCode: this.targetLocCode,
      targetLpnCode: this.targetLpnCode,
      targetBoxCode: this.targetBoxCode,
      //serialNumber: this.serialNumber
    }
    //判断序列号物品
    if (this.resultSkuItem.isSn == 1) {
      if (this.ScanNumberLength == 0) {
        this.nativeService.showToast('请扫描序列号');
        return;
      }
      params['moveQty'] = this.ScanNumberLength;//序列号长度
      // params['序列号'] = this.serialNumber;//序列号实体
      params['wspdId'] = this.defaultPackageDetail.wspdId;
      params['serialList'] = this.serialNumber;
    } else {
      if (Utils.isEmpty(this.moveQty)) {
        this.nativeService.showToast('请填写转移数量');
        return;
      }
      if (!Utils.IsPositiveInt(this.moveQty)) {
        this.nativeService.showToast('转移数量必须为正整数');
        return;
      }
      if (Utils.isEmpty(this.defaultPackageDetail)) {
        this.nativeService.showToast('请选择计量单位');
        return;
      }
      params['moveQty'] = Number.parseFloat(this.moveQty)*this.defaultPackageDetail.convertQty;
      params['wspdId'] = this.defaultPackageDetail.wspdId;
    }

    //物品转移提交
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.StockMove + Api.submitMoveStockSku, Method.Post, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      if (Utils.isEmpty(result.data)) {
        this.nativeService.showToast('提交成功');
        //清楚数据
        this.clearModel();
      }
    });
  }
  /**
   * 获取库存批次号
   */
  submitSku() {
    // if (Utils.isEmpty(this.sourceLpnCode)) {
    //   this.nativeService.showToast('原容器编码不能为空');
    //   return;
    // }
    if (Utils.isEmpty(this.sourceLocCode)) {
      this.nativeService.showToast('原库位编码不能为空');
      return;
    }
    if (Utils.isEmpty(this.resultSkuItem)) {
      this.nativeService.showToast('没有查询到物品信息');
      return;
    }
    let params = {
      skuId: this.resultSkuItem.skuId,
      sourceLpnCode: this.sourceLpnCode,
      sourceBoxCode: this.sourceBoxCode,
      sourceLocCode: this.sourceLocCode,
      //wspdId: this.defaultPackageDetail.wspdId,
      moveQty: Number.parseFloat(this.moveQty) * this.defaultPackageDetail.convertQty
    }
    
    //判断序列号物品
    if (this.resultSkuItem.isSn == 1) {
      if (this.ScanNumberLength == 0) {
        this.nativeService.showToast('请扫描序列号');
        return;
      }
      params['moveQty'] =  this.ScanNumberLength ;
      params['serialList'] = this.serialNumber;
      params['wspdId'] = this.defaultPackageDetail.wspdId;
      this.moveQty = this.ScanNumberLength.toString();

    } else {
      if (Utils.isEmpty(this.moveQty)) {
        this.nativeService.showToast('请填写转移数量');
        return;
      }
      if (!Utils.IsPositiveInt(this.moveQty)) {
        this.nativeService.showToast('转移数量必须为正整数');
        return;
      }
      if (Utils.isEmpty(this.defaultPackageDetail)) {
        this.nativeService.showToast('请选择计量单位');
        return;
      }
      params['moveQty'] = Number.parseFloat(this.moveQty)* this.defaultPackageDetail.convertQty;
      params['wspdId'] = this.defaultPackageDetail.wspdId;
    }
    this.nativeService.showLoading();
    //提交物品获取库存批次数量
    this.appService.httpRequest(Api.StockMove + Api.submitSku, Method.Post, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      let skuStockLotMoveVOs = result.data.skuStockLotMoveVOs;
      //判断是否为多个
      if (skuStockLotMoveVOs.length > 1) {
        //多个跳转到批次列表暂无
        this.skuStockLotMoveVOs = skuStockLotMoveVOs;
        //默认选中库存批次
        this.skuStockLotModel = this.skuStockLotMoveVOs[0];
        this.IsShow = this.IsSkuBathList;
      } else if (skuStockLotMoveVOs.length < 1) {
        this.nativeService.showToast('当前库房没有查询到该物品');
      } else {
        this.skuStockLotModel = skuStockLotMoveVOs[0];
        this.IsShow = this.IsSkuMove;
      }
    });
    // let skuStockLotMoveVOs = [
    //     {
    //         stockQty: '10', lotNumber: '05500555', ownerName: '徐吉岩',
    //         skuLotList: [{ skuLotIndex: '1', skuLotValue: '测试属性值1', skuLotLabel: '时间', skuLotEditType: 'yyyy' }, { skuLotIndex: '2', skuLotValue: '测试属性2', skuLotLabel: '供编', skuLotEditType: '' }],
    //         packageDetails: [{ wspdId: '1', wsuName: '个', wsuCode: '001' }, { wspdId: '2', wsuName: '包', wsuCode: '000' }],
    //         defaultPackageDetail: { wspdId: '1', wsuName: '个', wsuCode: '001', skuLevel: '1', convertQty: '10' }
    //     },
    //     {
    //         stockQty: '20', lotNumber: '055005558', ownerName: '徐吉岩2号',
    //         skuLotList: [{ skuLotIndex: '1', skuLotValue: '测试属性值1', skuLotLabel: '入库', skuLotEditType: '' }, { skuLotIndex: '2', skuLotValue: '测试属性2', skuLotLabel: '出库', skuLotEditType: '' }],
    //         packageDetails: [{ wspdId: '1', wsuName: '个', wsuCode: '001' }, { wspdId: '2', wsuName: '包', wsuCode: '000' }],
    //         defaultPackageDetail: { wspdId: '1', wsuName: '个', wsuCode: '001', skuLevel: '1', convertQty: '10' }
    //     }
    // ];
    //判断是否为多个
    // if (skuStockLotMoveVOs.length > 1) {
    //     //多个跳转到批次列表暂无
    //     this.skuStockLotMoveVOs = skuStockLotMoveVOs;
    //     //默认选中库存批次
    //     this.skuStockLotModel = this.skuStockLotMoveVOs[0];
    //     this.IsShow = this.IsSkuBathList;
    // } else if (skuStockLotMoveVOs.length < 1) {
    //     this.nativeService.showToast('当前库房没有查询到该物品');
    // } else {
    //     this.skuStockLotModel = skuStockLotMoveVOs[0];
    //     this.IsShow = this.IsSkuMove;
    // }
  }
  /**
   * 返回按钮
   */
  exit() {
    if (this.IsShow == this.IsSku) {//批次物品列表
      this.navCtrl.pop();
    } else if (this.IsShow == this.IsSkuSerial) {//序列号物品
      this.IsShow = this.IsSku;
    } else if (this.IsShow == this.IsSkuSerialInfo) {//序列号明细
      this.IsShow = this.IsSkuSerial;
    } else if (this.IsShow == this.IsSkuMove) {//移动库位界面
      this.IsShow = this.IsSku;//留个坑，未来加一个是否序列号的判断
      if (this.IsSn == '1') {
        this.IsShow = this.IsSkuSerial;
      } else {
        this.IsShow = this.IsSku;
      }
    } else if (this.IsShow == this.IsSkuBathList) {
      this.IsShow = this.IsSku;
    }
  }
  compareFn(e1, e2): boolean {
    return e1 && e2 ? e1.wspdId === e2.wspdId : e1 === e2;
  }
  /**
  * 删除扫描序列号
  * @param item 
  */
  removeItem(item) {
    this.serialNumber = this.serialNumber.filter(x => x != item);
    //更新长度
    this.ScanNumberLength = this.serialNumber.length;
  }
}
