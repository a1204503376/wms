import { Component, NgZone, ViewChild, ElementRef } from '@angular/core';
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
import { LoginUserInfoModel } from '../../../models/SystemFramework.Model';
import { BarcodeService, BarcodeType } from '../../../services/BarCodeService';
import { ScanModel } from '../../../models/DataBase.Model';
import { AppService } from "../../../services/App.service";
import { AppConstant, Api, ContenType, Method, BaseCodeConstant } from '../../../utils/appConstant';
import { Keyboard } from '@ionic-native/keyboard';
import { Storage } from '@ionic/storage';
import { AppGlobal } from '../../../services/AppGlobal';

@IonicPage()
@Component({
  selector: 'page-randomcheck',
  templateUrl: 'randomcheck.html',
})
export class RandomCheckPage {

  isShow: boolean = true;
  public scanView: string; //界面扫描实体
  public sourceLocCode: string;//原库位
  public scanModel: ScanModel = new ScanModel();//扫描实体
  public scanModelRecords: any = [];//已扫描物品列表
  public scrollHeight: any;
  public currentTotal: any = 0;
  public lpnCode: string;//容器编码
  public sysparms: any = [];//系统参数
  public isShowCurrentTotal: boolean = true;
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
    public ngZone: NgZone
  ) {
    this.storage.get('sysParam').then(data => {
      this.sysparms = data.filter(x => x.paramKey == 'system::lpnEnable')[0];
    });
  }
  ionViewWillEnter() {
    //事件注册（扫描结果接收）
    this.events.subscribe('barcode:scan', (barcode, time) => {
      let bt = this.barcodeService.GetBarcodeType(barcode);
      if (bt == BarcodeType.HuoWei) {
        this.ngZone.run(() => {
          this.sourceLocCode = barcode;
        });
      }
      if (bt == BarcodeType.BoxCode) {
        this.ngZone.run(() => {
          //条码解析
          this.barcodeService.ScanSkuBarcode(barcode).subscribe(val => {
            if (Utils.isNotEmpty(val)) {
              if(this.sysparms.paramValue==0&&Utils.isEmpty(this.lpnCode)){
                this.nativeService.showToast("请先扫描托盘！");
                return;
              }
              this.getSkuBaseUm(val);
            }
          })
        });
      }else if (bt == BarcodeType.TuoPan) {
        this.ngZone.run(() => {
          this.lpnCode = barcode;
        });
      }
    });
  }

  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);

  }
  presentPopover(myEvent) {
    let popover = this.popoverCtrl.create('PopoverPage', { show_item_3: true, show_item_4: false, show_item_5: true });
    popover.onDidDismiss(data => {
      if (Utils.isNotEmpty(data) && Utils.isNotEmpty(this.scanModelRecords)) {
        console.log(this.scanModelRecords);
        if (data == 'allCheck') {
          //全选
          for (let index = 0; index < this.scanModelRecords.length; index++) {
            this.scanModelRecords[index].isCheck = true;
          }
        } else if (data == 'clearCheck') {
          //取消全选
          for (let index = 0; index < this.scanModelRecords.length; index++) {
            this.scanModelRecords[index].isCheck = false;
          }
        } else if (data == 'show') {
          for (let item of this.scanModelRecords) {
            item['show'] = true;
          }
        } else if (data == 'hide') {
          for (let item of this.scanModelRecords) {
            if (item.skucode != this.scanModel.skucode) {
              item['show'] = false;
            } else {
              item['show'] = true;
            }
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

  skuStockIsCheckDel() {
    this.scanModelRecords = this.scanModelRecords.filter(x => x.isCheck === false);
    this.execTotal();
  }
  removeItem(item) {
    this.scanModelRecords = this.scanModelRecords.filter(x => x != item);
    this.execTotal();
  }
  changeQty() {
    if (Utils.isEmpty(this.scanModel.qty)) {
      this.scanModel.qty = "0";
    }
    this.execTotal();
  }
  clearRecordsSelected() {
    for (let item1 of this.scanModelRecords) {
      item1['selected'] = false;
      if (item1.skucode != this.scanModel.skucode) {
        item1['show'] = false;
      } else {
        item1['show'] = true;
      }
    }
  }

  itemClick(item) {
    this.clearRecordsSelected();
    item['selected'] = true;
    this.scanModel = item;
  }
  exit() {
    this.navCtrl.pop();
  }
  ionViewDidLoad() {
    this.scrollStypeTrue();
  }
  /**
   * 折叠状态
   */
  onIsShowClick() {
    this.isShow = !this.isShow;
    if (this.isShow) {
      this.scrollStypeTrue();
    } else {
      this.scrollStypeFalse();
    }
  }

  /**
   * 扫码回车事件
   * @param event
   */
  scanModelChange(event) {
    if (event && event.keyCode == 13) {
      if (Utils.isNotEmpty(this.scanView)) {
        this.barcodeService.ScanSkuBarcode(this.scanView).subscribe(val => {
          if (Utils.isNotEmpty(val)) {
            // if(this.sysparms.paramValue==0&&Utils.isEmpty(this.lpnCode)){
            //   this.nativeService.showToast("请先扫描托盘！");
            //   return;
            // }
            this.getSkuBaseUm(val);
          }
        })
      }
    }

  }
  getSkuBaseUm(val){
    let params = {
      skuCode: val.skucode
    }
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.api + Api.getSkuBaseUm, Method.Post, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      if(result.success){
        val['um'] = result.data;
        this.addToList(val);
      }
    });
  }
  addToList(val: ScanModel) {
    this.scanModel = val;
    let skulots = [];
    for (let prop in val.skuLotModel) {
      if (prop.search('skulot') != -1 && val.skuLotModel[prop] != '') {
        skulots.push(val.skuLotModel[prop]);
      }
    }
    this.scanModel['skulots'] = skulots;
    this.scanModel['isCheck'] = false;
    this.scanModel['selected'] = true;
    this.scanModel['show'] = true;
    this.scanModel['lpncode'] = this.lpnCode;
    if (Utils.isEmpty(this.scanModelRecords)) {
      this.scanModelRecords.push(this.scanModel);
    } else {
      this.clearRecordsSelected();
      let isContains = [];
      if(this.sysparms.paramValue==0){
        isContains = ['skuname','skulots','selected','isCheck','qty','setLotModel','setLotValue'];
       }else{
        isContains = ['lpncode','skuname','skulots','selected','isCheck','qty','setLotModel','setLotValue'];
       }
      for (let item of this.scanModelRecords) {
        if (Utils.isObjectValueEqual1(this.scanModel, item, isContains)) {
          item.qty = Number.parseInt(item.qty) + Number.parseInt(this.scanModel.qty);
          item['selected'] = true;
          this.scanModel = item;
        } else {
          if (!Utils.contains(this.scanModelRecords, this.scanModel, isContains)) {
            this.scanModelRecords.push(this.scanModel);
            break;
          }
        }
      }
    }
    this.execTotal();
  }
  execTotal() {
    this.currentTotal = 0;
    for (let item1 of this.scanModelRecords) {
      if (item1.skucode == this.scanModel.skucode) {
        this.currentTotal += Number.parseInt(item1.qty);
      }
    }
  }
  scrollStypeTrue() {
    // setTimeout(() => {
    //   var header= (document.getElementsByClassName("header")[0] as HTMLElement).offsetHeight;
    //   var head_lable= (document.getElementsByClassName("head_lable")[0] as HTMLElement).offsetHeight;
    //   var list_item_serial= (document.getElementsByClassName("list_item_serial")[0] as HTMLElement).offsetHeight;
    //   var footer= (document.getElementsByClassName("footer")[0] as HTMLElement).offsetHeight;
    //   this.scrollHeight=Number(document.body.clientHeight)-header-head_lable-list_item_serial-footer-10;
    // }, 600);
    this.scrollHeight = Number(document.body.clientHeight) - 323;
  }
  scrollStypeFalse() {
    // setTimeout(() => {
    //   var header= (document.getElementsByClassName("header")[0] as HTMLElement).offsetHeight;
    //   var list_item_serial= (document.getElementsByClassName("list_item_serial")[0] as HTMLElement).offsetHeight;
    //   var footer= (document.getElementsByClassName("footer")[0] as HTMLElement).offsetHeight;
    //   this.scrollHeight=Number(document.body.clientHeight)-header-list_item_serial-footer;
    // }, 600);
    this.scrollHeight = Number(document.body.clientHeight) - 173;
  }
  updateItem(item) {

  }
  btnOk() {
    if (Utils.isEmpty(this.sourceLocCode)) {
      this.nativeService.showToast("库位不能为空！");
      return;
    }
    // if (this.sysparms.paramValue == 0) {
    //   if (Utils.isEmpty(this.lpnCode)) {
    //     this.nativeService.showToast('容器为空,请扫描容器');
    //     return;
    //   }
    // }
    if (Utils.isEmpty(this.scanModelRecords)) {
      this.nativeService.showToast("请扫描物品");
      return;
    }
    this.submitRandomCheck();
  }

  submitRandomCheck() {
    let items = [];
    for (let item of this.scanModelRecords) {
      let itemTemp = {
        qty: item.qty,
        skuCode: item.skucode,
        skuName: item.skuname,
        umName:item.um,
        lpnCode:item.lpncode
      };
      for (let prop in item.skuLotModel) {

        if (prop.search('skulot') != -1 && Utils.isNotEmpty(item.skuLotModel[prop])) {
          let index = prop.substring(prop.indexOf('t') + 1, prop.length);
          itemTemp[`skuLot${index}`] = item.skuLotModel[prop];
        }
      }
      items.push(itemTemp);
    }
    let params = {
      lpnCode: this.lpnCode,
      locCode: this.sourceLocCode,
      items: items
    }
    console.log(params);
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.StockCountPDA + Api.randomCheckSubmit, Method.Post, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      if (result.code == 200) {
        this.nativeService.showToast(result.msg);
        this.clearModel();
      } else {
        this.nativeService.showToast(result.msg);
      }
    });

  }



  clearModel() {
    this.lpnCode = '';//容器编码
    this.scanView = '';
    this.sourceLocCode = '';
    this.scanModel = new ScanModel();
    this.scanModel.qty = '';
    this.scanModel.skucode = '';
    this.scanModel.skuname = '';
    this.scanModel['lpncode']='';
    this.scanModelRecords = [];
  }

  scrollToTop() {
    setTimeout(() => {
      window.scrollTo(0, document.body.scrollTop + 1);
      document.body.scrollTop >= 1 && window.scrollTo(0, document.body.scrollTop - 1);
    }, 10)
  }
}
