import { Component, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, ModalController, Events } from 'ionic-angular';
import { Utils } from '../../../../services/Utils';
import { NativeService } from "../../../../services/NativeService";
import { AppService } from "../../../../services/App.service";
import { Api, ContenType, Method } from '../../../../utils/appConstant';
import { Storage } from '@ionic/storage';
import { AppGlobal } from '../../../../services/AppGlobal';
import { BarcodeService, BarcodeType } from '../../../../services/BarCodeService';
/**
 * Generated class for the TakeawayinPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-takeawayin',
  templateUrl: 'takeawayin.html',
})
export class TakeawayinPage {
  public recList: number = 1;
  public IsShow: number = 0; //显隐标识
  public IsSku: number = 0;//物品显示
  public IsSkuList: number = 1;//物品列表显示
  public IsSkuSerial: number = 4;//序列号物品显示
  public IsSkuSerialInfo: number = 5;//序列号物品明细显示

  public PickInfoResult: any;//任务数据实体
  public pickPlansList: any;//任务物品集合
  public pickPlansModel: any;//当前选中任务
  public pickPlansModelTemp: any;//当前选中临时任务
  public taskId: string;//任务ID
  public wellenNo: string;//波次编码
  // public resultSkuItem: any;//选中的物品详情
  //页面头部元素
  public count: any;//任务总度总数量
  public finish: any;//任务已收数量
  //页面物品卡元素
  public skuName: any;//物品名称
  public skuCode: any;//SKU编号
  public skuSpec: any;//拣货规格
  public skuLot: any;//批属性
  public planCountQty: any;//物品总数量
  public realCountQty: any;//物品已收数量
  public planQtyName: any;//计划数量名
  public realQtyName: any;//实际数量名
  public lotNumberTitle: any;//实际数量名
  //页面物品详情元素
  public targetLpnCode: string;//拣货容器
  public sourceLocCode: string;//原库位
  public sourceLpnCode: string;//原容器
  public skuCodeModel: string;//物品编码
  public lotNumber: string;//批次号
  public pickQty: string;//拣货数量
  public boxCode: string;//拣货数量
  public packageDetails: any;//包装明细集合
  public defaultpackageDetail: any;//默认包装明细

  //序列号管理
  public serialNumberMode: string;//序列号
  public InNumberLength: number = 0;//已收序列号数量
  public ScanNumberLength: number = 0;//已扫描序列号数量
  public serialNumber: string[] = [];//序列号已扫描集合
  public InserialNumber: string[] = [];//序列号已提交
  public NumberListFalag: number = 1; //扫描标识 1：已扫描 2:已收货
  public isTilebool: boolean = true;

  public targetLocCode: string = 'PICK'; //落放位置
  public orderNo: any;
  public sobillNo: any;
  public sysparms: any = [];//系统参数
  constructor(public navCtrl: NavController, public navParams: NavParams,
    public nativeService: NativeService,
    public appService: AppService,
    public modalCtrl: ModalController,
    public storage: Storage,
    public barcodeService: BarcodeService,
    public events: Events,
    public ngZone: NgZone
  ) {
    this.pickPlansModel = this.navParams.get('pickPlansModel');
    this.PickInfoResult = this.navParams.get('PickInfoResult');
    this.pickPlansList = this.navParams.get('pickPlansList');
    this.wellenNo = this.navParams.get('wellenNo');
    this.taskId = this.navParams.get('taskId');
    this.pickPlansInModel();
    this.storage.get('sysParam').then(data => {
      this.sysparms = data.filter(x => x.paramKey == 'system::lpnEnable')[0];
    });
  }
  ionViewDidLoad() {
    this.HeadModel();
  }
  ionViewWillEnter() {
    if (this.navParams.get('flag') || false) {
      this.wellenNo = "";
    }
    //事件注册（扫描结果接收）
    this.events.subscribe('barcode:scan', (barcode, time) => {
      let bt = this.barcodeService.GetBarcodeType(barcode);
      if (bt == BarcodeType.TuoPan) {
        this.ngZone.run(() => {
          this.targetLpnCode = barcode;
        });
      }
      if (bt == BarcodeType.HuoWei) {
        this.ngZone.run(() => {
          this.targetLocCode = barcode;
        });
      }
      if (bt == BarcodeType.SKU) {
        this.ngZone.run(() => {
          this.skuCodeModel = barcode;
          this.getSkuInfo();
        });
      }
    });
  }
  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
  }
  clearModel() {
    this.targetLpnCode = '';
    this.skuCodeModel = '';
    this.boxCode = '';
    this.lotNumber = '';
    this.pickQty = '';
    this.serialNumber = [];
    this.ScanNumberLength = 0;
  }
  /**
 * 选项卡数据
 */
  HeadModel() {
    //头部
    this.count = this.PickInfoResult.count;
    this.finish = this.PickInfoResult.finish;
    this.orderNo = this.PickInfoResult.orderNo;
    this.sobillNo = this.PickInfoResult.sobillNo;
    //物品选项卡
    this.skuName = this.pickPlansModel.skuName;
    this.skuCode = this.pickPlansModel.skuCode;
    this.skuSpec = this.pickPlansModel.skuSpec;
    this.skuLot = this.pickPlansModel.skuLot;
    this.lotNumberTitle = this.pickPlansModel.lotNumber;
    this.lotNumber =  this.pickPlansModel.lotNumber;
    this.planCountQty = this.pickPlansModel.planCountQty;
    this.realCountQty = this.pickPlansModel.realCountQty;
    this.planQtyName = this.pickPlansModel.planQtyName;
    this.realQtyName = this.pickPlansModel.realQtyName;
  }

  pickPlansInModel() {
    this.HeadModel();
    //页面物品详情元素
    this.sourceLocCode = this.pickPlansModel.sourceLocCode;
    this.sourceLpnCode = this.pickPlansModel.sourceLpnCode;
    this.packageDetails = this.pickPlansModel.packageDetails;
    this.defaultpackageDetail = this.pickPlansModel.defaultPackageDetail;

    this.InserialNumber = this.pickPlansModel.serialList;
    if (Utils.isNotEmpty(this.pickPlansModel.serialList)) {
      this.InNumberLength = this.pickPlansModel.serialList.length;
    }
    //判断是否为序列号
    if (this.pickPlansModel.isSn == '1') {
      this.IsShow = this.IsSkuSerial;
    } else {
      this.IsShow = this.IsSku;
    }
  }
  /**
   * 切换物品列表
   */
  onClickItem() {
    this.IsShow = this.IsSkuList;
    this.pickPlansModelTemp = this.pickPlansModel;
    // this.clearModel();
  }
  /**
   * 拣货记录
   */
  detailed() {
  
    let params = {
      wellenNo: this.wellenNo
    }
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.Outstock + Api.getSopickLog, Method.Get, params, ContenType.Form, result => {
      this.nativeService.hideLoading();
      if (Utils.isNotEmpty(result.data.pickPlans)) {
        this.navCtrl.push('TakeawayindetailedPage', { pickPlans: result.data.pickPlans, finish: this.finish, count: this.count });
      } else {
        this.nativeService.showToast('没有查询到记录');
      }
    })
  }
  /**
   * 物品列表选中
   */
  headSelected(item) {
    this.pickPlansModelTemp = item;
  }
  togglePage() {
    if (Utils.isNotEmpty(this.defaultpackageDetail.convertQty)) {
      this.skuSpec = '1-' + this.defaultpackageDetail.convertQty
    } else {
      this.skuSpec = '';
    }
    this.planQtyPage();
    this.realQtyPage();
  }
  planQtyPage() {
    let result = "";
    let mainWsuName = "";
    let planQtynum = parseInt(this.planCountQty);
    this.packageDetails = this.packageDetails.sort(((n1, n2) => n2.convertQty - n1.convertQty));
    this.packageDetails.forEach(element => {
      if (element.skuLevel == this.defaultpackageDetail.skuLevel) {
        mainWsuName = element.wsuName;
      }
      if (element.skuLevel <= this.defaultpackageDetail.skuLevel) {
        let tmp = Math.floor(planQtynum / element.convertQty);
        planQtynum = planQtynum - element.convertQty * tmp;
        if (tmp != 0) {
          result += tmp + element.wsuName;
        }
      }
    });
    if (Utils.isEmpty(result)) {
      this.planQtyName = 0 + mainWsuName;
    } else {
      this.planQtyName = result;
    }
  }
  realQtyPage() {
    let result = "";
    let mainWsuName = "";
    let planQtynum = parseInt(this.realCountQty);
    this.packageDetails = this.packageDetails.sort(((n1, n2) => n2.convertQty - n1.convertQty));
    this.packageDetails.forEach(element => {
      if (element.skuLevel == this.defaultpackageDetail.skuLevel) {
        mainWsuName = element.wsuName;
      }
      if (element.skuLevel <= this.defaultpackageDetail.skuLevel) {
        let tmp = Math.floor(planQtynum / element.convertQty);
        planQtynum = planQtynum - element.convertQty * tmp;
        if (tmp != 0) {
          result += tmp + element.wsuName;
        }
      }
    });
    if (Utils.isEmpty(result)) {
      this.realQtyName = 0 + mainWsuName;
    } else {
      this.realQtyName = result;
    }
  }
  /**
  * 物品回车事件
  */
  skuCode_KeyDown(event) {
    if (event && event.keyCode == 13) {
      debugger
      if (Utils.isEmpty(this.skuCodeModel)) {
        this.nativeService.showToast('请输入或扫描物品编码');
        return;
      }
      this.getSkuInfo();
    }

  }
  getSkuInfo() {
    //通过编号查询物品
    let model = this.pickPlansList.filter(x => x.skuCode == this.skuCodeModel && x.sourceLocCode == this.sourceLocCode && x.sourceLpnCode == this.sourceLpnCode);
    if (Utils.isEmpty(model)) {
      //this.nativeService.showToast('当前任务下没找到该物品');
      return;
    }
    this.pickPlansModel = model[0];
    this.pickPlansInModel();
  }
  /**
 *查看物品明细
*/
  // openAsnRecordModal(skuList) {
  //   let myModal = this.modalCtrl.create('SkuListModal', {
  //     skuList: skuList,
  //   });
  //   myModal.onDidDismiss(data => {
  //     if (Utils.isNotEmpty(data)) {
  //       this.resultSkuItem = data;
  //       this.getSkuInfo();
  //     }
  //   });
  //   myModal.present();
  // }
  /**
   * 提交按钮
   */
  btnOk() {
    if (this.IsShow == this.IsSkuList) {
      this.pickPlansModel = this.pickPlansModelTemp;
      this.pickPlansInModel();
    } else if (this.IsShow == this.IsSkuSerialInfo) {
      this.submitPickInfo();
    } else {
      //拣货提交
      this.submitPickInfo();
    }
  }
  /**
   * 拣货提交
   */
  submitPickInfo() {
    if (Utils.isEmpty(this.sourceLocCode)) {
      this.nativeService.showToast('原库位不能为空');
      return;
    }
    if (Utils.isEmpty(this.lotNumber)) {
      this.nativeService.showToast('批次号不能为空');
      return;
    }
    if (Utils.isEmpty(this.targetLocCode)) {
      this.nativeService.showToast('落放位置不能为空');
      return;
    }
    if (Utils.isEmpty(this.defaultpackageDetail)) {
      this.nativeService.showToast('没有包装单位');
      return;
    }
    if (Utils.isEmpty(this.wellenNo)) {
      this.nativeService.showToast('波次编码不能为空');
      return;
    }
    let params = {
      wellenNo: this.wellenNo,
      pickPlanId: this.pickPlansModel.pickPlanId,
      targetLpnCode: this.targetLpnCode,
      sourceLpnCode: this.sourceLpnCode,
      sourceLocCode: this.sourceLocCode,
      boxCode: this.boxCode,
      skuCode: this.skuCode,
      lotNumber: this.lotNumber,
      pickQty: this.pickQty,
      targetLocCode: this.targetLocCode,
      wspdId: this.defaultpackageDetail.wspdId,
      isSn: this.pickPlansModel.isSn,
      taskId:this.taskId
    }
    if (this.pickPlansModel.isSn == '1') {
      if (this.ScanNumberLength == 0) {
        this.nativeService.showToast('请扫描序列号');
        return;
      }
      params['serialList'] = this.serialNumber;
      params['pickQty'] = this.serialNumber.length.toString();
    } else {
      if (this.skuCode != this.skuCodeModel) {
        this.nativeService.showToast('当前扫描物品编码与选中物品编码不一致');
        return;
      }
      if (Utils.isEmpty(this.pickQty)) {
        this.nativeService.showToast('数量不能为空');
        return;
      }
      if (!Utils.IsPositiveInt(this.pickQty)) {
        this.nativeService.showToast('数量必须为正整数');
        return;
      }
    }
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.Outstock + Api.submitPickInfo, Method.Post, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      //重置数据
      this.PickInfoResult = result.data;//数据源
      this.pickPlansList = this.PickInfoResult.pickPlans;//数据集合
      let model = this.pickPlansList.filter(x => x.skuCode == this.skuCodeModel && x.sourceLocCode == this.skuCodeModel && x.sourceLocCode == this.sourceLpnCode);
      // let model = this.pickPlansList.filter(x => x.skuCode == this.skuCode);//当前操作数据
      if (Utils.isNotEmpty(model)) {
        this.pickPlansModel = model[0];
      } else if (Utils.isNotEmpty(this.pickPlansList)) {
        this.pickPlansModel = this.pickPlansList[0];
      } else {
        this.navCtrl.pop();
      }
      this.navCtrl.getPrevious().data.flag = true;
      this.nativeService.showToast('提交成功');
      this.clearModel();
      this.HeadModel();
    })
  }
  /**
   * 返回按钮
   */
  exit() {
    if (this.IsShow == this.IsSkuList) {
      this.IsShow = this.IsSku;
    } else if (this.IsShow == this.IsSku) {
      this.navCtrl.pop();
    } else if (this.IsShow == this.IsSkuSerial) {//序列号物品
      this.lotNumber = '';
      this.targetLpnCode = '';
      this.IsShow = this.IsSku;
    } else if (this.IsShow == this.IsSkuSerialInfo) {//序列号明细
      this.IsShow = this.IsSkuSerial;
    }
  }
  compareFn(e1, e2): boolean {
    return e1 && e2 ? e1.wspdId === e2.wspdId : e1 === e2;
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
        let body = {
          skuId: this.pickPlansModel.skuId,
          serialNumber: this.serialNumberMode
        }
        body["lpnCode"] = this.sourceLpnCode
        this.appService.httpRequest(Api.Outstock + Api.OutIsHasSerial, Method.Get, body, '', result => {
          if (flag) {
            this.serialNumber.push(this.serialNumberMode);
            this.serialNumberMode = '';
          }
          this.ScanNumberLength = this.serialNumber.length;
        });
      };
    }
  }
  setNameLineClass() {
    return this.fontStypeSize(this.realQtyName);
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
  /**
  * 查看序列号
  */
  numclick() {
    this.IsShow = this.IsSkuSerialInfo;
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
