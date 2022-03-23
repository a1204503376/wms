import { Component, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, ModalController, Events, AlertController, PopoverController } from 'ionic-angular';
import { Utils } from '../../../services/Utils';
import { SkuLotModel } from '../../../models/SkuLotModel'
import { AppConstant, Api, ContenType, Method, BaseCodeConstant } from '../../../utils/appConstant';
import { NativeService } from "../../../services/NativeService"
import { AppService } from "../../../services/App.service";
import { BarcodeService } from '../../../services/BarCodeService';
import { ScanModel } from '../../../models/DataBase.Model';
import { AppGlobal } from '../../../services/AppGlobal';
/**
 * Generated class for the CollageTaskPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-merge-tray',
  templateUrl: 'merge-tray.html',
})
export class MergeTrayPage {
  scanView: string;//二维码内容

  scanModel: ScanModel = new ScanModel();//扫描实体

  scanModelList: ScanModel[] = [];//扫描箱码实体集合

  resultSkuItem: any;//当前扫描物品

  resultList: any;//打包任务详情

  isShow: boolean = true;
  isNext: boolean = true;
  lpnCode: string;//卡板号

  length: string;//卡板长

  width: string;//卡板宽

  height: string;//卡板高

  weight: string;//重量

  remaker: string;//备注
  isUpdate: boolean;
  taskId: string;//任务ID
  public currentTotal: any = 0;
  public scrollHeight: any;
  public isShowCurrentTotal: boolean = true;
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public nativeService: NativeService,
    public appService: AppService,
    public barcodeService: BarcodeService,
    public modalCtrl: ModalController,
    private ngZone: NgZone,
    public events: Events,
    public popoverCtrl: PopoverController,
    public alertControl: AlertController
  ) {

  }
  presentPopover(myEvent) {
    let popover = this.popoverCtrl.create('PopoverPage', { show_item_3: true, show_item_4: false, show_item_5: true });
    popover.onDidDismiss(data => {
      if (Utils.isNotEmpty(data) && Utils.isNotEmpty(this.scanModelList)) {
        console.log(this.scanModelList);
        if (data == 'allCheck') {
          //全选
          for (let index = 0; index < this.scanModelList.length; index++) {
            this.scanModelList[index]['isCheck'] = true;
          }
        } else if (data == 'clearCheck') {
          //取消全选
          for (let index = 0; index < this.scanModelList.length; index++) {
            this.scanModelList[index]['isCheck'] = false;
          }
        } else if (data == 'show') {
          for (let item of this.scanModelList) {
            item['show'] = true;
          }
        } else if (data == 'hide') {
          for (let item of this.scanModelList) {
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
    this.scanModelList = this.scanModelList.filter(x => x['isCheck'] === false);
    this.execTotal();
  }
  removeItem(item) {
    this.scanModelList = this.scanModelList.filter(x => x != item);
    this.execTotal();
  }
  changeQty() {
    if (Utils.isEmpty(this.scanModel.qty)) {
      this.scanModel.qty = "0";
    }
    this.execTotal();
  }
  clearRecordsSelected() {
    for (let item1 of this.scanModelList) {
      item1['selected'] = false;
      if (item1.skucode != this.scanModel.skucode) {
        item1['show'] = false;
      } else {
        item1['show'] = true;
      }
    }
  }
  execTotal() {
    this.currentTotal = 0;
    for (let item1 of this.scanModelList) {
      if (item1.skucode == this.scanModel.skucode) {
        this.currentTotal += Number.parseInt(item1.qty);
      }
    }
  }
  itemClick(item) {
    this.clearRecordsSelected();
    item['selected'] = true;
    this.scanModel = item;
  }

  ionViewWillEnter() {
    //事件注册（扫描结果接收）
    if (AppGlobal.scanFlag) {
      AppGlobal.scanFlag = false;
      this.events.subscribe('barcode:scan', (barcode, time) => {
        if (!this.isShow) {
          this.ngZone.run(() => {
            this.lpnCode = barcode;
          });
        } else {
          this.getStockInfoPutawayForBox(barcode);
        }
      });
    }
  }
  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
  }
  /**
   * 二维码数据解析
   *
   */
  scanModelChange(event) {
    if (event && event.keyCode == 13) {
      if (Utils.isEmpty(this.scanView)) {
        this.nativeService.showToast('扫描数据不能为空');
        return;
      }
      this.getStockInfoPutawayForBox(this.scanView);
    }
  }
  /**
  * 箱码解析
  */
  getStockInfoPutawayForBox(scanView) {
    this.barcodeService.ScanSkuBarcode(scanView).subscribe(val => {
      if (Utils.isNotEmpty(val)) {
        this.scanModel = val;
        //查询物品
        this.scanWmsSku(val);
      } else {
        AppGlobal.scanFlag = true;
      }
    })
  }

  /*物品扫描*/
  scanWmsSku(val: ScanModel) {
    let temp = this.scanModelList.filter(x => Utils.isObjectValueEqual1(x, val,
      ['address', 'lpnCode', 'soBillId', 'show', 'skuId', 'skuName', 'cCode', 'cName', 'skuname', 'skulots', 'selected', 'isCheck', 'qty', 'setLotModel', 'setLotValue']));
    let arr = temp.map(x => parseInt(x.qty));
    let qty = 0;
    if (arr.length > 0) {
      qty = arr.reduce((acc, val) => acc + val);
    }
    let params = {
      skuCode: val.skucode
    }
    if (qty > 0) {
      params['qty'] = qty + parseInt(val.qty);
      params['ccode'] = temp[0]['cCode'];
    } else {
      params['qty'] = parseInt(val.qty);
    }
    for (let item in val.skuLotModel) {
      if (item.includes('skulot') && Utils.isNotEmpty(val.skuLotModel[item])) {
        let index = item.substring('skulot'.length, item.length);
        params[`skuLot${index}`] = val.skuLotModel[item];
      }
    }
    //获取物品
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.api + Api.querySku, Method.Post, params, ContenType.Json, result => {
      this.nativeService.hideLoading();
      console.log(result);
      AppGlobal.scanFlag = true;
      //判断是否多个物品
      if (result.data.length > 1) {
        //选择物品
        if (Utils.isEmpty(this.scanModelList)) {

          // for (var i = 0; i < result.data.length; i++) {
          //   // 删除掉所有为2的元素
          //   if (result.data[i]['address'].indexOf(',') != -1) {
          //     let aa = result.data[i]['address'].split(',');
          //     let arr = [];
          //     for (let a of aa) {
          //       let obj = {};
          //       Object.assign(obj, result.data[i]);
          //       obj['address'] = a;
          //       arr.push(obj);
          //     }
          //     result.data.splice(i, 1);
          //     result.data.push.apply(result.data, arr);
          //   }
          // }
          this.openAsnRecordModal(result.data, val);
        } else {
          var temp = result.data.filter(x => x.ccode == this.scanModelList[0]['cCode']);
          if (Utils.isNotEmpty(temp)) {
            // if (temp[0]['address'].indexOf(',') != -1) {
            if (temp.length > 1) {
              // let aa = temp[0]['address'].split(',');
              // let arr = [];
              // for (let a of aa) {
              //   let obj = {};
              //   Object.assign(obj, temp[0]);
              //   obj['address'] = a;
              //   arr.push(obj);
              // }
              this.openAsnRecordModal(temp, val);
            } else {
              this.resultSkuItem = temp[0];
              this.fillData(temp[0], val);
            }
          } else {
            this.openAsnRecordModal(result.data, val);
          }
        }
      } else if (Utils.isEmpty(result.data)) {
        this.nativeService.showToast('出库暂存区中未找到该物料或扫描数量超出拣货数量，请进行核对！');
      } else {
        this.resultSkuItem = result.data[0];
        if (Utils.isEmpty(this.scanModelList)) {
          this.fillData(result.data[0], val);
        } else {
          if (this.scanModelList[0]['cCode'] == this.resultSkuItem.ccode) {
            this.fillData(result.data[0], val);
          } else {
            this.nativeService.showToast('请扫描同一客户的物料！');
          }
        }
      }
    });
  }

  fillData(item1, val) {
    console.log(item1);
    if (Utils.isNotEmpty(this.scanModelList)) {
      if (this.scanModelList[0]['address'] != item1.address) {
        this.nativeService.showToast('该物料客户地址不同！');
        return;
      }
    }
    let skulots = [];
    for (let prop in item1) {
      if (prop.search('skuLot') != -1 && item1[prop] != '') {
        skulots.push(item1[prop]);
      }
    }

    this.scanModel['skuId'] = item1.skuId;
    this.scanModel['soBillId'] = item1.soBillId;
    this.scanModel['skuName'] = item1.skuName;
    this.scanModel['cCode'] = item1.ccode;
    this.scanModel['cName'] = item1.cname;
    this.scanModel['address'] = item1.address;
    this.scanModel['lpnCode'] = item1.lpnCode;
    this.scanModel['skulots'] = skulots;
    this.scanModel['qty'] = item1.qty;
    this.scanModel['isCheck'] = false;
    this.scanModel['selected'] = true;
    this.scanModel['show'] = true;
    // if (Utils.isEmpty(this.scanModelList)) {
      this.scanModelList.push(this.scanModel);
    // } else {
    //   this.clearRecordsSelected();
    //
    //   for (let item of this.scanModelList) {
    //     // if (Utils.isObjectValueEqual1(this.scanModel, item,
    //     //   ['address', 'lpnCode', 'soBillId', 'show', 'skuId', 'skuName', 'cCode', 'cName', 'skuname', 'skulots', 'selected', 'isCheck', 'qty', 'setLotModel', 'setLotValue'])) {
    //     //   item.qty = (Number.parseInt(item.qty) + Number.parseInt(this.scanModel.qty)) + '';
    //     //   item['selected'] = true;
    //     //   this.scanModel = item;
    //     // } else {
    //       if (!Utils.contains(this.scanModelList, this.scanModel,
    //         ['address', 'lpnCode', 'soBillId', 'show', 'skuId', 'skuName', 'cCode', 'cName', 'skuname', 'skulots', 'selected', 'isCheck', 'qty', 'setLotModel', 'setLotValue'])) {
    //         this.scanModelList.push(this.scanModel);
    //         break;
    //       }
    //     // }
    //   }
    // }
    this.execTotal();
  }
  // scrollStypeTrue() {
  //   this.scrollHeight = Number(document.body.clientHeight) - 323;
  // }
  // scrollStypeFalse() {
  //   this.scrollHeight = Number(document.body.clientHeight) - 173;
  // }
  // ionViewDidLoad() {
  //   this.scrollStypeTrue();
  // }
  /**
   * 折叠状态
   */
  onIsShowClick() {
    this.isShow = !this.isShow;
    // if (this.isShow) {
    //   this.scrollStypeTrue();
    // } else {
    //   this.scrollStypeFalse();
    // }
  }
  /**
  *查看物品明细
  */
  openAsnRecordModal(mergeTrays, val: ScanModel) {
    let myModal = this.modalCtrl.create('MergeTraySelectModal', {
      records: mergeTrays,
    });
    myModal.onDidDismiss(data => {
      console.log(data);
      if (Utils.isNotEmpty(data)) {
        this.resultSkuItem = data;
        if (Utils.isEmpty(this.scanModelList)) {
          this.fillData(data, val);
        } else {
          if (this.scanModelList[0]['cCode'] == this.resultSkuItem.ccode) {
            this.fillData(data, val);
          } else {
            this.nativeService.showToast('请扫描同一客户的物料！');
          }
        }
      }
    });
    myModal.present();
  }
  /**
   * 提交出库拼托信息
   */
  submitOutStockLpnInfo() {
    if (Utils.isEmpty(this.lpnCode)) {
      this.nativeService.showToast('请填写卡板号');
      return;
    }
    if (Utils.isEmpty(this.length)) {
      this.nativeService.showToast('请填写卡板长度');
      return;
    }
    if (Utils.isEmpty(this.width)) {
      this.nativeService.showToast('请填写卡板宽度');
      return;
    }
    if (Utils.isEmpty(this.height)) {
      this.nativeService.showToast('请填写卡板高度');
      return;
    }
    if (Utils.isEmpty(this.weight)) {
      this.nativeService.showToast('请填写卡板重量');
      return;
    }

    let items = [];
    for (let item of this.scanModelList) {
      let itemTemp = {
        qty: item.qty,
        skuCode: item.skucode,
        lpnCode: item['lpnCode'],
        skuId: item['skuId'],
        soBillId: item['soBillId']
      };
      debugger;
      for (let prop in item.skuLotModel) {
        if (prop.search('skulot') != -1) {
          let index = prop.substring(prop.indexOf('t') + 1, prop.length);
          let a = parseInt(index) - 1;
          if (Utils.isNotEmpty(item['skulots'][a])) {
            itemTemp[`skuLot${index}`] = item['skulots'][a];
          }

        }
      }
      items.push(itemTemp);
    }
    let params = {
      lpnCode: this.lpnCode,
      length: this.length,
      width: this.width,
      height: this.height,
      weight: this.weight,
      mergeTraySkuVos: items,
      remaker: this.remaker
    }

    this.nativeService.showLoading();
    this.appService.httpRequest(Api.Outstock + Api.submitOutStockLpnInfo, Method.Post, params, ContenType.Json, result => {
      if (result.success) {
        this.navCtrl.pop();
      }
    });
  }
  /**
   * 查询拼托接口
   */
  getOutstockLpnInfo() {
    this.appService.httpRequest(Api.Outstock + Api.getOutstockLpnInfo, Method.Get, { taskId: this.taskId }, ContenType.Content, result => {
      this.nativeService.hideLoading();
      this.nativeService.showToast('提交成功');
      this.clearModel();
      if (result.complated) {
        this.navCtrl.pop();
      }
      // if (Utils.isNotEmpty(result.data)) {
      //   this.resultList = result.data;
      //   if (this.resultList.finish == this.resultList.total) {
      //     this.taskPDAClose();
      //   }
      // }

    });
  }
  /**
 * 关闭拼托任务
 */
  taskPDAClose() {
    this.appService.httpRequest(Api.taskPDA + Api.taskPDAClose, Method.Get, { ids: this.taskId }, ContenType.Content, result => {
      this.nativeService.hideLoading();
      this.navCtrl.pop();
    });
  }
  /**
   * 清空页面部分数据
   */
  clearModel() {
    this.scanModel = null;
    this.scanModelList = [];
    this.length = '';
    this.lpnCode = '';
    this.width = '';
    this.height = '';
    this.weight = '';
    this.isShow = true;
  }
  /**
   * 头部标签点击
   */
  handItemClick() {
    if (this.isShow) {
      if (this.resultList.lpnList.length > 0) {
        this.navCtrl.push('CollageTaskListPage', { result: this.resultList });
      }
    } else {
      if (this.scanModelList.length > 0) {
        let myModal = this.modalCtrl.create('CollageTaskBoxPage', {
          scanModelList: this.scanModelList,
          result: this.resultList
        });
        myModal.onDidDismiss(data => {
          if (data != undefined) {
            this.scanModelList = data;
          }
        });
        myModal.present();
      }
    }
  }
  /**
   * 提交按钮
   */
  btnOk() {
    if (this.isNext) {
      if (this.scanModelList.length <= 0) {
        this.nativeService.showToast('没有扫描箱码,请扫描');
        return;
      }
      this.isNext = false;
      // this.getLpnCode();
    } else {
      //提交托盘
      this.submitOutStockLpnInfo();
    }
  }
  getLpnCode() {
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.Outstock + Api.getLpnCode, Method.Get, { billNo: this.resultList.soBillNo }, ContenType.Content, result => {
      this.nativeService.hideLoading();
      if (result.success) {
        this.lpnCode = result.data;
      }
    });
  }
  /**
   * 返回按钮
   */
  exit() {
    if (this.isNext) {
      this.navCtrl.pop();
    } else {
      this.isNext = true;
    }
  }
}
