import { Component, NgZone } from '@angular/core';
import {
  IonicPage,
  NavController,
  NavParams,
  PopoverController,
  ModalController,
  AlertController,
  Events
} from 'ionic-angular';
import { Utils } from '../../../services/Utils';
import { NativeService } from "../../../services/NativeService";
import { BarcodeService , BarcodeType} from '../../../services/BarCodeService';
import { ScanModel } from '../../../models/DataBase.Model';
import { AppService } from "../../../services/App.service";
import { AppConstant, Api, ContenType, Method, BaseCodeConstant } from '../../../utils/appConstant';
import { Keyboard } from '@ionic-native/keyboard';
import { Storage } from '@ionic/storage';
import { AppGlobal } from '../../../services/AppGlobal';


/**
 * Generated class for the MovesboxPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-movesbox',
  templateUrl: 'movesbox.html',
})
export class MovesboxPage {

  isShow: boolean = true;

  selectOptions: any;

  scanView: string; //界面扫描实体

  sourceLocCode: string;//原库位

  sourceLpnCode: string;//原容器

  scanModel: ScanModel;//扫描实体

  skuStockList: any[] = [];//移动列表实体

  skuStockItem: any = {};//当前移动实体

  public sysparms: any = [];//系统参数
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public popoverCtrl: PopoverController,
    public nativeService: NativeService,
    public barcodeService: BarcodeService,
    public appService: AppService,
    public modalCtrl: ModalController,
    public alertCtrl: AlertController,
    public keyboard: Keyboard,
    public storage: Storage,
    public events: Events,
    public ngZone:NgZone
  ) {
    this.selectOptions = {
      title: 'Pizza Toppings',
      subTitle: 'Select your toppings',
      mode: 'md'
    };
    this.storage.get('sysParam').then(data => {
      this.sysparms = data.filter(x => x.paramKey == 'system::lpnEnable')[0];
    });
   
  }
  ionViewWillEnter() {
    //事件注册（扫描结果接收）
    this.events.subscribe('barcode:scan', (barcode, time) => {
      let bt=this.barcodeService.GetBarcodeType(barcode);
      if (bt==BarcodeType.HuoWei) {
        this.ngZone.run(() => {
          this.sourceLocCode = barcode;
        });
      }
      if(bt==BarcodeType.BoxCode){
        this.ngZone.run(() => {
        //条码解析
        this.barcodeService.ScanSkuBarcode(barcode).subscribe(val => {
          if (Utils.isNotEmpty(val)) {
            this.scanModel = val;
            //查询物品
            this.scanWmsSku(val);
          }
        })
      });
      }
    });
  }
  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
  }
  presentPopover(myEvent) {
    let popover = this.popoverCtrl.create('PopoverPage');
    popover.onDidDismiss(data => {
      if (Utils.isNotEmpty(data) && Utils.isNotEmpty(this.skuStockList)) {
        console.log(this.skuStockList);
        if (data == 'allCheck') {
          //全选
          for (let index = 0; index < this.skuStockList.length; index++) {
            this.skuStockList[index].isCheck = true;
          }
        } else if (data == 'clearCheck') {
          //取消全选
          for (let index = 0; index < this.skuStockList.length; index++) {
            this.skuStockList[index].isCheck = false;
          }
        } else {
          //删除选中
          this.skuStockIsCheckDel();
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
    this.skuStockList = this.skuStockList.filter(x => x.isCheck === false);
  }
  removeItem(item) {
    this.skuStockList = this.skuStockList.filter(x => x != item);
  }
  exit() {
    this.navCtrl.pop();
  }
  ionViewDidLoad() {

  }
  /**
   * 折叠状态
   */
  onIsShowClick() {
    this.isShow = !this.isShow;
  }
  /**
   * 扫码回车事件
   * @param event
   */
  scanModelChange(event) {
    if (event && event.keyCode == 13) {
      if (Utils.isNotEmpty(this.scanView) && Utils.isNotEmpty(this.sourceLocCode)) {
        this.barcodeService.ScanSkuBarcode(this.scanView).subscribe(val => {
          if (Utils.isNotEmpty(val)) {
            this.scanModel = val;
            //查询物品
            this.scanWmsSku(val);
          }
        })
      } else {
        this.nativeService.showToast('请扫描箱码或库位');
      }
    }

  }
 
  /*物品扫描*/
  scanWmsSku(val: ScanModel) {
    if (Utils.isEmpty(val.skucode)) {
      this.nativeService.showToast('物品编码不能为空');
    }
    let params = {
      skuCode: val.skucode
    }
    //获取物品
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.api + Api.SkuList, Method.Post, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      //判断是否多个物品
      if (result.data.length > 1) {
        //选择物品
        this.openAsnRecordModal(result.data, val);
      } else if (Utils.isEmpty(result.data)) {
        this.nativeService.showToast('没有查询到物品');
      } else {
        this.getMoveStockModel(val, result.data[0].skuId);
      }
    });
  }

  /**
  * 查询库存信息
  */
  getMoveStockModel(model: ScanModel, skuId) {
    if (Utils.isEmpty(model.qty)) {
      this.nativeService.showToast('物品数量不能为空');
      return;
    }
    if (Utils.isEmpty(model.skucode)) {
      this.nativeService.showToast('物品编码不能为空');
      return;
    }
    let skuLotModel = this.barcodeService.convertLotsModel(model);
    // this.skuCode = model.skucode;
    // this.skuName = model.skuname;
    let param = {
      // locCode: this.locCode,
      // qty: model.qty,
      // skuCode: model.skucode,
      // whId: this.warhouserItem.whId,
      // skuLots: skuLotModel,
      // skuId: this.resultSkuItem.skuId
      skuId: skuId,
      sourceLocCode: this.sourceLocCode,
      sourceLpnCode: this.sourceLpnCode,
      wsuName: Utils.isEmpty(model.um)?'':model.um,
      skuCode: model.skucode,
      skuName: model.skuname,
      moveQty: model.qty,
      ...skuLotModel
    }
    this.appService.httpRequest(Api.stockInner + Api.getMoveStock, Method.Get, param, ContenType.Content, result => {
      this.nativeService.hideLoading();
      if(!result.success){
        this.nativeService.showToast(result.msg);
        return;
      }
      if (Utils.isNotEmpty(result.data)) {
        if(Utils.isEmpty(result.data.stockList))return;
        if(result.data.stockList.length>1){
          this.openStockSelectModal(result.data);
        }else{
          result.data['isCheck'] = false;
          result.data['isWarning'] = false;
          result.data['stockId'] = result.data.stockList[0].stockId;
          result.data['wspId'] = result.data.stockList[0].wspId;
          result.data['soBillId'] = result.data.stockList[0].soBillId;
          result.data['soBillNo'] = result.data.stockList[0].soBillNo;
          result.data['orderNo'] = result.data.stockList[0].orderNo;
          result.data['billDetailId'] = result.data.stockList[0].billDetailId;
          result.data['wellenId'] = result.data.stockList[0].wellenId;
          result.data['sourceLpnCode'] = this.sourceLpnCode;
          this.skuStockList.push(result.data);
          this.skuStockItem = result.data;
        }
      }
    });

  }
  /**
  *查看物品明细
  */
  openAsnRecordModal(skuList, val: ScanModel) {
    let myModal = this.modalCtrl.create('SkuListModal', {
      skuList: skuList,
    });
    myModal.onDidDismiss(data => {
      if (Utils.isNotEmpty(data)) {
        this.getMoveStockModel(val, data.skuId);
      }
    });
    myModal.present();
  }
  openStockSelectModal(data1) {
    let myModal = this.modalCtrl.create('StockSelectModal', {
      stockRecords: data1.stockList,
    });
    myModal.onDidDismiss(data => {
      if(Utils.isEmpty(data))return;
      data1['isCheck'] = false;
      data1['isWarning'] = false;
      data1['stockId'] = data.stockId;
      data1['wspId'] = data.wspId;
      data1['soBillId'] = data.soBillId;
      data1['soBillNo'] = data.soBillNo;
      data1['orderNo'] = data.orderNo;
      data1['billDetailId'] = data.billDetailId;
      data1['wellenId'] = data.wellenId;
      data1['sourceLpnCode'] = this.sourceLpnCode;
      this.skuStockList.push(data1);
      this.skuStockItem = data1;
    });
    myModal.present();
  }
  /**
   * 切换规格物品
   */
  specOnclick(skuStockItem) {
    console.log(skuStockItem);
    let alert = this.alertCtrl.create();
    alert.setTitle('选择包装规格');
    skuStockItem.stockList.forEach(element => {
      alert.addInput({
        type: 'radio',
        label: element.skuSpec,
        value: element,
        checked: false
      });
    });
    alert.addButton('返回');
    alert.addButton({
      text: '确定',
      handler: data => {
        console.log(data);
        for (let index = 0; index < skuStockItem.stockList.length; index++) {
          const element = skuStockItem.stockList[index];
          if (element === data) {
            let temp = skuStockItem.stockList[0];
            skuStockItem.stockList[0] = element;
            skuStockItem.stockList[index] = temp;
            //更新库存IDskuStockItem.stockList[0]
            skuStockItem.stockId = skuStockItem.stockList[0].stockId;
          }
        }
      }
    });
    alert.present();
  }
  /**
   * 转移至
   */
  btnOk() {
    let skuStockList = this.skuStockList.filter(x => x.isCheck == true);
    if (Utils.isEmpty(skuStockList)) {
      this.nativeService.showToast('转移列表中不存在数据');
      return;
    }
    //转移库存验证
    console.log(this.skuStockList);

    console.log(skuStockList);
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.stockInner + Api.verifyStockForMoveByBox, Method.Post, skuStockList, ContenType.Json, result => {
      this.nativeService.hideLoading();
      if (Utils.isNotEmpty(result.data)) {
        //渲染当前不足的库存
        for (let index = 0; index < result.data.length; index++) {
          const element = result.data[index];
          for (let j = 0; j < this.skuStockList.length; j++) {
            if (this.skuStockList[j].stockId == element.stockId) {
              this.skuStockList[j].isWarning = true;
            }
          }
        }
      } else {
        //进行页面跳转喽  
        //this.navCtrl.push('MovesboxInfoPage', { skuStockList: this.skuStockList });
        let myModal = this.modalCtrl.create('MovesboxInfoPage', {
          skuStockList: skuStockList,
          sourceLocCode: this.sourceLocCode,//原库位
          sourceLpnCode: this.sourceLpnCode//原容器
        });
        //回调
        myModal.onDidDismiss(data => {
          if (data) {
            //数据清空
            this.clearModel();
          }
        });
        myModal.present();
      }
    });
  }
  clearModel() {
    this.scanView = '';
    this.sourceLocCode = '';
    this.scanModel = new ScanModel();
    this.skuStockList = [];
    this.skuStockItem = { skuName: '', moveQty: '', wsuName: '' };
  }
}
