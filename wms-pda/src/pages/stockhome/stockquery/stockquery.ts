import { Component, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, Events, PopoverController, } from 'ionic-angular';
import { AppService } from "../../../services/App.service";
import { NativeService } from "../../../services/NativeService"
import { Utils } from '../../../services/Utils';
import { Api, ContenType, Method } from '../../../utils/appConstant';
import { BarcodeService, BarcodeType } from '../../../services/BarCodeService';
import { CommonService } from "../../../utils/common";
import { Storage } from '@ionic/storage';
import { AppGlobal } from '../../../services/AppGlobal';
/**
 * Generated class for the StockqueryPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-stockquery',
  templateUrl: 'stockquery.html',
})
export class StockqueryPage {
  public searchQuery: string;//搜索框文字
  public locCode: string = "";//库位编码
  public lpnCode: string = "";//容器编码
  public skuCode: string = "";//物品编码
  public lotNumber: string = "";//批次号
  public serialNumber: string = "";//序列号
  public boxCode:string = "";//箱码

  public stockVOList: any[] = [];//库存查询列表
  public skuName: string;//物品名称
  public spec: string;//规格
  public whName: string;//库房名称
  public stickQty: number;//库存数量
  public availableQty: number;//可用量
  public sName: string; //供应商
  public ownerName: string; //货主

  public current: number = 1;//当前页 
  public size: number = 10;//每页条数
  public ascs: string = "";//正序字段
  public descs: string = "";//倒序字段

  public hasPage: boolean = false;//是否有下一页
  public sortMenu: boolean = false;//是否显示排序菜单

  public isResult: boolean = false;//是否返回数据

  public sysparms: any = [];//系统参数
  public infiniteScroll:any;
  public isCheck: boolean = false;
  public isMore :boolean = false;
  constructor(public navCtrl: NavController,
    public popoverCtrl: PopoverController,
    public navParams: NavParams,
    public appService: AppService,
    public nativeService: NativeService,
    public barCodeService: BarcodeService,
    public common: CommonService,
    private storage: Storage,
    private ngZone: NgZone,
    private events: Events,
    private barcodeService: BarcodeService
  ) {
    this.storage.get('sysParam').then(data => {
      this.sysparms = data.filter(x => x.paramKey == 'system::lpnEnable')[0];
    });
  }
  ionViewWillEnter() {
    //事件注册（扫描结果接收）
    this.events.subscribe('barcode:scan', (barcode, time) => {
      let bt = this.barcodeService.GetBarcodeType(barcode);
      this.queryInit();
      this.ngZone.run(()=>{
        this.searchQuery = barcode;
        this.resetPage();
      });
      switch (bt) {
        case BarcodeType.HuoWei:
          this.ngZone.run(()=>{
            this.locCode = barcode;
            this.searchQuery = barcode;
            this.StockQuery();
          });
          break;
        case BarcodeType.TuoPan:
          this.ngZone.run(()=>{
            this.lpnCode = barcode;
            this.StockQuery();
          });
          break;
        case BarcodeType.SKU:
          this.ngZone.run(()=>{
            this.skuCode = barcode;
            this.StockQuery();
          });
          break;
        case BarcodeType.BatchNo:
          this.ngZone.run(()=>{
            this.lotNumber = barcode;
            this.StockQuery();
          });
          break;
        case BarcodeType.SerialNumber:
          this.ngZone.run(()=>{
            this.serialNumber = barcode;
            this.StockQuery();
          });
          break;
        case BarcodeType.BoxCode:
          this.ngZone.run(()=>{
            this.boxCode = barcode;
            this.StockQuery();
          });
        break;
      }
    });
  }
  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
  }
  /**
   * 选中列项
   * @param stockVO 
   */
  itemInfo(stockVO) {
    if (this.isCheck) {
      stockVO.CheckStock = !stockVO.CheckStock;
    } else {
      if (stockVO.isSn == 1) {
        this.navCtrl.push('StockdetailPage', { stockVO: stockVO });
      }
    }
  }
  keyEnter(event) {
    if (event && event.keyCode == 13) {
      this.resetPage();
      this.SearchQuery();
    }
  }
  sortAsc(sortName) {
    this.ascs = sortName;
    this.resetPage();
    this.SearchQuery();
  }
 
  resetPage(){
    this.stockVOList = [];
    if(this.infiniteScroll){
      this.infiniteScroll.enable(true);
    }
    this.size = 10;
    this.current = 1;
  }
  SearchQuery() {
    if (Utils.isEmpty(this.searchQuery)) {
      this.nativeService.showToast('请输入需要搜索的内容');
      return;
    }
    this.isResult = false;
    let bt = this.barCodeService.GetBarcodeType(this.searchQuery);
    this.queryInit();
    debugger
    switch (bt) {
      case BarcodeType.HuoWei:
        this.locCode = this.searchQuery;
        break;
      case BarcodeType.TuoPan:
        this.lpnCode = this.searchQuery;
        break;
      case BarcodeType.SKU:
        this.skuCode = this.searchQuery;
        break;
      case BarcodeType.BatchNo:
        this.lotNumber = this.searchQuery;
        break;
      case BarcodeType.SerialNumber:
        this.serialNumber = this.searchQuery;
        break;
      case BarcodeType.BoxCode:
        this.boxCode = this.searchQuery;
        break;
    }
    this.StockQuery();
  }
  StockQuery() {
    let params = {};
    if(Utils.isNotEmpty(this.boxCode)){
        this.storage.get("sysParam").then(v=>{
          let flag=false;
          v.forEach(item => {
            if(item.paramKey=='pda.stock.keywords'){
              flag=true;
              let value = item.paramValue;
              let valueSplit=value.split(",");
              let aa=valueSplit.filter(e=>{
                let boxstr=this.boxCode.toLowerCase();
                let str = e.toLowerCase();
                return !(boxstr.indexOf(Utils.clearSpace(str))!=-1)
              });
              if(aa&&aa.length>0){
                this.nativeService.showToast("箱码格式不正确！");
                return;
              }else{
                this.barcodeService.ScanSkuBarcode(this.boxCode).subscribe(val => {
                  valueSplit.forEach(e => {
                    let str = Utils.clearSpace(e).toLowerCase();
                    if(str.indexOf("skulot")!=-1){
                      let attrName = "skuLot"+str.substring(6,str.length);
                      params[attrName]=val.skuLotModel[str];
                    }else{
                      if(str=='skucode'){
                        params['skuCode'] = val[str];
                      }
                    }
                  });
                  params["boxCode"] = "1"
                  this.queryStockRequest(params);
                })
              }
            }
          });
          if(!flag){
            this.nativeService.showToast("您还未设置箱码扫描规则无法为您查询");
          }
        });
    }else{
      params['locCode']=this.locCode,
      params['lpnCode']=this.lpnCode,
      params['skuCode']=this.skuCode,
      params['lotNumber']=this.lotNumber,
      params['serialNumber']=this.serialNumber
      params["boxCode"] = "0"
      this.queryStockRequest(params);
    }
    
  }
  queryStockRequest(params){
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.api + Api.StockQuery
      + "?current=" + this.current
      + "&size=" + this.size
      + "&ascs=" + this.ascs
      + "&type=" + params["boxCode"]
      + "&descs" + this.descs,
      Method.Post, params, ContenType.Json, result => {
        this.nativeService.hideLoading();
        if (Utils.isEmpty(result.data.records)) {
          if(this.infiniteScroll){
            this.infiniteScroll.enable(false);
            this.infiniteScroll.complete(); 
          }else{
            this.nativeService.showToast('暂无库存信息！');
          }
          return;
        } 
        this.isResult = true;
        this.sortMenu = true;
        this.stockVOList = this.stockVOList.concat(result.data.records);
        this.current++;
        if (this.infiniteScroll) {
          this.infiniteScroll.complete();  
        }
      });
  }
  queryInit() {
    this.locCode = "";
    this.lpnCode = "";
    this.skuCode = "";
    this.lotNumber = "";
    this.serialNumber = "";
    this.boxCode = "";
  }
  /**
   * 是否显示打印
   */
  pritIsShow() { 
    this.isCheck = !this.isCheck;
    this.isMore = !this.isMore;
    if (!this.isCheck&&Utils.isNotEmpty(this.stockVOList)) {
      this.stockVOList = this.stockVOList.map(function (item, index, arr) {
        item.CheckStock = false;
        return item;
      });
    }
  }
  /**
   * 标签打印
   */
  btnOk() {
    if (this.isCheck) {
      //获取选择的库存
      if (Utils.isNotEmpty(this.stockVOList)) {
        let stockVOListIsTrue = this.stockVOList.filter(x => x.CheckStock == true);
        if (stockVOListIsTrue.length > 0) {
          this.navCtrl.push('StockPritPage', { stockVOListIsTrue: stockVOListIsTrue });
        } else {
          this.nativeService.showToast('请选择要打印的库位');
        }
      }
    } else {
      this.nativeService.showToast('请点击右上【打印】按钮开启打印');
    }
  }
  /**
   * 返回上一层
   */
  exit() {
    this.navCtrl.pop();
  }
  presentPopover(myEvent) {
    let popover = this.popoverCtrl.create('PopoverPage',{show_item_3:false});
    popover.onDidDismiss(data => {
      if (Utils.isNotEmpty(data) && Utils.isNotEmpty(this.stockVOList)) {
        console.log(this.stockVOList);
        if (data == 'allCheck') {
          //全选
          for (let index = 0; index < this.stockVOList.length; index++) {
            this.stockVOList[index].CheckStock = true;
          }
        } else if (data == 'clearCheck') {
          //取消全选
          for (let index = 0; index < this.stockVOList.length; index++) {
            this.stockVOList[index].CheckStock = false;
          }
        }
      }
    });
    popover.present({
      ev: myEvent
    });
  }
  skuStockIsCheckDel() {
    this.stockVOList = this.stockVOList.filter(x => x.CheckStock === false);
  }
  loadData(infiniteScroll) { 
    this.infiniteScroll = infiniteScroll;
    this.SearchQuery();
  }
}
