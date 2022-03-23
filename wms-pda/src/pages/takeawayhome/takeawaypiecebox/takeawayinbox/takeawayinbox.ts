import { Component, ViewChild, NgZone, ElementRef } from '@angular/core';
import { IonicPage, NavController, NavParams, ModalController, Events,PopoverController } from 'ionic-angular';
import { Utils } from '../../../../services/Utils';
import { NativeService } from "../../../../services/NativeService";
import { AppService } from "../../../../services/App.service";
import { Api, ContenType, Method } from '../../../../utils/appConstant';
import { Storage } from '@ionic/storage';
import { BarcodeService, BarcodeType } from '../../../../services/BarCodeService';
import { ScanModel } from '../../../../models/DataBase.Model';
import { IfObservable } from 'rxjs/observable/IfObservable';
import { AppGlobal } from '../../../../services/AppGlobal';
import { textSpanIntersectsWithPosition } from 'typescript';
/**
 * Generated class for the TakeawayinboxPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-takeawayinbox',
  templateUrl: 'takeawayinbox.html',
})
export class TakeawayinboxPage {

  @ViewChild('labelTypecom') labelTypecom;

  // @ViewChild('ceshi') ceshi: ElementRef<HTMLElement>;;
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
  public orderNo: any;
  public sobillNo: any;
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
  public sourceLocCode1:string;
  public sourceLpnCode: string;//原容器
  public skuCodeModel: string;//物品编码
  public lotNumber: string;//批次号
  public pickQty: string;//拣货数量
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
  public totalPlanQty:number = 0;
  public totalScanQty:number = 0;
  public targetLocCode: string = 'PICK'; //落放位置

  public sysparms: any = [];//系统参数

  public scanView: string; //界面扫描实体

  public scanModel: ScanModel;
  public scanModelRecords: any = [];
  public um: string;//箱码扫描单位
  public transportDate:string;
  public cName:string;
  constructor(public navCtrl: NavController, public navParams: NavParams,
    public nativeService: NativeService,
    public appService: AppService,
    public modalCtrl: ModalController,
    public storage: Storage,
    public barcodeService: BarcodeService,
    private ngZone: NgZone,
    private events: Events,
    public popoverCtrl: PopoverController,
  ) {

    
    this.pickPlansModel = this.navParams.get('pickPlansModel');
    this.PickInfoResult = this.navParams.get('PickInfoResult');
    this.pickPlansList = this.navParams.get('pickPlansList');
    this.taskId = this.navParams.get('taskId');
    this.wellenNo = this.navParams.get('wellenNo');
    this.storage.get('sysParam').then(data => {
      this.sysparms = data.filter(x => x.paramKey == 'system::lpnEnable')[0];
    });
  }
  ionViewDidLoad() {
    this.labelTypecom.ComponentFlag = true;
    this.labelTypecom.locCodeFlag = false;
    this.HeadModel();
  }

  initModel() {
    this.labelTypecom.ComponentFlag = true;
    this.labelTypecom.locCodeFlag = false;
    this.pickPlansInModel();
    this.firstLoadCom();
  }
  clearModel() {
    this.targetLpnCode = '';
    this.skuCodeModel = '';
    this.lotNumber = '';
    this.pickQty = '';
    this.sourceLocCode = "";
    this.serialNumber = [];
    this.ScanNumberLength = 0;
    this.scanView = '';
    this.targetLocCode = 'PICK';
    this.scanModel = null;

  }
  clearModelLabel() {
    this.targetLocCode = "PICK";
    this.sourceLpnCode = ""; 
    this.sourceLocCode1 = "";
    this.targetLpnCode = "";
    this.labelTypecom.scanModel.skucode = "";
    this.labelTypecom.scanModel.skuname = "";
    this.labelTypecom.scanModel.qty = "";
    this.labelTypecom.scanModel.um = "";
  }
  /**
   * 选项卡数据
   */
  HeadModel() {
    //头部
    this.count = this.PickInfoResult.count;
    this.finish = this.PickInfoResult.finish;
    //物品选项卡
    this.sourceLocCode = this.pickPlansModel.sourceLocCode;
    this.skuName = this.pickPlansModel.skuName;
    this.skuCode = this.pickPlansModel.skuCode;
    this.orderNo = this.PickInfoResult.orderNo;
    this.sobillNo = this.PickInfoResult.sobillNo;
    this.cName = this.PickInfoResult.cName;
    this.transportDate = this.PickInfoResult.transportDate;
    this.skuSpec = this.pickPlansModel.skuSpec;
    this.skuLot = this.pickPlansModel.skuLot;
    this.lotNumberTitle = this.pickPlansModel.lotNumber;
    this.planCountQty = this.pickPlansModel.planCountQty;
    this.totalPlanQty = this.pickPlansModel.totalPlanQty;
    this.totalScanQty = this.pickPlansModel.totalScanQty;
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
    //获取包装单位为箱的包装
    if (Utils.isNotEmpty(this.scanModel)) {
      this.defaultpackageDetail = this.pickPlansModel.packageDetails.find(x => x.wsuName == this.scanModel.um);
    } else {
      this.defaultpackageDetail = this.pickPlansModel.defaultPackageDetail;
    }



    this.InserialNumber = this.pickPlansModel.serialList;
    if (Utils.isNotEmpty(this.pickPlansModel.serialList)) {
      this.InNumberLength = this.pickPlansModel.serialList.length;
    }
    //判断是否为序列号
    // if (this.pickPlansModel.isSn == '1') {
    //   this.IsShow = this.IsSkuSerial;
    // } else {
    //   this.IsShow = this.IsSku;
    // }
    this.IsShow = this.IsSku;
  }
  ionViewWillEnter() {
    this.labelTypecom.qtyDisabled =false;
    //事件注册（扫描结果接收）
    this.events.subscribe('barcode:scan', (barcode, time) => {
      let bt = this.barcodeService.GetBarcodeType(barcode);
      // if (bt == BarcodeType.BoxCode) {
      //   this.ngZone.run(() => {
      //     this.getAsnHeaderDetailForBox(barcode);
      //   });
      // }
      if (bt == BarcodeType.HuoWei) {
        this.ngZone.run(() => {
          this.sourceLocCode1 = barcode;
        });
      }
    });
  }
  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
  }
  // scanModelChange(event) {
  //   if (event && event.keyCode == 13) {
  //     if (Utils.isNotEmpty(this.scanView)) {
  //       this.getAsnHeaderDetailForBox(this.scanView);
  //     } else {
  //       this.nativeService.showToast('请扫描箱码');
  //     }
  //   }
  // }
  /**
   * 首次加载组件信息
   */
  firstLoadCom() {
    //组件赋值
    this.labelTypecom.scanModel.skucode = this.pickPlansModel.skuCode;
    this.labelTypecom.scanModel.skuname = this.pickPlansModel.skuName;
    // this.labelTypecom.scanModel.qty = this.planCountQty;
    this.labelTypecom.scanModel.um = this.defaultpackageDetail ? this.defaultpackageDetail.wsuName : "";
  }
  /**
   * 解析并处理箱码数据
   * @param model 
   */
  getAsnHeaderDetailForBox(model) {
    this.barcodeService.ScanSkuBarcode(model).subscribe(val => {
      this.scanModel = val;
      if (Utils.isNotEmpty(val)) {
        for (let index = 1; index <= 30; index++) {
          val.skuLotModel[`skuLotName${index}`] = this.pickPlansModel.skuLotValue[`skuLotLabel${index}`];
        }
        const pickPlansModel = this.pickPlansList.filter(x =>{
          if(x.skuCode!=val.skucode)return false;
          return true;
        });
        if (pickPlansModel.length <= 0) {
          this.nativeService.showToast('该拣货任务没有匹配到扫描物品明细!');
        } else {
          // this.scanModel.um = this.labelTypecom.scanModel.um;
          this.pickPlansModel = pickPlansModel[0];
          this.getTotalScanQtyBySku(val);
        }
      }
    })
  }
  /**
   * 切换物品列表
   */
  onClickItem() {
    this.IsShow = this.IsSkuList;
    this.pickPlansModelTemp = this.pickPlansModel;
    if(Utils.isNotEmpty(this.pickPlansList)){
      for(let item of this.pickPlansList){
        item['isCheck']=false;
      }
    }
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
        this.navCtrl.push('TakeawayindetailedPage', { pickPlans: result.data.pickPlans, finish: this.finish,
           count: this.count, wellenNo: this.wellenNo,orderNo:this.orderNo,sobillNo:this.sobillNo});
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
      if (Utils.isEmpty(this.skuCodeModel)) {
        this.nativeService.showToast('请输入或扫描物品编码');
        return;
      }
      // let params = {
      //   skuCode: this.skuCodeModel
      // }
      //获取物品
      // this.resultSkuItem=null;
      // this.nativeService.showLoading();
      // this.appService.httpRequest(Api.api + Api.SkuList, Method.Post, params, ContenType.Json, result => {
      //   this.nativeService.hideLoading();
      //   //判断是否多个物品
      //   console.log(result);
      //   if (result.data.length > 1) {
      //     //选择物品
      //     this.openAsnRecordModal(result.data);
      //   } else if (Utils.isEmpty(result.data)) {
      //     this.nativeService.showToast('没有查询到物品');
      //   } else {
      //     this.resultSkuItem = result.data[0];
      //     this.getSkuInfo();
      //   }
      // });
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
      this.labelTypecom.ComponentFlag = true;
      this.labelTypecom.locCodeFlag = false;
      this.IsShow = this.IsSku;
      this.HeadModel();
    } else if (this.IsShow == this.IsSkuSerialInfo) {
      this.submitPickInfo();
    } else {
      //拣货提交
      this.submitPickInfo();
    }
  }
  presentPopover(myEvent) {
    let popover = this.popoverCtrl.create('PopoverPage',{show_item_3:false,show_item_4:true});
    popover.onDidDismiss(data => {
      if (Utils.isNotEmpty(data) && Utils.isNotEmpty(this.pickPlansList)) {
        console.log(this.pickPlansList);
        if (data == 'allCheck') {
          //全选
          for (let index = 0; index < this.pickPlansList.length; index++) {
            this.pickPlansList[index].isCheck = true;
          }
        } else if (data == 'clearCheck') {
          //取消全选
          for (let index = 0; index < this.pickPlansList.length; index++) {
            this.pickPlansList[index].isCheck = false;
          }
        }else if(data == 'complated'){
          //批量完成拣货任务
          if(Utils.isNotEmpty(this.pickPlansList)){
           let item = this.pickPlansList.find((x)=>{return x.isCheck});
           if(Utils.isNotEmpty(item)){
            for(let item1 of this.pickPlansList){
              if(item1.isCheck){
                this.complatedClick1(item1);
              }
            }
           }else{
             this.nativeService.showToast('请选择要完成的拣货任务');
           }
          }else{
            this.nativeService.showToast('请选择要完成的拣货任务');
          }
        }
      }
    });
    popover.present({
      ev: myEvent
    });
  }
  /**
   * 删除选中
   */
  skuStockIsCheckDel() {
    this.pickPlansList = this.pickPlansList.filter(x => x.isCheck === false);
  }
  getTotalScanQtyBySku(val){
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.Outstock + Api.getTotalScanQtyBySku, Method.Post, 
      'wellenNo='+this.wellenNo+
      '&skuCode='+ this.pickPlansModel.skuCode
      , ContenType.Form, result => {
        if(result.success){
          this.totalScanQty = result.data.totalScanQty;
          this.totalPlanQty = result.data.totalPlanQty;
          this.pickPlansModel.totalPlanQty = this.totalPlanQty;
          this.pickPlansModel.totalScanQty = this.totalScanQty;
          val.um = result.data.baseUm;
          if(Utils.isEmpty(this.scanModelRecords)){
            this.scanModelRecords.push(val);
         }else{
           for(let item of this.scanModelRecords){
              if(Utils.isObjectValueEqual1(val,item,['skuname','qty','setLotModel','setLotValue'])){
                if(Number.isNaN(item.qty)||Utils.isEmpty(item.qty)){
                  item.qty = Number.parseInt(val.qty);
                }else{
                  item.qty=Number.parseInt(item.qty)+Number.parseInt(val.qty);
                }
                 val=item;
              }else{
                if(!Utils.contains(this.scanModelRecords,val,['skuname','qty','setLotModel','setLotValue'])){
                  this.scanModelRecords.push(val);
                  break;
                }
              }
           }
         }
          this.labelTypecom.changeRun(val);
          this.initModel();
          // this.labelTypecom.scanModel.qty = val.qty;
        }
    });
  }
  /**
   * 完成点击事件
   */
  complatedClick1(item){
    let lots = {};
    for (let prop in item.skuLot) {
      if(Utils.isEmpty(item.skuLot[prop]))continue;
      lots[prop] = item.skuLot[prop];
    }
    let params = {
      wellenNo: this.wellenNo,
      pickPlanId: item.pickPlanId,
      sourceLocCode: item.sourceLocCode,
      skuCode: item.skuCode,
      lotNumber: item.lotNumber,
      pickQty: Number.parseInt(item.planCountQty)-Number.parseInt(item.realCountQty),
      targetLocCode: this.targetLocCode,
      wspdId: item.packageDetails[0].wspdId,
      isSn: this.pickPlansModel.isSn,
      lots: lots
    }
    if (this.sysparms.paramValue == 0) {
      params['targetLpnCode'] = this.targetLpnCode;
      params['sourceLpnCode'] = item.sourceLpnCode;
    }
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.Outstock + Api.submitPickInfoByBox, Method.Post, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      this.PickInfoResult = result.data;
      this.pickPlansList = this.PickInfoResult.pickPlans;
      let model = this.pickPlansList.filter(x => x.skuCode == this.labelTypecom.scanModel.skucode && x.skuName == this.labelTypecom.scanModel.skuname);
      if (Utils.isNotEmpty(model)) {
        this.pickPlansModel = model[0];
      } else if (Utils.isNotEmpty(this.pickPlansList)) {
        this.pickPlansModel = this.pickPlansList[0];
      }else{
        this.navCtrl.pop();
      }
      this.count = this.PickInfoResult.count;
      this.finish = this.PickInfoResult.finish;
    })
  }
  /**
   * 拣货提交
   */
  submitPickInfo() {
    if (Utils.isEmpty(this.scanView)) {
      this.nativeService.showToast('箱号不能为空');
      return;
    }
    if (Utils.isEmpty(this.sourceLocCode1)) {
      this.nativeService.showToast('原库位不能为空');
      return;
    }
    if (Utils.isEmpty(this.lotNumberTitle)) {
      this.nativeService.showToast('批次号不能为空');
      return;
    }

    if (Utils.isEmpty(this.targetLocCode)) {
      this.nativeService.showToast('落放位置不能为空');
      return;
    }
    if (Utils.isEmpty(this.labelTypecom.scanModel.um)) {
      this.nativeService.showToast('没有包装单位');
      return;
    }
    if (Utils.isEmpty(this.wellenNo)) {
      this.nativeService.showToast('波次编码不能为空');
      return;
    }
    if (Utils.isEmpty(this.defaultpackageDetail)) {
      this.nativeService.showToast('当前订单没有匹配到为箱的包装单位');
      return;
    }
    let lots = {};
    for (let key in this.labelTypecom.scanModel.skuLotModel) {
      if (key.indexOf('skuLotName') == -1) {
        let str = key.substring(6,key.length);
        lots[`skuLot${str}`] = this.labelTypecom.scanModel.skuLotModel[key];
      }
    }
    let params = {
      wellenNo: this.wellenNo,
      pickPlanId: this.pickPlansModel.pickPlanId,
      sourceLocCode: this.sourceLocCode1,
      skuCode: this.labelTypecom.scanModel.skucode,
      boxCode:this.scanView,
      lotNumber: this.lotNumberTitle,
      pickQty: this.labelTypecom.scanModel.qty,
      targetLocCode: this.targetLocCode,
      // wsuName: this.labelTypecom.scanModel.um,
      wspdId: this.defaultpackageDetail.wspdId,
      isSn: this.pickPlansModel.isSn,
      lots: lots,
      taskId:this.taskId
    }
    if (this.sysparms.paramValue == 0) {
      params['targetLpnCode'] = this.targetLpnCode;
      params['sourceLpnCode'] = this.sourceLpnCode;
    }
    // if (this.pickPlansModel.isSn == '1') {
    //   if (this.ScanNumberLength == 0) {
    //     this.nativeService.showToast('请扫描序列号');
    //     return;
    //   }
    //   params['serialList'] = this.serialNumber;
    //   params['pickQty'] = this.serialNumber.length.toString();
    // } else {
    if (Utils.isEmpty(this.labelTypecom.scanModel.um)) {
      this.nativeService.showToast('数量不能为空');
      return;
    }
    // }
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.Outstock + Api.submitPickInfoByBox, Method.Post, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      //重置数据
      this.PickInfoResult = result.data;//数据源
      this.pickPlansList = this.PickInfoResult.pickPlans;//数据集合
      let model = this.pickPlansList.filter(x => x.skuCode == this.labelTypecom.scanModel.skucode && x.skuName == this.labelTypecom.scanModel.skuname);
      if (Utils.isNotEmpty(model)) {
        this.pickPlansModel = model[0];
      } else if (Utils.isNotEmpty(this.pickPlansList)) {
        this.pickPlansModel = this.pickPlansList[0];

      } else {
        this.navCtrl.pop();
      }
      this.HeadModel();
      this.clearModelLabel();
      this.nativeService.showToast('提交成功');

    })
  }
  /**
   * 返回按钮
   */
  exit() {
    if (this.IsShow == this.IsSkuList) {
      this.IsShow = this.IsSku;
      // this.initModel();
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