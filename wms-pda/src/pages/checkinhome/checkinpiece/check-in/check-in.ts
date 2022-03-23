import { Component, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, Events } from 'ionic-angular';
import { AppService } from "../../../../services/App.service";
import { Utils } from '../../../../services/Utils';
import { NativeService } from "../../../../services/NativeService"
import { AppConstant, Api, ContenType, Method, BaseCodeConstant } from '../../../../utils/appConstant';
import { SkuLotValResultModel, SkuLot } from '../../../../models/SkuLotValResultModel';
import { Storage } from '@ionic/storage';
import { AppGlobal } from '../../../../services/AppGlobal';
import { BarcodeService, BarcodeType } from '../../../../services/BarCodeService';

/**
 * Generated class for the CheckInPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-check-in',
  templateUrl: 'check-in.html',
})
export class CheckInPage {
  public recList: number = 1;
  public result: any;//单据信息
  public asnBillNo: string;//单据编号
  public isSn: number = 0;//是否为序列号管理 1,序列号管理  0,,非序列号管理
  public asnDetails: any;//订单明细
  public isMaxLength: Boolean = false;
  public serialNumber: string[] = [];//序列号已扫描集合
  public Sn: any;//序列号集合
  public serialNumberMode: string;//序列号实体
  public InNumberLength: number = 0;//已收序列号数量
  public ScanNumberLength: number = 0;//已扫描序列号数量
  public isTilebool: boolean = true;
  public NumberListFalag: number = 1; //扫描标识 1：已扫描 2:已收货
  /*页面元素*/
  public planCountQty: string;//计划总数量
  public scanCountQty: string;//实际总数量
  public finish: string; //明细单完成数
  public count: string//明细单总数

  public planQtyName: string;//单据明细计划名称
  public scanQtyName: string;//单据明细已完成名称
  public scanView: string;//箱码页面实体
  public planQty: string;//单据明细计划
  public scanQty: string;//单据明细已完成
  public skuCode: string;//物品编号
  public skuName: string;//物品名称
  public skuSpec: string;//物品规格
  public detailStatus: string;//接受状态 10:未接收,20:已接收
  public sname: string;//供应商名称
  public skuPackage: any;//包装管理
  public skuPackageDetails: any;//包装管理集合
  public skuPackageDetailsItem: any = {};//包装管理选中
  public skuConfig: SkuLotValResultModel[] = [];//批属性明细
  public sku: any;//物品信息
  public lpnCode: string;//容器编码
  public scanQtynum: string;//实际数量
  public locCode: string = 'STAGE';//库位编码
  public lotArray: SkuLot = new SkuLot();
  public taskId: string;
  public boxCode: string;
  //批属性暂定不动态
  // public skuConfig1: SkuLotValResultModel;
  // public skuConfig2: SkuLotValResultModel;
  public sysparms: any = [];//系统参数
  public lotCount: number = 30;
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public appService: AppService,
    public nativeService: NativeService,
    private storage: Storage,
    private events: Events,
    public barcodeService: BarcodeService,
    private ngZone: NgZone
  ) {
    if (Utils.isNotEmpty(this.navParams.get('result'))) {//订单编号输入
      this.result = this.navParams.get('result');
      this.dataFilling();
    };
    if (Utils.isNotEmpty(this.navParams.get('taskId'))) {//订单编号输入
      this.taskId = this.navParams.get('taskId');
    };
    this.storage.get('sysParam').then(data => {
      // let lotCount = data.filter(x => x.paramKey == "account:lotCount")[0].paramValue;
      // if (lotCount.length > 0) {
      //   this.lotCount = parseInt(lotCount);
      // }
      this.sysparms = data.filter(x => x.paramKey == 'system::lpnEnable')[0];
    });
  }

  ionViewWillEnter() {
    //事件注册（扫描结果接收）
    this.events.subscribe('barcode:scan', (barcode, time) => {
      let bt = this.barcodeService.GetBarcodeType(barcode);
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
      }
      // else if(bt == BarcodeType.BoxCode) {
      //     this.ngZone.run(() => {
      //       this.scanWmsSku1(barcode);
      //     });
      // }
    });
  }

  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
  }

  dataFilling() {
    this.asnBillNo = this.result.asnBillNo;
    this.sname = this.result.sname;
    this.planCountQty = this.result.asnDetailMinVO.planCountQty;
    this.scanCountQty = this.result.asnDetailMinVO.scanCountQty;
    this.finish = this.result.asnDetailMinVO.finish;
    this.count = this.result.asnDetailMinVO.count;
  }

  /**
   * 通过任务获取订单详情
   */
  clearModel() {
    // this.skuConfig1 = null;
    this.lotArray = new SkuLot();
    this.skuConfig = null;
    this.recList = 1;
    this.isSn = 0;//是否为序列号管理 1,序列号管理  0,,非序列号管理
    this.isMaxLength = false;
    this.serialNumber = [];//序列号已扫描集合
    this.Sn = null;//序列号集合
    this.serialNumberMode = '';//序列号实体
    this.InNumberLength = 0;//已收序列号数量
    this.ScanNumberLength = 0;//已扫描序列号数量
    this.isTilebool = true;
    this.NumberListFalag = 1; //扫描标识 1：已扫描 2:已收货
    // public planCountQty: string;//单据计划
    // public scanCountQty: string;//单据已完成
    this.planQty = '';//单据明细计划
    this.scanQty = '';//单据明细已完成
    this.planQtyName = '';
    this.scanQtyName = '';

    this.skuCode = '';//物品编号
    this.skuName = '';//物品名称
    this.skuSpec = '';//物品规格
    this.detailStatus = '';//接受状态 10:未接收,20:已接收
    //this.sname = '';//供应商名称
    this.skuPackage = null;//包装管理
    this.skuPackageDetails = null;//包装管理集合
    this.skuPackageDetailsItem = {};//包装管理选中
    this.skuConfig = [];//批属性明细
    this.sku = null;//物品信息
    this.lpnCode = '';//容器编码
    this.scanQtynum = '';//实际数量
    this.locCode = 'STAGE';//库位编码
    this.asnDetails = null;
    this.boxCode = "";
  }

  setServerIp() {
    // alert("点你妹");
  }
  /**
     * 检测物品信息
     * @param scanView 
     */
  scanWmsSku1(scanView) {
    //箱码解析
    this.barcodeService.ScanSkuBarcode(scanView).subscribe(val => {
      if (Utils.isEmpty(val.skucode)) {
        this.nativeService.showToast('物品编码不能为空');
      }
      this.scanQtynum = val.qty;
      this.scanWmsSku(val.skucode);
    });
  }


  /*物品扫描*/
  scanWmsSku(skuCode) {
    let body = "asnBillNo=" + this.result.asnBillNo + "&skuCode=" + encodeURI(skuCode);
    this.nativeService.showLoading()
    this.appService.httpRequest(Api.Instock + Api.AsnHeaderGetAsnHeaderDetail, Method.Post, body, ContenType.Form, result => {
      this.nativeService.hideLoading();
      if (result.data.success) {
        let asnInfo = result.data;
        this.skuConfig = [];
        if (Utils.isNotEmpty(asnInfo.asnDetails)) {
          this.planQty = asnInfo.asnDetail.planCountQty;
          this.scanQty = asnInfo.asnDetail.scanCountQty;
          this.planQtyName = asnInfo.asnDetail.planQtyName;
          this.scanQtyName = asnInfo.asnDetail.scanQtyName;
          ;
          this.skuName = asnInfo.asnDetail.skuName;
          this.scanQty = asnInfo.asnDetail.scanCountQty;
          this.skuSpec = asnInfo.asnDetail.skuSpec;
          this.detailStatus = asnInfo.asnDetail.detailStatusName;
          this.asnDetails = asnInfo.asnDetails[0];
          //获取30个批属性的值
          for (let index = 1; index <= 30; index++) {
            let lotkey = 'skuLot' + index;
            let lotval = this.asnDetails[lotkey];
            this.lotArray[lotkey] = lotval;
          }
          if (Utils.isNotEmpty(asnInfo.skuConfig)) {
            this.getModel(asnInfo.skuConfig);
          }
          if (Utils.isNotEmpty(asnInfo.skuPackage)) {
            this.skuPackage = asnInfo.skuPackage;
            this.skuSpec = this.skuPackage.spec;
          }
          if (Utils.isNotEmpty(asnInfo.skuPackageDetails)) {
            this.skuPackageDetails = asnInfo.skuPackageDetails;
            let pageUm = this.skuPackageDetails.filter(x => x.wsuCode == this.asnDetails.umCode);
            if (Utils.isNotEmpty(this.skuPackageDetails.filter(x => x.wsuCode == this.asnDetails.umCode))) {
              this.skuPackageDetailsItem = pageUm[0];
            }
          }
          if (Utils.isNotEmpty(asnInfo.skuPackageDetails)) {
            this.sku = asnInfo.sku;
            if (this.sku.isSn == 1) {
              this.skuPackageDetailsItem = this.skuPackageDetails.filter(x => x.skuLevel == 1)[0];
              this.isSn = asnInfo.sku.isSn;
              //获取序列号
              let params = {
                asnBillId: this.asnDetails.asnBillId,
                snStatus: BaseCodeConstant.SN_STATUS_2,
                skuCode: skuCode
              }
              this.appService.httpRequest(Api.Instock + Api.snlistInfo, Method.Post, params, ContenType.Json, result => {
                this.nativeService.hideLoading();
                if (Utils.isNotEmpty(result.data)) {
                  this.Sn = result.data;
                  this.InNumberLength = this.Sn.length;
                } else {
                  //this.nativeService.showToast(result.data.msg);
                  this.InNumberLength = 0;
                }
              });
            }
          }
        }
        this.togglePage();
      } else {
        this.nativeService.showToast(result.data.msg);
      }
    });
  }
  scanModelChange(event) {
    if (event && event.keyCode == 13) {
      if (Utils.isNotEmpty(this.scanView)) {
        this.scanWmsSku1(this.scanView);
      } else {
        this.nativeService.showToast('请扫描箱码');
      }
    }
  }
  /**
   * 数据转换
   */
  getModel(skuConfig) {
    skuConfig.forEach(Config => {
      let SkuLotVal: SkuLotValResultModel = new SkuLotValResultModel();
      SkuLotVal.LotValue = this.asnDetails[AppConstant.skuLot + Config.skuLotIndex];
      SkuLotVal.skuLotIndex = Config.skuLotIndex;
      SkuLotVal.skuLotLabel = Config.skuLotLabel;
      SkuLotVal.skuLot = Config.skuLot;
      SkuLotVal.skuLotDisp = Config.skuLotDisp;
      SkuLotVal.skuLotMust = Config.skuLotMust;
      SkuLotVal.skuLotMix = Config.skuLotMix;
      if (Utils.isNotEmpty(Config.skuLotMixMask)) {
        SkuLotVal.skuLotMixMask = Config.skuLotMixMask.toUpperCase();
      }
      SkuLotVal.skuLen = Config.skuLen;
      if (Utils.isEmpty(Config.skuLen) || Config.skuLen == 0) {
        Config.skuLen = AppConstant.MaxLength;
      }
      this.isMaxLength = true;
      SkuLotVal.skuLotEditType = Config.skuLotEditType;
      if (Utils.Contains(Config.skuLotMixMask, 'yyyy') || Utils.Contains(Config.skuLotMixMask, 'YYYY')) {
        SkuLotVal.CtlLotEditVal = AppConstant.Date;
        SkuLotVal.CtlLotEditBoole = false;
        if (Utils.isNotEmpty(SkuLotVal.LotValue)) {
          SkuLotVal.LotValue = Utils.getDateBath(Config.skuLotMixMask, SkuLotVal.LotValue);
        }
      } else {
        SkuLotVal.CtlLotEditVal = AppConstant.Text;
        SkuLotVal.CtlLotEditBoole = true;
      }
      this.skuConfig.push(SkuLotVal);
    });
    console.log(this.skuConfig);
  }

  scrollToTop() {
    setTimeout(() => {
      window.scrollTo(0, document.body.scrollTop + 1);
      document.body.scrollTop >= 1 && window.scrollTo(0, document.body.scrollTop - 1);
    }, 10)
  }

  skuCode_KeyDown(event) {
    if (event && event.keyCode == 13) {
      if (Utils.isEmpty(this.skuCode)) {
        this.nativeService.showToast('物品编码不能为空');
        return;
      }
      this.scanWmsSku(this.skuCode);
    }
  }
  //  boxCode_KeyDown(event) {
  //     if (event && event.keyCode == 13) {
  //         if (Utils.isEmpty(this.boxCode)) {
  //             this.nativeService.showToast('箱号不能为空');
  //             return;
  //         }
  //         this.scanWmsSku(this.boxCode);
  //     }
  // }
  // lpnCode_KeyDown(event) {
  //     if (event && event.keyCode == 13) {
  //         if (Utils.isEmpty(this.lpnCode)) {
  //             this.nativeService.showToast('容器不能为空');
  //             return;
  //         }
  //         this.scanWmsSku(this.lpnCode);
  //     }
  // }
  /**
   * 序列号回车事件
   */
  serialNumber_KeyDown(event) {
    if (event && event.keyCode == 13) {
      if (Utils.isNotEmpty(this.serialNumberMode)) {
        let flag = true;
        this.serialNumber.forEach(element => {
          if (element.toUpperCase() == this.serialNumberMode.toUpperCase()) {
            flag = false;
          }
        });
        // if (Utils.isNotEmpty(this.Sn)) {
        //   this.Sn.forEach(element => {
        //     if (element.snDetailCode == this.serialNumberMode) {
        //       flag = false;
        //     }
        //   });
        // }
        if (this.serialNumber.length + this.InNumberLength >= parseInt(this.planQty)) {
          flag = false;
        }
        let body = {
          asnDetailId: this.asnDetails.asnDetailId,
          snDetailCode: this.serialNumberMode
        }
        this.appService.httpRequest(Api.Instock + Api.isHasSerial, Method.Get, body, '', result => {
          if (flag) {
            this.serialNumber.push(this.serialNumberMode);
            this.serialNumberMode = '';
          }
          this.ScanNumberLength = this.serialNumber.length;
        });
      }
      ;
    }
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

  isTitle(event) {
    this.isTilebool = event;
    if (event) {
      this.NumberListFalag = 1;
    } else {
      this.NumberListFalag = 2;
    }
  }

  expression() {
    if (this.recList == 1) {
      this.recList = 0;
    } else {
      this.recList = 1;
    }
  }

  detailed() {
    if (Utils.isEmpty(this.asnBillNo)) {
      this.nativeService.showToast('单据编号为空')
      return;
    }
    this.navCtrl.push('CheckinDetailedPage', { asnBillNo: this.asnBillNo });
  }

  /**
   * 查看序列号
   */
  numclick() {
    this.isSn = 3;
  }

  /**
   * 收货提交
   */
  btnOk() {
    let arrStr = '';
    if (Utils.isEmpty(this.asnDetails)) {
      this.nativeService.showToast('请先扫描物品信息');
      return;
    }
    if (this.isSn == 3) {
      this.isSn = 1;
    }
    if (this.isSn == 1) {
      if (Utils.isEmpty(this.serialNumber)) {
        this.nativeService.showToast('没有扫描序列号');
        return;
      }
      this.serialNumber.forEach(element => {
        arrStr += element + ',';
      });
      arrStr = arrStr.substring(0, arrStr.length - 1);
      this.scanQtynum = this.serialNumber.length.toString();
    }
    if (this.isSn == 0) {
      if (Utils.isEmpty(this.skuPackageDetailsItem)) {
        this.nativeService.showToast('请填写包装单位');
        return;
      }
      if (Utils.isEmpty(this.skuCode)) {
        this.nativeService.showToast('请扫描物品编码');
        return;
      }
      if (Utils.isEmpty(this.scanQtynum)) {
        this.nativeService.showToast('请填写数量');
        return;
      }
      if (!Utils.IsPositiveInt(this.scanQtynum)) {
        this.nativeService.showToast('数量必须为正整数');
        return;
      }
      if (this.skuCode != this.sku.skuCode) {
        this.nativeService.showToast('当前物品编码和扫描物品编码不一致');
        return;
      }
    }
    if (Utils.isEmpty(this.locCode)) {
      this.nativeService.showToast('请填写库位');
      return;
    }
    let params = {
      skuId: this.sku.skuId,
      skuCode: this.sku.skuCode,
      boxCode: this.boxCode,
      isSn: this.isSn,
      planCountQty: this.planQty,
      scanCountQty: this.scanQty,
      wspId: this.skuPackage.wspId,
      wspdId: this.skuPackageDetailsItem.wspdId,
      snDetailCode: arrStr,
      lpnCode: this.lpnCode,
      asnBillNo: this.asnBillNo,
      scanQty: this.scanQtynum,
      locCode: this.locCode,
      whId: this.result.whId,
      asnBillId: this.result.asnBillId,
      asnDetailId: this.asnDetails.asnDetailId,
      taskId: this.taskId
    };
    let falseInfo = true;
    for (let index = 0; index < this.skuConfig.length; index++) {
      if (Utils.isNotEmpty(this.skuConfig[index].skuLotMixMask)) {
        if (Utils.Contains(this.skuConfig[index].skuLotMixMask, 'yyyy') || Utils.Contains(this.skuConfig[index].skuLotMixMask, 'YYYY')) {
          if (Utils.isNotEmpty(this.skuConfig[index].LotValue)) {
            this.skuConfig[index].LotValue = Utils.postDateBath(this.skuConfig[index].skuLotMixMask, this.skuConfig[index].LotValue);
          }
        }
      }
      if (this.skuConfig[index].skuLotMust == '1' && Utils.isEmpty(this.skuConfig[index].LotValue)) {
        this.nativeService.showToast('批属性【' + this.skuConfig[index].skuLotLabel + '】:' + '为必填值');
        falseInfo = false;
        break;
      }
      this.lotArray['skuLot' + this.skuConfig[index].skuLotIndex] = this.skuConfig[index].LotValue;
    }
    for (let property in this.lotArray) {
      params[property] = this.lotArray[property];
    }
    if (!falseInfo) {
      return;
    }
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.Instock + Api.submitAsnHeader, Method.Post, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      this.nativeService.showToast('提交成功');
      this.clearModel();
      this.finish = result.data.asnDetail.finish;
      this.count = result.data.asnDetail.count;
      if (this.finish == this.count) {
        this.navCtrl.getPrevious().data.flag = true;
        this.navCtrl.pop();
      }
    });
  }

  compareFn(e1, e2): boolean {
    return e1 && e2 ? e1.wspdId === e2.wspdId : e1 === e2;
  }

  togglePage() {
    // if (Utils.isNotEmpty(this.skuPackageDetailsItem.convertQty)) {
    //     this.skuSpec = '1-' + this.skuPackageDetailsItem.convertQty
    // } else {
    //     this.skuSpec = '';
    // }
    this.scanQtyNameChange();
    this.planQtyNameChange();
  }

  /**
   * 已接收单位
   */
  scanQtyNameChange() {
    if (this.skuPackageDetailsItem.wspdId == 0 || this.skuPackageDetailsItem == 0 || this.skuPackageDetailsItem.scanQtyName == 0) {
      return;
    }
    let result = "";
    let mainWsuName = "";
    let scanQtynum = parseInt(this.scanQty);
    if (Utils.isNotEmpty(this.skuPackageDetails)) {
      this.skuPackageDetails = this.skuPackageDetails.sort(((n1, n2) => n2.convertQty - n1.convertQty));
    }
    if (Utils.isNotEmpty(this.skuPackageDetails)) {
      this.skuPackageDetails.forEach(element => {
        if (element.skuLevel == this.skuPackageDetailsItem.skuLevel) {
          mainWsuName = element.wsuName;
        }
        if (element.skuLevel <= this.skuPackageDetailsItem.skuLevel) {
          let tmp = Math.floor(scanQtynum / element.convertQty);
          scanQtynum = scanQtynum - element.convertQty * tmp;
          if (tmp != 0) {
            result += tmp + element.wsuName;
          }
        }
      });
    }
    if (Utils.isEmpty(result)) {
      this.scanQtyName = 0 + mainWsuName;
      //this.planQtyName = 0 + mainWsuName;
    } else {
      this.scanQtyName = result;
      //this.planQtyName = result;
    }
  }

  /**
   * 计划量
   */
  planQtyNameChange() {
    if (this.skuPackageDetailsItem.wspdId == 0 || this.skuPackageDetailsItem == 0 || this.skuPackageDetailsItem.planQtyName == 0) {
      return;
    }
    let result = "";
    let mainWsuName = "";
    let scanQtynum = parseInt(this.planQty);
    if (Utils.isNotEmpty(this.skuPackageDetails)) {
      this.skuPackageDetails = this.skuPackageDetails.sort(((n1, n2) => n2.convertQty - n1.convertQty));
    }
    if (Utils.isNotEmpty(this.skuPackageDetails)) {
      this.skuPackageDetails.forEach(element => {
        if (element.skuLevel == this.skuPackageDetailsItem.skuLevel) {
          mainWsuName = element.wsuName;
        }
        if (element.skuLevel <= this.skuPackageDetailsItem.skuLevel) {
          let tmp = Math.floor(scanQtynum / element.convertQty);
          scanQtynum = scanQtynum - element.convertQty * tmp;
          if (tmp != 0) {
            result += tmp + element.wsuName;
          }
        }
      });
    }
    if (Utils.isEmpty(result)) {
      this.planQtyName = 0 + mainWsuName;
      //this.planQtyName = 0 + mainWsuName;
    } else {
      this.planQtyName = result;
      //this.planQtyName = result;
    }
  }

  setNameLineClass() {
    return this.fontStypeSize(this.scanQtyName);
  }

  setNameLineScanClass() {
    return this.fontStypeSize(this.planQtyName);
  }

  fontStypeSize(obj) {
    if (Utils.isEmpty(obj)) {
      return '16px';
    }
    if (obj.length <= 8) {
      return '16px';
    } else {
      return '12px;'
    }
  }

  exit() {
    if (this.isSn === 3) {
      this.isSn = 1;
    } else if (this.isSn === 1) {
      this.clearModel();
      this.isSn = 0;
    } else {
      this.navCtrl.pop();
    }
  }
}
